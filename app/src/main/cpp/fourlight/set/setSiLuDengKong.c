#include "setdll.h"
#include "../../common/param.h"
#include "../../common/BCT645.h"
#include <stdio.h>
#include <string.h>


#define DI1 0x97200101       //��բ
#define DI2 0x97200102       //��բ

int data_pack_setsiLuDengKong(INPUT_STRU *in, OUTPUT_STRU *out) {
    int err_code = 0;
    unsigned char infoDataBuff[200], meterAddr[6], DI[4], mode[2];
    unsigned char *DLT645_Data, DLT645_Frame[300];
    unsigned short DLT645_DataLen;
    int ret, infoDataLen, paramLen;
    unsigned long new_DI_tmp;
    unsigned char new_DI[4];

    BCT645_FC_T FC = WRITE_DATA;
    int DI_len = 4;
    unsigned char psw[4] = {0x00, 0x00, 0x00, 0x00};
    unsigned char op[4] = {0x12, 0x34, 0x56, 0x78};

    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    err_code = find_param(in, PARA_DI, DI, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    if (DI[0] == 0x97 && DI[1] == 0x20 && DI[2] == 0x01 && DI[3] == 0x01) {
        new_DI_tmp = DI1;
        getDI1(new_DI, new_DI_tmp);
    } else if (DI[0] == 0x97 && DI[1] == 0x20 && DI[2] == 0x01 && DI[3] == 0x02) {
        new_DI_tmp = DI2;
        getDI1(new_DI, new_DI_tmp);
    } else {
        return -1;
    }

    err_code = find_param(in, PARA_INFO_DATA, infoDataBuff, &paramLen);
    if (err_code != 0) {
        return -1;
    }
    infoDataLen = paramLen;


    DLT645_Data = infoDataBuff;
    DLT645_DataLen = infoDataLen;

    Data_t packData = {.data = DLT645_Data, .len = DLT645_DataLen};
    Data_t DLT645_FrameOut = {.data = DLT645_Frame, .len = sizeof(DLT645_Frame)};
    ret = BCT645_AllDataPack(meterAddr, FC, new_DI, DI_len, in->subSEQ, psw, op, &packData,
                             &DLT645_FrameOut);
    if (ret == DLT645_ERR_OK) {
        out->frame_len = hex2str(DLT645_FrameOut.data, DLT645_FrameOut.len, DLT645_FrameOut.len * 2,
                                 out->frame);
    }
    out->frame[out->frame_len] = '\0';
    BCT645_PrintErrMsg(ret, out->error, 200, 0);
    out->err_len = strlen(out->error);

    return 0;
}


int data_result_setsiLuDengKong(INPUT_STRU *in, OUTPUT_STRU *out) {
    unsigned char meterAddr[6], DI[4];
    unsigned char DLT645_frame[300], DLT645_Data[200];
    unsigned short DLT645_frameLen, DLT645_DataLen = 0;
    int ret, paramLen;
    int err_code = 0;

    BCT645_FC_T FC = WRITE_DATA;
    unsigned char *new_DI = NULL;
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

    ret = BCT645_AllDataParse(meterAddr, FC, new_DI, DI_len, in->subSEQ, &BCT645frame, &dataOut);

    return ret;
}
void getDI1(unsigned char *DI, unsigned long DI_tmp)
{
    DI[0] = (unsigned char)(DI_tmp >> 24);
    DI[1] = (unsigned char)(DI_tmp >> 16);
    DI[2] = (unsigned char)(DI_tmp >> 8);
    DI[3] = (unsigned char)(DI_tmp);
}