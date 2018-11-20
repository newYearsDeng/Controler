#include "../common/BCT645.h"
#include "dll.h"
#include "../common/param.h"
#include <stdio.h>
#include <string.h>

#define DI1 0x97200101       //��բ
#define DI2 0x97200102      //��բ

int data_pack_socket(INPUT_STRU *in, OUTPUT_STRU *out) {
    unsigned char infoDataBuff[200], meterAddr[6];
    unsigned char DLT645_Frame[300], *DLT645_Data;
    unsigned short DLT645_DataLen = 0;
    int ret, paramLen, infoDataLen = 0;
    int err_code = 0;
    unsigned long new_DI;

    BCT645_FC_T FC = WRITE_DATA;
//	unsigned char DI[4] = { 0x97, 0x20, 0x01, 0x01 };
    unsigned char DI[4];
    int DI_len = 4;
    unsigned char psw[4] = {0X00, 0X00, 0X00, 0X02};
    unsigned char opCode[4] = {0x12, 0x34, 0x56, 0x78};

    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    err_code = find_param(in, PARA_DI, DI, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    if (DI[0] == 0x97 && DI[1] == 0x20 && DI[2] == 0x01 && DI[3] == 0x01) {
        new_DI = DI1;
    } else if (DI[0] == 0x97 && DI[1] == 0x20 && DI[2] == 0x01 && DI[3] == 0x02) {
        new_DI = DI2;
    } else {
        return -1;
    }

    infoDataBuff[0] = 0x0f;
    infoDataLen = 1;

    DLT645_Data = infoDataBuff;
    DLT645_DataLen = infoDataLen;

    Data_t packData;
    packData.data = DLT645_Data;
    packData.len = DLT645_DataLen;

    Data_t DLT645_FrameOut = {DLT645_Frame, sizeof(DLT645_Frame)};
    ret = BCT645_AllDataPack(meterAddr, FC, DI, DI_len, in->subSEQ, psw, opCode, &packData,
                             &DLT645_FrameOut);

    if (ret == DLT645_ERR_OK) {
        out->frame_len = hex2str(DLT645_FrameOut.data, DLT645_FrameOut.len, DLT645_FrameOut.len * 2,
                                 out->frame);
    }
    out->frame[DLT645_FrameOut.len * 2] = '\0';

    return ret;
}


int data_result_socket(INPUT_STRU *in, OUTPUT_STRU *out) {
    unsigned char meterAddr[6];
    unsigned char DLT645_frame[300], DLT645_Data[200];
    unsigned short DLT645_frameLen, DLT645_DataLen = 0;
    int ret, paramLen;
    int err_code = 0;

    BCT645_FC_T FC = WRITE_DATA;
//	unsigned char DI[4] = { 0xbc, 0x0f, 0x00, 0x01 };
    unsigned char *DI = NULL;
    int DI_len = 0;


    DLT645_frameLen = str2hex(out->frame, out->frame_len, sizeof(DLT645_frame), DLT645_frame);

    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    Data_t BCT645frame, dataOut;
    BCT645frame.data = DLT645_frame;
    BCT645frame.len = DLT645_frameLen;

    dataOut.data = DLT645_Data;
    dataOut.len = sizeof(DLT645_Data);

    ret = BCT645_AllDataParse(meterAddr, FC, DI, DI_len, in->subSEQ, &BCT645frame, &dataOut);

    return ret;
}
