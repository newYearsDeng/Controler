#include "getdll.h"
#include "../common/param.h"
#include "../common/BCT645.h"
#include <stdio.h>
#include <string.h>


#define DI1 0xbcdc0001   //����״̬��1

int data_pack_getsiLuDengKong(INPUT_STRU *in, OUTPUT_STRU *out) {
    int err_code = 0;
    unsigned char infoDataBuff[200], meterAddr[6], DI[4];
    unsigned char *DLT645_Data, DLT645_Frame[300];
    unsigned short DLT645_DataLen;
    int ret, infoDataLen, paramLen;
    unsigned long new_DI_tmp;
    unsigned char new_DI[4];

    BCT645_FC_T FC = READ_DATA;
    int DI_len = 4;
    unsigned char *psw = NULL;
    unsigned char *op = NULL;

    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    err_code = find_param(in, PARA_DI, DI, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    if (DI[0] == 0xbc && DI[1] == 0xdc && DI[2] == 0x00 && DI[3] == 0x01) {
        new_DI_tmp = DI1;
        getDI(new_DI, new_DI_tmp);
    } else {
        return -1;
    }

    DLT645_Data = infoDataBuff;
    DLT645_DataLen = infoDataLen = 0;

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


int data_result_getsiLuDengKong(INPUT_STRU *in, OUTPUT_STRU *out) {
    unsigned char meterAddr[6], DI[4], data_buf[200];
    unsigned char DLT645_frame[300], DLT645_Data[200];
    unsigned short DLT645_frameLen, DLT645_DataLen = 0;
    int ret, paramLen, i, data_buf_len;
    int err_code = 0;

    BCT645_FC_T FC = READ_DATA;
    unsigned long new_DI_tmp;
    unsigned char new_DI[4];
    int DI_len = 4;
    int one, two, three, four;

    DLT645_frameLen = str2hex(out->frame, out->frame_len, sizeof(DLT645_frame), DLT645_frame);


    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    err_code = find_param(in, PARA_DI, DI, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    if (DI[0] == 0xbc && DI[1] == 0xdc && DI[2] == 0x00 && DI[3] == 0x01) {
        new_DI_tmp = DI1;
        getDI(new_DI, new_DI_tmp);
    } else {
        return -1;
    }


    Data_t BCT645frame, dataOut;
    BCT645frame.data = DLT645_frame;
    BCT645frame.len = DLT645_frameLen;

    dataOut.data = DLT645_Data;
    dataOut.len = sizeof(DLT645_Data);

    ret = BCT645_AllDataParse(meterAddr, FC, new_DI, DI_len, in->subSEQ, &BCT645frame, &dataOut);
    if (ret == DLT645_ERR_OK) {
        data_buf_len = DLT645_frame[9] - 4;
        memcpy(data_buf, DLT645_frame + 14, data_buf_len);

        for (i = 0; i < data_buf_len; i++) {
            data_buf[i] -= 0x33;
        }
        one = data_buf[1] & 0x01;
        two = (data_buf[1] >> 1) & 0x01;
        three = (data_buf[1] >> 2) & 0x01;
        four = (data_buf[1] >> 3) & 0x01;
        sprintf(out->data, "{\"one:\"\"%d\",\"two:\"\"%d\",\"three:\"\"%d\",\"four:\"\"%d\"}", one,
                two, three, four);               //������1����̵����ϣ�0�����̵�������
        out->data_len = strlen(out->data);
    }
    return ret;
}
