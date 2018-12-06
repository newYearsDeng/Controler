#include "dll.h"
#include "../../common/param.h"
#include "../../common/BCT645.h"

#include <stdio.h>
#include <string.h>


#define DI1 0x00000000       //组合有功总电能    6整2小
#define DI2 0x02010100      //a相电压           3整1小
#define DI3 0x02020100      //a相电流           3整3小
#define DI4 0x02030000      //瞬时总有功功率    2整4小
#define DI5 0x02800002      //电网频率          2整2小
#define DI6 0x02060000      //总功率因数        1整3小
#define DI7 0x04000503      //读取跳合闸状态 国网   3字节
#define DI8 0xbc020000      //读取跳合闸状态    3字节

#define DECIMAL_LEN 2
#define FLAG 0

int data_pack_cmd(INPUT_STRU *in, OUTPUT_STRU *out) {
    int err_code = 0;
    unsigned char infoDataBuff[200], meterAddr[6], DI[4];
    unsigned char *DLT645_Data, DLT645_Frame[300];
    unsigned short DLT645_DataLen;
    int ret, infoDataLen, paramLen;
    unsigned long new_DI;

    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    err_code = find_param(in, PARA_DI, DI, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    if (DI[0] == 0x00 && DI[1] == 0x00 && DI[2] == 0x00 && DI[3] == 0x00) {
        new_DI = DI1;
    } else if (DI[0] == 0x02 && DI[1] == 0x01 && DI[2] == 0x01 && DI[3] == 0x00) {
        new_DI = DI2;
    } else if (DI[0] == 0x02 && DI[1] == 0x02 && DI[2] == 0x01 && DI[3] == 0x00) {
        new_DI = DI3;
    } else if (DI[0] == 0x02 && DI[1] == 0x03 && DI[2] == 0x00 && DI[3] == 0x00) {
        new_DI = DI4;
    } else if (DI[0] == 0x02 && DI[1] == 0x80 && DI[2] == 0x00 && DI[3] == 0x02) {
        new_DI = DI5;
    } else if (DI[0] == 0x02 && DI[1] == 0x06 && DI[2] == 0x00 && DI[3] == 0x00) {
        new_DI = DI6;
    } else if (DI[0] == 0x04 && DI[1] == 0x00 && DI[2] == 0x05 && DI[3] == 0x03) {
        new_DI = DI7;
    } else if (DI[0] == 0xbc && DI[1] == 0x02 && DI[2] == 0x00 && DI[3] == 0x00) {
        new_DI = DI8;
    } else {
        return -1;
    }

    DLT645_Data = infoDataBuff; //+1;
    DLT645_DataLen = infoDataLen = 0;

    Data_t packData = {.data=DLT645_Data, .len=DLT645_DataLen};
    Data_t DLT645_FrameOut = {.data=DLT645_Frame, .len=sizeof(DLT645_Frame)};
    ret = BCT645_ReadDataFramePack(meterAddr, new_DI, in->subSEQ, &packData, &DLT645_FrameOut);
    if (ret == DLT645_ERR_OK) {
        out->frame_len = hex2str(DLT645_FrameOut.data, DLT645_FrameOut.len, DLT645_FrameOut.len * 2,
                                 out->frame);
    }
    BCT645_PrintErrMsg(ret, out->error, 200, 0);
    out->err_len = strlen(out->error);

    return 0;
}

int data_result_cmd(INPUT_STRU *in, OUTPUT_STRU *out) {
    int err_code = 0;
    unsigned char meterAddr[6], DI[4], infoDataBuff[200];
    unsigned char DLT645_frame[300], DLT645_Data[1000], data_buf[50];
    unsigned short DLT645_frameLen, DLT645_DataLen = 0;
    int ret, infoDataLen, paramLen;
    unsigned long new_DI;
    unsigned char decimal_len = 0;
    int i, data_buf_len, flag;

    DLT645_frameLen = str2hex(out->frame, out->frame_len, sizeof(DLT645_frame), DLT645_frame);

    memcpy(DI, DLT645_frame + 10, 4);
    reverse_array(DI, 4);
    for (i = 0; i < 4; i++) {
        DI[i] -= 0x33;
    }

    if (DI[0] == 0x00 && DI[1] == 0x00 && DI[2] == 0x00 && DI[3] == 0x00) {
        new_DI = DI1;
        decimal_len = 2;
    } else if (DI[0] == 0x02 && DI[1] == 0x01 && DI[2] == 0x01 && DI[3] == 0x00) {
        new_DI = DI2;
        decimal_len = 1;
    } else if (DI[0] == 0x02 && DI[1] == 0x02 && DI[2] == 0x01 && DI[3] == 0x00) {
        new_DI = DI3;
        decimal_len = 3;
    } else if (DI[0] == 0x02 && DI[1] == 0x03 && DI[2] == 0x00 && DI[3] == 0x00) {
        new_DI = DI4;
        decimal_len = 4;
    } else if (DI[0] == 0x02 && DI[1] == 0x80 && DI[2] == 0x00 && DI[3] == 0x02) {
        new_DI = DI5;
        decimal_len = 2;
    } else if (DI[0] == 0x02 && DI[1] == 0x06 && DI[2] == 0x00 && DI[3] == 0x00) {
        new_DI = DI6;
        decimal_len = 3;
    } else if (DI[0] == 0x04 && DI[1] == 0x00 && DI[2] == 0x05 && DI[3] == 0x03) {
        new_DI = DI7;
        decimal_len = -2;
    } else if (DI[0] == 0xbc && DI[1] == 0x02 && DI[2] == 0x00 && DI[3] == 0x00) {
        new_DI = DI8;
        decimal_len = -1;
    } else {
        return -1;
    }

    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
    if (err_code != 0) {
        return -1;
    }

    Data_t DLT645frame, dataOut;
    DLT645frame.data = DLT645_frame;
    DLT645frame.len = DLT645_frameLen;
    if (out->data_len) {
        DLT645_DataLen = str2hex(out->data, out->data_len, sizeof(DLT645_Data), DLT645_Data);
    }
    dataOut.data = DLT645_Data + DLT645_DataLen;
    dataOut.len = sizeof(DLT645_Data) - DLT645_DataLen;

    ret = BCT645_ReadDataFrameParse(meterAddr, new_DI, in->subSEQ, &DLT645frame, &dataOut);
    if (ret == DLT645_ERR_SUB) {
        DLT645_DataLen += dataOut.len;
        out->data_len = hex2str(DLT645_Data, DLT645_DataLen, DLT645_DataLen * 2, out->data);
        in->subSEQ++;
        data_pack_cmd(in, out);
    } else if (ret == DLT645_ERR_OK) {
        data_buf_len = DLT645frame.data[9] - 4;
        memcpy(data_buf, DLT645frame.data + 14, data_buf_len);
        for (i = 0; i < data_buf_len; i++) {
            data_buf[i] -= 0x33;
        }

        if (decimal_len == -1) {
            flag = data_buf[1] & 0x01;
            sprintf(out->data, "%d", flag);
        } else {
            out_data_to_string(data_buf, data_buf_len, decimal_len, out->data);
        }
    }


    return ret;
}

void out_data_to_string(unsigned char *data_buf, int byte_len, unsigned char len, char *out_data) {
    char field_value[50] = {0};
    unsigned char decimal_len, is_sign_bit;
    int i;
    int res_byte_len = 0;

    decimal_len = len;
    is_sign_bit = FLAG;

    if (decimal_len == 0) {
        reverse_array(data_buf, byte_len);
        res_byte_len = hex2str(data_buf, byte_len, byte_len * 2, out_data);
    } else {
        for (i = 0; i < byte_len; i++) {
            data_buf[i] = bcd_to_decimal(data_buf[i]);
        }
        reverse_data_frame_to_string(data_buf, field_value, decimal_len, byte_len, is_sign_bit);
        res_byte_len = strlen(field_value);
        memcpy(out_data, field_value, res_byte_len);
        out_data[res_byte_len] = '\0';
    }
}


int bcd_to_decimal(unsigned char bcd) {
    return ((bcd >> 4) * 10 + (bcd & 0x0f));
}

unsigned char decimal_to_bcd(int dec) {
    return ((dec / 10 << 4) + dec % 10);
}

bool is_char_have_letter(const unsigned char data) {
    if ((data >> 4) > 0x09 || (data & 0x0f) > 0x09)
        return true;
    else
        return false;
}

bool is_have_letter(const unsigned char *data, const unsigned char byte_len) {
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

void frame_to_string_templet(char *str_in, char *data_str, unsigned char *frame_data, int *bit_len,
                             int byte_len, int decimal_len, unsigned char is_sign_bit) {
    unsigned char field_buf[20];           //字段字节内容（bcd)

    char field_value[50];       // 字段的具体内容(str)
    char field_str[100];          //字段加上字段内容


    memcpy(field_buf, frame_data + *bit_len, byte_len);
    *bit_len += byte_len;
    if (is_have_letter(field_buf, byte_len) == true) {
        sprintf(field_value, "");
    } else {
        reverse_data_frame_to_string(field_buf, field_value, decimal_len, byte_len, is_sign_bit);
    }
    sprintf(field_str, "\"%s\":\"%s\",", str_in, field_value);
    strcat(data_str, field_str);
}

void reverse_data_frame_to_string(unsigned char *data, char *out, unsigned char decimal,
                                  unsigned char byte_size, unsigned char is_sign_bit) {
    int i;
    int value;      //数组中所有数据的和
    int decimal_num;
    double decimal_flag;
    double res;   //最终结果
    bool flag = false;

    reverse_array(data, byte_size);

    if ((is_sign_bit == 1) && (data[0] >= 80)) {
        data[0] -= 80;
        flag = true;
    }
    value = count_data_sum(data, byte_size);

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

int count_data_sum(unsigned char *data, unsigned char byte_size) {
    int i;
    int sum = 0;
    for (i = 0; i < byte_size; i++) {
        sum *= 100;
        sum += data[i];
    }
    return sum;
}

int main() {
    int ret, i;
    char data[200];
//	char *frame = "6813000602152068910833333333353433335416";
    char *frame = "68000002081820689106333434353444F116";

    int frame_len = 40;
//	char frame[200];
    char error[132];
    int data_len = 0;
    int err_len = 0;

//	INPUT_STRU in = { "201502060013" };
    INPUT_STRU in = {"201808020000"};
    in.subSEQ = 0;


    in.di = "00000000";
    in.info = "006000";
    in.info_datalen = 6;
/*
    00000000   //组合有功总电能    6整2小
	02010100   //a相电压           3整1小
	02020100   //a相电流           3整3小
	02030000   //瞬时总有功功率    2整4小
	02800002   //电网频率          2整2小
	02060000   //总功率因数        1整3小
*/

    OUTPUT_STRU out = {data, frame, error, data_len, frame_len, err_len};


//ret = data_pack(&in, &out);
    //  printf("%s\n",out.frame);

    ret = data_result_cmd(&in, &out);
    printf("%s", out.data);

    system("pause");
    return 0;
}





//#include "dll.h"
//#include "../../common/param.h"
//#include "../../common/BCT645.h"
//
//#include <stdio.h>
//#include <string.h>
//
//
//#define DI1 0x00000000       //����й��ܵ���    6��2С
//#define DI2 0x02010100      //a���ѹ           3��1С
//#define DI3 0x02020100      //a�����           3��3С
//#define DI4 0x02030000      //˲ʱ���й�����    2��4С
//#define DI5 0x02800002      //����Ƶ��          2��2С
//#define DI6 0x02060000      //�ܹ�������        1��3С
//
//#define DECIMAL_LEN 2
//#define FLAG 0
//
//int data_pack_cmd(INPUT_STRU *in, OUTPUT_STRU *out) {
//    int err_code = 0;
//    unsigned char infoDataBuff[200], meterAddr[6], DI[4];
//    unsigned char *DLT645_Data, DLT645_Frame[300];
//    unsigned short DLT645_DataLen;
//    int ret, infoDataLen, paramLen;
//    unsigned long new_DI;
//
//    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
//    if (err_code != 0) {
//        return -1;
//    }
//
//    err_code = find_param(in, PARA_DI, DI, &paramLen);
//    if (err_code != 0) {
//        return -1;
//    }
//
//    if (DI[0] == 0x00 && DI[1] == 0x00 && DI[2] == 0x00 && DI[3] == 0x00) {
//        new_DI = DI1;
//    } else if (DI[0] == 0x02 && DI[1] == 0x01 && DI[2] == 0x01 && DI[3] == 0x00) {
//        new_DI = DI2;
//    } else if (DI[0] == 0x02 && DI[1] == 0x02 && DI[2] == 0x01 && DI[3] == 0x00) {
//        new_DI = DI3;
//    } else if (DI[0] == 0x02 && DI[1] == 0x03 && DI[2] == 0x00 && DI[3] == 0x00) {
//        new_DI = DI4;
//    } else if (DI[0] == 0x02 && DI[1] == 0x80 && DI[2] == 0x00 && DI[3] == 0x02) {
//        new_DI = DI5;
//    } else if (DI[0] == 0x02 && DI[1] == 0x06 && DI[2] == 0x00 && DI[3] == 0x00) {
//        new_DI = DI6;
//    } else {
//        return -1;
//    }
//
//    DLT645_Data = infoDataBuff; //+1;
//    DLT645_DataLen = infoDataLen = 0;
//
//    Data_t packData = {.data=DLT645_Data, .len=DLT645_DataLen};
//    Data_t DLT645_FrameOut = {.data=DLT645_Frame, .len=sizeof(DLT645_Frame)};
//    ret = BCT645_ReadDataFramePack(meterAddr, new_DI, in->subSEQ, &packData, &DLT645_FrameOut);
//    if (ret == DLT645_ERR_OK) {
//        out->frame_len = hex2str(DLT645_FrameOut.data, DLT645_FrameOut.len, DLT645_FrameOut.len * 2,
//                                 out->frame);
//    }
//    BCT645_PrintErrMsg(ret, out->error, 200, 0);
//    out->err_len = strlen(out->error);
//
//    return 0;
//}
//
//int data_result_cmd(INPUT_STRU *in, OUTPUT_STRU *out) {
//    int err_code = 0;
//    unsigned char meterAddr[6], DI[4], infoDataBuff[200];
//    unsigned char DLT645_frame[300], DLT645_Data[1000], data_buf[50];
//    unsigned short DLT645_frameLen, DLT645_DataLen = 0;
//    int ret, infoDataLen, paramLen;
//    unsigned long new_DI;
//    unsigned char decimal_len = 0;
//    int i, data_buf_len;
//
//    DLT645_frameLen = str2hex(out->frame, out->frame_len, sizeof(DLT645_frame), DLT645_frame);
//
//    memcpy(DI, DLT645_frame + 10, 4);
//    reverse_array(DI, 4);
//    for (i = 0; i < 4; i++) {
//        DI[i] -= 0x33;
//    }
//
//    if (DI[0] == 0x00 && DI[1] == 0x00 && DI[2] == 0x00 && DI[3] == 0x00) {
//        new_DI = DI1;
//        decimal_len = 2;
//    } else if (DI[0] == 0x02 && DI[1] == 0x01 && DI[2] == 0x01 && DI[3] == 0x00) {
//        new_DI = DI2;
//        decimal_len = 1;
//    } else if (DI[0] == 0x02 && DI[1] == 0x02 && DI[2] == 0x01 && DI[3] == 0x00) {
//        new_DI = DI3;
//        decimal_len = 3;
//    } else if (DI[0] == 0x02 && DI[1] == 0x03 && DI[2] == 0x00 && DI[3] == 0x00) {
//        new_DI = DI4;
//        decimal_len = 4;
//    } else if (DI[0] == 0x02 && DI[1] == 0x80 && DI[2] == 0x00 && DI[3] == 0x02) {
//        new_DI = DI5;
//        decimal_len = 2;
//    } else if (DI[0] == 0x02 && DI[1] == 0x06 && DI[2] == 0x00 && DI[3] == 0x00) {
//        new_DI = DI6;
//        decimal_len = 3;
//    } else {
//        return -1;
//    }
//    //   data_buf_len = BYTE_LEN;
//    data_buf_len = DLT645_frame[9] - 4;
//    memcpy(data_buf, DLT645_frame + 14, data_buf_len);
//    for (i = 0; i < data_buf_len; i++) {
//        data_buf[i] -= 0x33;
//        //       data_buf[i] = bcd_to_decimal(data_buf[i]);
//    }
//    //  reverse_array(data_buf,data_buf_len);
//
//    err_code = find_param(in, PARA_METER_ADDR, meterAddr, &paramLen);
//    if (err_code != 0) {
//        return -1;
//    }
//
//    Data_t DLT645frame, dataOut;
//    DLT645frame.data = DLT645_frame;
//    DLT645frame.len = DLT645_frameLen;
//    if (out->data_len) {
//        DLT645_DataLen = str2hex(out->data, out->data_len, sizeof(DLT645_Data), DLT645_Data);
//    }
//    dataOut.data = DLT645_Data + DLT645_DataLen;
//    dataOut.len = sizeof(DLT645_Data) - DLT645_DataLen;
//
//    ret = BCT645_ReadDataFrameParse(meterAddr, new_DI, in->subSEQ, &DLT645frame, &dataOut);
//    if (ret == DLT645_ERR_SUB) {
//        DLT645_DataLen += dataOut.len;
//        out->data_len = hex2str(DLT645_Data, DLT645_DataLen, DLT645_DataLen * 2, out->data);
//        in->subSEQ++;
//        data_pack_cmd(in, out);
//    } else if (ret == DLT645_ERR_OK) {
//        DLT645_DataLen += dataOut.len;
//        {
//            out_data_to_string(data_buf, data_buf_len, decimal_len, out->data);
//
//        }
//    }
//
//    return ret;
//}
//
//void out_data_to_string(unsigned char *data_buf, int byte_len, unsigned char len, char *out_data) {
//    char field_value[50] = {0};
//    unsigned char decimal_len, is_sign_bit;
//    int i = 0;
//    int res_byte_len = 0;
//
//    decimal_len = len;
//    is_sign_bit = FLAG;
//
//    if (decimal_len == 0) {
//        reverse_array(data_buf, byte_len);
//        res_byte_len = hex2str(data_buf, byte_len, byte_len * 2, out_data);
//    } else {
//        for (i; i < byte_len; i++) {
//            data_buf[i] = bcd_to_decimal(data_buf[i]);
//        }
//        reverse_data_frame_to_string(data_buf, field_value, decimal_len, byte_len, is_sign_bit);
//        res_byte_len = strlen(field_value);
//        memcpy(out_data, field_value, res_byte_len);
//        out_data[res_byte_len] = '\0';
//    }
//}
//
//
//int bcd_to_decimal(unsigned char bcd) {
//    return ((bcd >> 4) * 10 + (bcd & 0x0f));
//}
//
//unsigned char decimal_to_bcd(int dec) {
//    return ((dec / 10 << 4) + dec % 10);
//}
//
//bool is_char_have_letter(const unsigned char data) {
//    if ((data >> 4) > 0x09 || (data & 0x0f) > 0x09)
//        return true;
//    else
//        return false;
//}
//
//bool is_have_letter(const unsigned char *data, const unsigned char byte_len) {
//    int i;
//    int count = 0;
//    unsigned char data_tmp[20];
//    memcpy(data_tmp, data, byte_len);
//    for (i = 0; i < byte_len; i++) {
//        if ((data_tmp[i] >> 4) > 0x09) {
//            return true;
//        }
//    }
//
//    return false;
//}
//
//void frame_to_string_templet(char *str_in, char *data_str, unsigned char *frame_data, int *bit_len,
//                             int byte_len, int decimal_len, unsigned char is_sign_bit) {
//    unsigned char field_buf[20];           //�ֶ��ֽ����ݣ�bcd)
//
//    char field_value[50];       // �ֶεľ�������(str)
//    char field_str[100];          //�ֶμ����ֶ�����
//
//
//    memcpy(field_buf, frame_data + *bit_len, byte_len);
//    *bit_len += byte_len;
//    if (is_have_letter(field_buf, byte_len) == true) {
//        sprintf(field_value, "");
//    } else {
//        reverse_data_frame_to_string(field_buf, field_value, decimal_len, byte_len, is_sign_bit);
//    }
//    sprintf(field_str, "\"%s\":\"%s\",", str_in, field_value);
//    strcat(data_str, field_str);
//}
//
//void reverse_data_frame_to_string(unsigned char *data, char *out, unsigned char decimal,
//                                  unsigned char byte_size, unsigned char is_sign_bit) {
//    int i;
//    int value;      //�������������ݵĺ�
//    int decimal_num;
//    double decimal_flag;
//    double res;   //���ս��
//    bool flag = false;
//
//    reverse_array(data, byte_size);
//
//    if ((is_sign_bit == 1) && (data[0] >= 80)) {
//        data[0] -= 80;
//        flag = true;
//    }
//    value = count_data_sum(data, byte_size);
//
//    if (decimal == 0) {
//        res = value;
//    } else {
//        decimal_num = 1;
//        double decimal_flag = 1;
//        for (i = 0; i < decimal; i++) {
//            decimal_num *= 10;
//            decimal_flag *= 0.1;
//        }
//        res = value / decimal_num + value % decimal_num * decimal_flag;
//    }
//
//    if ((is_sign_bit == 1) && flag) {
//        switch (decimal) {
//            case 0:
//                sprintf(out, "-%.0f", res);
//                break;
//            case 1:
//                sprintf(out, "-%.1f", res);
//                break;
//            case 2:
//                sprintf(out, "-%.2f", res);
//                break;
//            case 3:
//                sprintf(out, "-%.3f", res);
//                break;
//            case 4:
//                sprintf(out, "-%.4f", res);
//                break;
//            case 5:
//                sprintf(out, "-%.5f", res);
//                break;
//            case 6:
//                sprintf(out, "-%.6f", res);
//                break;
//            case 7:
//                sprintf(out, "-%.7f", res);
//                break;
//            case 8:
//                sprintf(out, "-%.8f", res);
//                break;
//            default:
//                sprintf(out, "-%.0f", res);
//                break;
//        }
//    } else {
//        switch (decimal) {
//            case 0:
//                sprintf(out, "%.0f", res);
//                break;
//            case 1:
//                sprintf(out, "%.1f", res);
//                break;
//            case 2:
//                sprintf(out, "%.2f", res);
//                break;
//            case 3:
//                sprintf(out, "%.3f", res);
//                break;
//            case 4:
//                sprintf(out, "%.4f", res);
//                break;
//            case 5:
//                sprintf(out, "%.5f", res);
//                break;
//            case 6:
//                sprintf(out, "%.6f", res);
//                break;
//            case 7:
//                sprintf(out, "%.7f", res);
//                break;
//            case 8:
//                sprintf(out, "%.8f", res);
//                break;
//            default:
//                sprintf(out, "%.0f", res);
//                break;
//        }
//    }
//
//}
//
//int count_data_sum(unsigned char *data, unsigned char byte_size) {
//    int i;
//    int sum = 0;
//    for (i = 0; i < byte_size; i++) {
//        sum *= 100;
//        sum += data[i];
//    }
//    return sum;
//}
//
//int main() {
//    int ret, i;
//    char data[200];
//    char *frame = "6813000602152068910833333333353433335416";
//    int frame_len = 40;
////	char frame[200];
//    char error[132];
//    int data_len = 0;
//    int err_len = 0;
//
//    INPUT_STRU in = {"201502060013"};
//    in.subSEQ = 0;
//
//
//    in.di = "00000000";
//    in.info = "006000";
//    in.info_datalen = 6;
///*
//    00000000   //����й��ܵ���    6��2С
//	02010100   //a���ѹ           3��1С
//	02020100   //a�����           3��3С
//	02030000   //˲ʱ���й�����    2��4С
//	02800002   //����Ƶ��          2��2С
//	02060000   //�ܹ�������        1��3С
//*/
//
//    OUTPUT_STRU out = {data, frame, error, data_len, frame_len, err_len};
//
//
////	ret = data_pack_cmd(&in, &out);
////    printf("%s\n",out.frame);
//
//    ret = data_result_cmd(&in, &out);
//    printf("%s", out.data);
//
//    system("pause");
//    return 0;
//}
