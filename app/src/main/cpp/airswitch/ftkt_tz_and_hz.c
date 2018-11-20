#include "dll.h"
#include "../common/param.h"
#include "../common/BCT645.h"
#include "../common/des.h"

#include <stdio.h>
#include <string.h>


 int data_pack_air_switch(INPUT_STRU *in, OUTPUT_STRU *out) {
    unsigned char keyt[200], keyt2[200], signBuff[5], infoDataBuff[200], infoDataBuffTemp[200], infoAddrBuff[200], meterAddr[6], meterAddrTemp[6];
    unsigned char DLT645_Frame[300], *DLT645_Data;
    unsigned short DLT645_DataLen = 0;
    int err_code = 0;
    int ret, infoDataLen, paramLen, keytLen;
    int i;
    BCT645_FC_T fc;

    unsigned char fixed_code[16] = {0xF0, 0xE1, 0xD2, 0xC3, 0xB4, 0xA5, 0x96, 0x87, 0x0F, 0x1E,
                                    0x2D, 0x3C, 0x4B, 0x5A, 0x69, 0x78};
    unsigned char subKey1[192];
    unsigned char subKey2[192];

    err_code = find_param(in, PARA_KEY, keyt, &paramLen);
    if (err_code != 0) {
        return -1;
    }
    for (int i = 0; i < 16; i++) {
        printf("%02x", keyt[i]);
    }

    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    err_code = find_param(in, PARA_STYLE, signBuff, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    if (signBuff[0] != 0x1a && signBuff[0] != 0x4b) {
        return -1;
    }

    if (in->subSEQ == 0) {
        fc = CUSTOM_03H;
        infoDataBuff[0] = 0x15;
        infoDataBuff[1] = 0x00;
        infoDataLen = 2;
    } else {
        fc = CUSTOM_1CH;
        infoDataLen = str2hex(out->frame, out->frame_len, out->frame_len / 2, infoDataBuffTemp);
        memcpy(infoDataBuff, infoDataBuffTemp + 10, 8);
        for (i = 0; i < 8; i++) {
            infoDataBuff[i] -= 0x33;
        }
        infoDataLen = 8;
    }

    /*
    memset(infoAddrBuff, 0, 10);
    memcpy(infoAddrBuff + 10, meterAddr, 6);

    SetSubKey(subKey1, fixed_code);
    Des_Go(keyt2, keyt, 16, subKey1, DECRYPT);

    SetSubKey(subKey2, keyt2);
    Des_Go(infoAddrBuff, infoAddrBuff, 16, subKey2, ENCRYPT);
    */

    //SetSubKey(subKey1, fixed_code);
    //Des_Go(keyt, keyt, 16, subKey1, DECRYPT);


    DLT645_Data = infoDataBuff;
    DLT645_DataLen = infoDataLen;

    Data_t packData;
    packData.data = DLT645_Data;
    packData.len = DLT645_DataLen;

    Data_t DLT645_FrameOut = {DLT645_Frame, sizeof(DLT645_Frame)};
    unsigned char sign;
    if (in->subSEQ == 0) {
        sign = 0x1a;
    } else {
        sign = signBuff[0];
    }

    ret = BCT645_Splitairconditioning_DataPack(meterAddr, sign, fc, &packData, keyt,
                                               &DLT645_FrameOut);


    if (ret == DLT645_ERR_OK) {
        out->frame_len = hex2str(DLT645_FrameOut.data, DLT645_FrameOut.len, DLT645_FrameOut.len * 2,
                                 out->frame);
    }
    out->frame[out->frame_len] = '\0';

    return ret;
}

int data_result_air_switch(INPUT_STRU *in, OUTPUT_STRU *out) {
    int ret;
    if (in->subSEQ == 0) {
        ret = data_result_tz_air_switch(in, out);
        if (ret == DLT645_ERR_OK) {
            in->subSEQ++;
            ret = data_pack_air_switch(in, out);

            if (ret == DLT645_ERR_OK) {
                return DLT645_ERR_SUB;
            }
        }
    } else {
        ret = data_result_tz_air_switch(in, out);
    }
    return ret;
}

int data_result_tz_air_switch(INPUT_STRU *in, OUTPUT_STRU *out) {
    unsigned char meterAddr[6];
    unsigned char DLT645_frame[300], DLT645_Data[200];
    unsigned short DLT645_frameLen;

    int ret, paramLen;
    int err_code = 0;

    unsigned char keyt[200], infoDataBuff[200];
    unsigned char DLT645_Frame[300];
    unsigned short DLT645_DataLen = 0;
    int infoDataLen, keytLen;

    DLT645_frameLen = str2hex(out->frame, out->frame_len, sizeof(DLT645_frame), DLT645_frame);


    err_code = find_param(in, PARA_KEY, keyt, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    Data_t BCT645frame, dataOut;
    BCT645frame.data = DLT645_frame;
    BCT645frame.len = DLT645_frameLen;

    dataOut.data = DLT645_Data;
    dataOut.len = sizeof(DLT645_Data);
    //    ret=BCT645_DesGXDZGMFrameResult(meterAddr, &BCT645frame, new_DI_1, &dataOut);
    unsigned char FC;
    if (in->subSEQ == 0) {
        FC = 0x03;
    } else
        FC = 0x1c;

    ret = BCT645_DataFrameParse(meterAddr, 0, in->subSEQ, FC, 0, 0, &BCT645frame, &dataOut);

    return ret;
}
