#include "dll.h"
#include "../../common/param.h"
#include "../../common/BCT645.h"
#include <stdio.h>
#include <string.h>


#define DI1 0xbcaa0001      //�¶ȼ�ģʽ
#define DI2 0xbcaa0002       //���ٵ�λ
#define DI3 0xbcaa0005       //�ͷ�ģʽ
#define DI4 0x02ff5555       //�������������ݿ飨�й��ܵ��ܣ���ѹ�����������й����ʣ��޹����ʣ�ff���棩������Ƶ�ʣ��������أ��յ�����״̬��00-����01-�أ���

int data_pack_getZhongYangKongTiao(INPUT_STRU *in, OUTPUT_STRU *out) {
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

    if (DI[0] == 0xbc && DI[1] == 0xaa && DI[2] == 0x00 && DI[3] == 0x01) {
        new_DI_tmp = DI1;
        getDI(new_DI, new_DI_tmp);
    } else if (DI[0] == 0xbc && DI[1] == 0xaa && DI[2] == 0x00 && DI[3] == 0x02) {
        new_DI_tmp = DI2;
        getDI(new_DI, new_DI_tmp);
    } else if (DI[0] == 0xbc && DI[1] == 0xaa && DI[2] == 0x00 && DI[3] == 0x05) {
        new_DI_tmp = DI3;
        getDI(new_DI, new_DI_tmp);
    } else if (DI[0] == 0x02 && DI[1] == 0xff && DI[2] == 0x55 && DI[3] == 0x55) {
        new_DI_tmp = DI4;
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


int data_result_getZhongYangKongTiao(INPUT_STRU *in, OUTPUT_STRU *out) {
    unsigned char meterAddr[6], DI[4], data_buf[200];
    unsigned char DLT645_frame[300], DLT645_Data[200];
    unsigned short DLT645_frameLen, DLT645_DataLen = 0;
    int ret, paramLen, i, data_buf_len;
    int err_code = 0;

    BCT645_FC_T FC = READ_DATA;
    unsigned long new_DI_tmp;
    unsigned char new_DI[4];
    int DI_len = 4;

    char out_data[1024];

    DLT645_frameLen = str2hex(out->frame, out->frame_len, sizeof(DLT645_frame), DLT645_frame);


    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    err_code = find_param(in, PARA_DI, DI, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    if (DI[0] == 0xbc && DI[1] == 0xaa && DI[2] == 0x00 && DI[3] == 0x01) {
        new_DI_tmp = DI1;
        getDI(new_DI, new_DI_tmp);
    } else if (DI[0] == 0xbc && DI[1] == 0xaa && DI[2] == 0x00 && DI[3] == 0x02) {
        new_DI_tmp = DI2;
        getDI(new_DI, new_DI_tmp);
    } else if (DI[0] == 0xbc && DI[1] == 0xaa && DI[2] == 0x00 && DI[3] == 0x05) {
        new_DI_tmp = DI3;
        getDI(new_DI, new_DI_tmp);
    } else if (DI[0] == 0x02 && DI[1] == 0xff && DI[2] == 0x55 && DI[3] == 0x55) {
        new_DI_tmp = DI4;
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
        int k = 0;
        data_buf_len = DLT645_frame[9] - 4;
        memcpy(data_buf, DLT645_frame + 14, data_buf_len);

        for (i = 0; i < data_buf_len; i++) {
            data_buf[i] -= 0x33;
        }

        if (new_DI_tmp == DI1) {
            sprintf(out->data, "{\"temperature:\"\"%02x\",\"mode:\"\"%02x\"}", data_buf[0],
                    data_buf[1]);               //�趨�¶ȼ�ģʽ���趨�¶ȣ����ֽڣ���ģʽ��00���䣬01��ů�������ֽڣ���

        } else if (new_DI_tmp == DI2) {
            sprintf(out->data, "{\"windspeed:\"\"%02x\"}", data_buf[0]);
        } else if (new_DI_tmp == DI3) {
            sprintf(out->data, "{\"mode:\"\"%02x\"}", data_buf[0]);
        } else if (new_DI_tmp == DI4) {
            for (k = 0; k < data_buf_len - 1; k++) {
                data_buf[k] = bcd_to_decimal_zykt(data_buf[k]);
            }
            out_data_to_string_more_one(data_buf, out_data);
            sprintf(out->data, "{%s}", out_data);
        } else {
            return -1;
        }

        out->data_len = strlen(out->data);
    } else {
        sprintf(out->data, "result err! errcode:%d", ret);
        out->data_len = strlen(out->data);
    }

    return ret;
}


void out_data_to_string_more_one(unsigned char *frame_data, char *out_data) {
    int bit_len = 0;

    char str_in[50];
    char out[100];
    unsigned char value_buf[10];

    strcpy(out_data, "");

    sprintf(str_in, "ygzdn");
    frame_to_string_templet_zykt(str_in, out_data, frame_data, &bit_len, 4, 2, 0);

    sprintf(str_in, "dy");
    frame_to_string_templet_zykt(str_in, out_data, frame_data, &bit_len, 2, 1, 0);

    sprintf(str_in, "dl");
    frame_to_string_templet_zykt(str_in, out_data, frame_data, &bit_len, 3, 3, 1);

    sprintf(str_in, "zyggl");
    frame_to_string_templet_zykt(str_in, out_data, frame_data, &bit_len, 3, 4, 1);

//	sprintf(str_in, "wggl");
//	frame_to_string_templet(str_in, out_data, frame_data, bit_len, 3, 0, 0);
    sprintf(str_in, "wggl");
    sprintf(out, "\"%s\":\"ffffff\",", str_in);
    strcat(out_data, out);
    bit_len += 3;

    sprintf(str_in, "dwpl");
    frame_to_string_templet_zykt(str_in, out_data, frame_data, &bit_len, 2, 2, 0);

    sprintf(str_in, "glys");
    frame_to_string_templet_zykt(str_in, out_data, frame_data, &bit_len, 2, 3, 1);

    sprintf(str_in, "ktkgzt");
    memcpy(value_buf, frame_data + bit_len, 1);
    sprintf(out, "\"%s\":\"%02x\"", str_in, value_buf[0]);
    strcat(out_data, out);
    bit_len += 1;

}


int bcd_to_decimal_zykt(unsigned char bcd) {
    return ((bcd >> 4) * 10 + (bcd & 0x0f));
}

unsigned char decimal_to_bcd_zykt(int dec) {
    return ((dec / 10 << 4) + dec % 10);
}

bool is_char_have_letter_zykt(const unsigned char data) {
    if ((data >> 4) > 0x09 || (data & 0x0f) > 0x09)
        return true;
    else
        return false;
}

bool is_have_letter_zykt(const unsigned char *data, const unsigned char byte_len) {
    int i;
    int count = 0;
    unsigned char data_tmp[20];
    memcpy(data_tmp, data, byte_len);
    for (i = 0; i < byte_len; i++) {
        if ((data_tmp[i] >> 4) > 0x09) {
            return true;
        }
    }

    return false;
}

void
frame_to_string_templet_zykt(char *str_in, char *data_str, unsigned char *frame_data, int *bit_len,
                             int byte_len, int decimal_len, unsigned char is_sign_bit) {
    unsigned char field_buf[20];           //�ֶ��ֽ����ݣ�bcd)

    char field_value[50];       // �ֶεľ�������(str)
    char field_str[100];          //�ֶμ����ֶ�����


    memcpy(field_buf, frame_data + *bit_len, byte_len);
    *bit_len += byte_len;
    if (is_have_letter_zykt(field_buf, byte_len) == true) {
        sprintf(field_value, "");
    } else {
        reverse_data_frame_to_string_zykt(field_buf, field_value, decimal_len, byte_len,
                                          is_sign_bit);
    }
    sprintf(field_str, "\"%s\":\"%s\",", str_in, field_value);
    strcat(data_str, field_str);
}

void reverse_data_frame_to_string_zykt(unsigned char *data, char *out, unsigned char decimal,
                                       unsigned char byte_size, unsigned char is_sign_bit) {
    int i;
    int value;      //�������������ݵĺ�
    int decimal_num;
    double decimal_flag;
    double res;   //���ս��
    bool flag = false;

    reverse_array(data, byte_size);

    if ((is_sign_bit == 1) && (data[0] >= 80)) {
        data[0] -= 80;
        flag = true;
    }
    value = count_data_sum_zykt(data, byte_size);

    if (decimal == 0) {
        res = value;
    } else {
        decimal_num = 1;
        double decimal_flag = 1;
        for (i = 0; i < decimal; i++) {
            decimal_num *= 10;
            decimal_flag *= 0.1;
        }
        res = value / decimal_num + value % decimal_num * decimal_flag;
    }

    if ((is_sign_bit == 1) && flag) {
        switch (decimal) {
            case 0:
                sprintf(out, "-%.0f", res);
                break;
            case 1:
                sprintf(out, "-%.1f", res);
                break;
            case 2:
                sprintf(out, "-%.2f", res);
                break;
            case 3:
                sprintf(out, "-%.3f", res);
                break;
            case 4:
                sprintf(out, "-%.4f", res);
                break;
            case 5:
                sprintf(out, "-%.5f", res);
                break;
            case 6:
                sprintf(out, "-%.6f", res);
                break;
            case 7:
                sprintf(out, "-%.7f", res);
                break;
            case 8:
                sprintf(out, "-%.8f", res);
                break;
            default:
                sprintf(out, "-%.0f", res);
                break;
        }
    } else {
        switch (decimal) {
            case 0:
                sprintf(out, "%.0f", res);
                break;
            case 1:
                sprintf(out, "%.1f", res);
                break;
            case 2:
                sprintf(out, "%.2f", res);
                break;
            case 3:
                sprintf(out, "%.3f", res);
                break;
            case 4:
                sprintf(out, "%.4f", res);
                break;
            case 5:
                sprintf(out, "%.5f", res);
                break;
            case 6:
                sprintf(out, "%.6f", res);
                break;
            case 7:
                sprintf(out, "%.7f", res);
                break;
            case 8:
                sprintf(out, "%.8f", res);
                break;
            default:
                sprintf(out, "%.0f", res);
                break;
        }
    }

}

int count_data_sum_zykt(unsigned char *data, unsigned char byte_size) {
    int i;
    int sum = 0;
    for (i = 0; i < byte_size; i++) {
        sum *= 100;
        sum += data[i];
    }
    return sum;
}