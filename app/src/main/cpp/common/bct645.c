#include "des.h"
#include "BCT645.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

#define ArraySize(X)    ((int)(sizeof(X)/sizeof(X[0])))
#define get_bit(data, i) ((data>>i)&0x01)
#define IS_BCT645_RSP(frame)    get_bit((frame)[8],7)
#define IS_ABNORMAL_FLAG(frame) get_bit((frame)[8],6)
#define IS_SUBFRAME_FLAG(frame)    get_bit((frame)[8],5)
#define BCT645_FC(frame)  ((frame)[8]&0x1f)

static unsigned char checksum(const unsigned char *data, unsigned short len) {
    unsigned char sum = 0;
    while (len--)
        sum += data[len];
    return sum;
}

static void reverse_array(unsigned char *buff, unsigned short len) {
    unsigned char tmp;
    int i;
    for (i = 0; i < len / 2; i++) {
        tmp = buff[i];
        buff[i] = buff[len - i - 1];
        buff[len - i - 1] = tmp;
    }
}

void set_err_code(char *err_code, Data_t frame) {
    if (0 == frame.len) {
        sprintf(err_code, "00");
    } else if (1 == frame.len) {
        sprintf(err_code, "%02x", frame.data[0]);
    } else if (2 == frame.len) {
        sprintf(err_code, "%02x%02x", frame.data[1], frame.data[0]);
    } else if (3 == frame.len) {
        sprintf(err_code, "%02x%02x%02x", frame.data[2], frame.data[1], frame.data[0]);
    } else if (4 == frame.len) {
        sprintf(err_code, "%02x%02x%02x%02x", frame.data[3], frame.data[2], frame.data[1],
                frame.data[0]);
    } else
        sprintf(err_code, "%02x%02x%02x%02x%02x", frame.data[4], frame.data[3], frame.data[2],
                frame.data[1], frame.data[0]);
}

const char *BCT645_PrintErrMsg(int errCode, char *errStr, int size, char *errWord) {
    static const char *const errMsg[] = {
            "BCT645:success",
            "BCT645:respond with subsequence flag",
            "BCT645:exception response frame",
            "BCT645:frame format error",
            "BCT645:meter addr mismatch",
            "BCT645:frame with startup format",
            "BCT645:SEQ mismatch",
            "BCT645:CW mismatch",
            "BCT645:DI mismatch",
            "BCT645:data length overflow",
            "BCT645:data check error",
    };
    const char *zErr = "BCT645:unknown error";

    if (errCode < ArraySize(errMsg) && errMsg[errCode] != NULL)
        zErr = errMsg[errCode];
    if (errStr) {
        if (errCode != DLT645_ERR_FAIL)
            snprintf(errStr, size, "%s", zErr);
        else
            snprintf(errStr, size, "%s, error[%s]", zErr, errWord);
    }
    return zErr;
}

int BCT645_Format(const unsigned char *data, unsigned short datasize, unsigned short *offset) {
    unsigned short i;
    for (i = 0; datasize - i > 11; i++) {
        //	printf("data_0:%02x;  data_7:%02x;  datasize:%d  -- %02x", data[i], data[i + 7], datasize, i + data[i + 9] + 12);
        //	printf("cs: %02x -- %02x;   jiesu: %02x", data[i + data[i + 9] + 10], checksum(data + i, data[i + 9] + 10),data[i + data[i + 9] + 11]);
        if (data[i] == 0x68
            && data[i + 7] == 0x68
            && datasize >= (i + data[i + 9] + 12)
            && data[i + data[i + 9] + 11] == 0x16
            && data[i + data[i + 9] + 10] == checksum(data + i, data[i + 9] + 10)) {
            *offset = i;
            return data[i + 9] + 12;
        }
    }
    return -1;
}

int BCT645_FramePack(BCT645_info *info, Data_t *frameOut) {
    int i;
    if (info->data.len + 12 > frameOut->len)
        return DLT645_ERR_LEN;
    frameOut->data[0] = frameOut->data[7] = 0x68;
    if (info->fc == BROADCAST) {
        memset(frameOut->data + 1, 0x99, 6);
    } else if (info->fc == READ_ADDR) {
        memset(frameOut->data + 1, 0xAA, 6);
    } else {
        memcpy(frameOut->data + 1, info->addr, 6);
        reverse_array(frameOut->data + 1, 6);
    }

    frameOut->data[8] = info->fc;
    frameOut->data[9] = info->data.len;

    for (i = 0; i < info->data.len; i++)
        frameOut->data[10 + i] = info->data.data[i] + 0x33;
    frameOut->data[10 + info->data.len] = checksum(frameOut->data, 10 + info->data.len);
    frameOut->data[11 + info->data.len] = 0x16;
    frameOut->len = 12 + info->data.len;
    return DLT645_ERR_OK;
}

int BCT645_ApplicationData(unsigned char *addr, Data_t *frame, Data_t *dataOut) {
    int len, i;
    unsigned short offset;

    if ((len = BCT645_Format(frame->data, frame->len, &offset)) < 0) {
        return DLT645_ERR_FORMAT;
    }
    frame->data += offset;

    if (addr[0] != frame->data[6] || addr[1] != frame->data[5] || addr[2] != frame->data[4]
        || addr[3] != frame->data[3] || addr[4] != frame->data[2] || addr[5] != frame->data[1]) {
        return DLT645_ERR_ADDR;
    }
    //	if(!IS_BCT645_RSP(frame->data))
    //	{
    //		return DLT645_ERR_RSP;
    //	}

    if (IS_ABNORMAL_FLAG(frame->data))//异常应答
    {
        //	dataOut->data[0]=frame->data[10]-0x33;
        //	dataOut->len=1;

        dataOut->len = frame->data[9];
        for (i = 0; i < dataOut->len; i++) {
            dataOut->data[i] = frame->data[10 + i] - 0x33;
        }

        return DLT645_ERR_FAIL;
    }

    len = frame->data[9];
    if (len > dataOut->len) {
        return DLT645_ERR_LEN;
    }
    for (i = 0; i < len; i++) {
        dataOut->data[i] = frame->data[10 + i] - 0x33;
    }
    dataOut->len = len;

    if (IS_SUBFRAME_FLAG(frame->data))//后续帧标志
    {
        return DLT645_ERR_SUB;
    }
    return DLT645_ERR_OK;
}

int BCT645_ReadDataFramePack(unsigned char *addr, unsigned long DI, unsigned char SEQ,
                             Data_t *packData, Data_t *frameOut) {
    unsigned char applicationData[200];
    int applicationLen = 4, i;
    BCT645_info info;

    info.fc = READ_DATA;

    applicationData[0] = (unsigned char) DI;
    applicationData[1] = (unsigned char) (DI >> 8);
    applicationData[2] = (unsigned char) (DI >> 16);
    applicationData[3] = (unsigned char) (DI >> 24);

    if (packData->len > sizeof(applicationData) - 5) {
        return DLT645_ERR_LEN;
    }

    if (SEQ) {
        applicationLen++;
        applicationData[applicationLen + packData->len - 1] = SEQ;
        info.fc = READ_SUBDATA;
    }
    for (i = 0; i < packData->len; i++) {
        applicationData[4 + i] = packData->data[i];
        applicationLen++;
    }
    reverse_array(applicationData + 4, packData->len);

    info.addr = addr;
    info.data.data = applicationData;
    info.data.len = applicationLen;
    return BCT645_FramePack(&info, frameOut);
}

int BCT645_ReadDataFrameParse(unsigned char *addr, unsigned long DI, unsigned char SEQ,
                              Data_t *frame, Data_t *dataOut) {
    int ret;

    ret = BCT645_ApplicationData(addr, frame, dataOut);
    if (ret != DLT645_ERR_OK && ret != DLT645_ERR_SUB)
        return ret;
    unsigned long frameDI = 0;
    unsigned char tmp;
    tmp = frame->data[10] - 0x33;
    frameDI += (unsigned long) tmp;
    tmp = frame->data[11] - 0x33;
    frameDI += (unsigned long) tmp << 8;
    tmp = frame->data[12] - 0x33;
    frameDI += (unsigned long) tmp << 16;
    tmp = frame->data[13] - 0x33;
    frameDI += (unsigned long) tmp << 24;
    if (BCT645_FC(frame->data) != READ_DATA && BCT645_FC(frame->data) != READ_SUBDATA) {
        return DLT645_ERR_CW;
    }
    if (DI != frameDI) {
        return DLT645_ERR_DI;
    }

    if (ret == DLT645_ERR_OK || ret == DLT645_ERR_SUB) {
        if (BCT645_FC(frame->data) == READ_SUBDATA) {
            if (SEQ != dataOut->data[dataOut->len - 1]) {
                return DLT645_ERR_SEQ;
            }
            memcpy(dataOut->data, dataOut->data + 4, dataOut->len - 5);//-seq
            dataOut->len = dataOut->len - 5;
        } else {
            memcpy(dataOut->data, dataOut->data + 4, dataOut->len - 4);
            dataOut->len = dataOut->len - 4;
        }
    } else if (ret == DLT645_ERR_FAIL) {
        dataOut->len = 1;
    }
    return ret;
}

int BCT645_WriteDataPack(unsigned char *addr, unsigned long DI,
                         unsigned char *psw, unsigned char *OPcode,
                         Data_t *packData, Data_t *frameOut) {
    unsigned char applicationData[200];

    int i, applicationLen = 12;
    applicationData[0] = (unsigned char) DI;
    applicationData[1] = (unsigned char) (DI >> 8);
    applicationData[2] = (unsigned char) (DI >> 16);
    applicationData[3] = (unsigned char) (DI >> 24);
    if (psw != NULL) {
        memcpy(applicationData + 4, psw, 4);
        reverse_array(applicationData + 4, 4);
    } else {
        memset(applicationData + 4, 0, 4);
    }

    if (OPcode != NULL) {
        memcpy(applicationData + 8, OPcode, 4);
        reverse_array(applicationData + 8, 4);
    } else {
        applicationData[8] = 0x12;
        applicationData[9] = 0x34;
        applicationData[10] = 0x56;
        applicationData[11] = 0x78;
        reverse_array(applicationData + 8, 4);
    }

    if (packData->len > sizeof(applicationData) - 12) {
        return DLT645_ERR_LEN;
    }
    for (i = 0; i < packData->len; i++) {
        applicationData[12 + i] = packData->data[i];
        applicationLen++;
    }
    reverse_array(applicationData + 12, applicationLen - 12);

    BCT645_info info;
    info.addr = addr;
    info.fc = WRITE_DATA;
    info.data.data = applicationData;
    info.data.len = applicationLen;
    return BCT645_FramePack(&info, frameOut);
}

int BCT645_WriteDataPackSimp(unsigned char *addr, unsigned long DI,
                             Data_t *packData, Data_t *frameOut) {
    return BCT645_WriteDataPack(addr, DI, NULL, NULL, packData, frameOut);
}

int BCT645_WriteDataFrameParse(unsigned char *addr, Data_t *frame, Data_t *dataOut) {
    int ret;

    ret = BCT645_ApplicationData(addr, frame, dataOut);
    if (ret != DLT645_ERR_OK)
        return ret;
    if (BCT645_FC(frame->data) != WRITE_DATA) {
        return DLT645_ERR_CW;
    }

    return DLT645_ERR_OK;
}

int BCT645_IRSendFramePack(unsigned char *addr, unsigned long DI,
                           Data_t *packData, Data_t *frameout) {
    unsigned char applicationData[220];

    int i, applicationLen = 4;
    applicationData[0] = (unsigned char) DI;
    applicationData[1] = (unsigned char) (DI >> 8);
    applicationData[2] = (unsigned char) (DI >> 16);
    applicationData[3] = (unsigned char) (DI >> 24);

    if (packData->len > sizeof(applicationData) - 4) {
        return DLT645_ERR_LEN;
    }
    for (i = 0; i < packData->len; i++) {
        applicationData[4 + i] = packData->data[i];
        applicationLen++;
    }

    BCT645_info info;
    info.addr = addr;
    info.fc = IR_SEND;
    info.data.data = applicationData;
    info.data.len = applicationLen;

    return BCT645_FramePack(&info, frameout);
}

int BCT645_IRSendFrameParse(unsigned char *addr, unsigned long DI, unsigned char SEQ,
                            Data_t *frame, Data_t *dataOut) {
    int ret;

    ret = BCT645_ApplicationData(addr, frame, dataOut);
    if (ret != DLT645_ERR_OK)
        return ret;
    unsigned long frameDI = 0;
    unsigned char tmp;
    tmp = frame->data[10] - 0x33;
    frameDI += (unsigned long) tmp;
    tmp = frame->data[11] - 0x33;
    frameDI += (unsigned long) tmp << 8;
    tmp = frame->data[12] - 0x33;
    frameDI += (unsigned long) tmp << 16;
    tmp = frame->data[13] - 0x33;
    frameDI += (unsigned long) tmp << 24;
    if (BCT645_FC(frame->data) != IR_SEND) {
        return DLT645_ERR_CW;
    }
    if (DI != frameDI) {
        return DLT645_ERR_DI;
    }

    return ret;
}


int
BCT645_DataFrameParse(unsigned char *addr, unsigned long DI, unsigned char SEQ, unsigned char CW,
                      int is_judge_identifier, int is_data_out,
                      Data_t *frame, Data_t *dataOut) {
    int ret;


    ret = BCT645_ApplicationData(addr, frame, dataOut);

    if (ret != DLT645_ERR_OK && ret != DLT645_ERR_SUB) {
        return ret;
    }

    if (BCT645_FC(frame->data) != CW) {
        return DLT645_ERR_CW;
    }

    if (is_judge_identifier != 0) {
        unsigned long frameDI = 0;
        unsigned char tmp;
        tmp = frame->data[10] - 0x33;
        frameDI += (unsigned long) tmp;
        tmp = frame->data[11] - 0x33;
        frameDI += (unsigned long) tmp << 8;
        tmp = frame->data[12] - 0x33;
        frameDI += (unsigned long) tmp << 16;
        tmp = frame->data[13] - 0x33;
        frameDI += (unsigned long) tmp << 24;
        if (DI != frameDI) {
            return DLT645_ERR_DI;
        }
    }

    if (is_data_out != 0) {
        if (ret == DLT645_ERR_OK || ret == DLT645_ERR_SUB) {
            if (BCT645_FC(frame->data) == READ_SUBDATA) {
                if (SEQ != dataOut->data[dataOut->len - 1]) {
                    return DLT645_ERR_SEQ;
                }
                memcpy(dataOut->data, dataOut->data + 4, dataOut->len - 5);//-seq
                dataOut->len = dataOut->len - 5;
            } else {
                memcpy(dataOut->data, dataOut->data + 4, dataOut->len - 4);
                dataOut->len = dataOut->len - 4;
            }
        } else if (ret == DLT645_ERR_FAIL) {
            dataOut->len = 1;
        }
    }

    return ret;
}


int BCT645_AllDataPack(unsigned char *addr, BCT645_FC_T FC, unsigned char *DI, int DI_len,
                       unsigned char SEQ,
                       unsigned char *psw, unsigned char *OPcode, Data_t *packData,
                       Data_t *frameOut) {
    unsigned char applicationData[200];
    int i = 0, applicationLen = 0;

    if (DI != NULL) {
        memcpy(applicationData, DI, DI_len);
        reverse_array(applicationData, DI_len);
        applicationLen += DI_len;
    }

    if (psw != NULL) {
        memcpy(applicationData + applicationLen, psw, 4);
        reverse_array(applicationData + applicationLen, 4);
        applicationLen += 4;
    }

    if (OPcode != NULL) {
        memcpy(applicationData + applicationLen, OPcode, 4);
        reverse_array(applicationData + applicationLen, 4);
        applicationLen += 4;
    }

    if (packData->len > sizeof(applicationData) - 12) {
        return DLT645_ERR_LEN;
    }
    if (packData->len > 0) {
        for (i = 0; i < packData->len; i++) {
            applicationData[applicationLen + i] = packData->data[i];
            //		    applicationLen++;
        }
        //   reverse_array(applicationData + applicationLen, packData->len);
    }

    BCT645_info info;
    info.addr = addr;
    info.fc = FC;
    if (SEQ) {
        applicationLen++;
        applicationData[applicationLen + packData->len - 1] = SEQ;
        info.fc = READ_SUBDATA;
    }
    info.data.data = applicationData;
    info.data.len = applicationLen + packData->len;

    return BCT645_FramePack(&info, frameOut);
}


int BCT645_AllDataParse(unsigned char *addr, BCT645_FC_T FC, unsigned char *DI, int DI_len,
                        unsigned char SEQ, Data_t *frame, Data_t *dataOut) {
    int i, ret;


    ret = BCT645_ApplicationData(addr, frame, dataOut);

    if (ret != DLT645_ERR_OK && ret != DLT645_ERR_SUB) {
        return ret;
    }

    if (BCT645_FC(frame->data) != FC) {
        return DLT645_ERR_CW;
    }

    if (DI != NULL) {
        for (i = 0; i < DI_len; i++) {
            if ((unsigned char)(frame->data[13 - (4 - DI_len) - i] - 0x33) != DI[i]) {
                return DLT645_ERR_DI;
            }
        }
    }
    return ret;
}


//水表生成和解析
int
Watermeter_ReadFramePack(unsigned char T, unsigned char *addr, unsigned char *DI, unsigned char ser,
                         Data_t *frameOut) {
    frameOut->data[0] = 0x68;
    frameOut->data[1] = T;
    memcpy(frameOut->data + 2, addr, 7);
    reverse_array(frameOut->data + 2, 7);

    frameOut->data[9] = 0x01;
    frameOut->data[10] = 0x03;

    memcpy(frameOut->data + 11, DI, 2);
    reverse_array(frameOut->data + 11, 2);

    frameOut->data[13] = ser;

    frameOut->data[14] = checksum(frameOut->data, 14);
    frameOut->data[15] = 0x16;
    frameOut->len = 16;
    return DLT645_ERR_OK;
}


int
Watermeter_ApplicationData(unsigned char t, unsigned char *addr, unsigned char *DI, Data_t *frame,
                           Data_t *dataOut) {
    int len;
    unsigned short offset;

    if ((len = Watermeter_Format(frame->data, frame->len, &offset)) < 0) {
        return DLT645_ERR_FORMAT;
    }
    frame->data += offset;

    if (addr[0] != frame->data[8] || addr[1] != frame->data[7] || addr[2] != frame->data[6]
        || addr[3] != frame->data[5] || addr[4] != frame->data[4] || addr[5] != frame->data[3] ||
        addr[6] != frame->data[2]) {
        return DLT645_ERR_ADDR;
    }

    if (frame->data[9] == 0xc1)//异常应答
    {
        dataOut->data[0] = frame->data[10];
        dataOut->data[1] = frame->data[11];
        dataOut->data[2] = frame->data[12];
        dataOut->len = 3;
        return DLT645_ERR_FAIL;
    }

    len = frame->data[10];
    if (len > dataOut->len) {
        return DLT645_ERR_LEN;
    }

    dataOut->len = len;

    if (frame->data[1] != t) {
        return DLT645_ERR_FORMAT;
    }

    if ((frame->data[9] & 0x1f) != 0x01) {
        return DLT645_ERR_CW;
    }

    if (frame->data[11] != DI[1] || frame->data[12] != DI[0]) {
        return DLT645_ERR_DI;
    }
    return DLT645_ERR_OK;
}

int Watermeter_Format(const unsigned char *data, unsigned short datasize, unsigned short *offset) {
    unsigned short i;
    for (i = 0; datasize - i > 11; i++) {
        if (data[i] == 0x68
            && datasize >= (i + data[i + 10] + 12)
            && data[i + data[i + 10] + 12] == 0x16
            && data[i + data[i + 10] + 11] == checksum(data + i, data[i + 10] + 11)) {
            *offset = i;
            return data[i + 10] + 12;
        }
    }
    return -1;
}

int BeiDian_Watermeter_ReadFramePack(unsigned char T, unsigned char *addr, unsigned char pfc,
                                     unsigned char *DI, unsigned char *time_buf, Data_t *frameOut) {
    frameOut->data[0] = 0x68;
    frameOut->data[1] = T;
    memcpy(frameOut->data + 2, addr, 5);
    reverse_array(frameOut->data + 2, 5);

    frameOut->data[7] = 0x02;
    frameOut->data[8] = 0x01;
    frameOut->data[9] = pfc;
    frameOut->data[10] = 0x0d;

    memcpy(frameOut->data + 11, DI, 4);
    reverse_array(frameOut->data + 11, 4);

    memcpy(frameOut->data + 15, DI, 7);

    memset(frameOut->data + 22, 0, 6);

    frameOut->data[28] = checksum(frameOut->data, 28);
    frameOut->data[29] = 0x16;
    frameOut->len = 30;
    return DLT645_ERR_OK;
}

int BeiDian_Watermeter_ApplicationData(unsigned char t, unsigned char *addr, unsigned char pfc,
                                       unsigned char *DI, Data_t *frame, Data_t *dataOut) {
    int len;
    unsigned short offset;

    if ((len = BeiDian_Watermeter_Format(frame->data, frame->len, &offset)) < 0) {
        return DLT645_ERR_FORMAT;
    }
    frame->data += offset;

    if (addr[0] != frame->data[6] || addr[1] != frame->data[5] || addr[2] != frame->data[4]
        || addr[3] != frame->data[3] || addr[4] != frame->data[2]) {
        return DLT645_ERR_ADDR;
    }

    if (frame->data[8] != 0x81)//异常应答
    {
        return DLT645_ERR_FAIL;
    }
    if (frame->data[9] != pfc) {
        return DLT645_ERR_FORMAT;
    }

    if (frame->data[11] != DI[3] || frame->data[12] != DI[2] || frame->data[13] != DI[1] ||
        frame->data[14] != DI[0]) {
        return DLT645_ERR_DI;
    }
    return DLT645_ERR_OK;
}

int BeiDian_Watermeter_Format(const unsigned char *data, unsigned short datasize,
                              unsigned short *offset) {
    unsigned short i;
    for (i = 0; datasize - i > 11; i++) {
        if (data[i] == 0x68
            && data[i + 8] == 0x81
            && data[i + 46] == 0x16
            && data[i + 45] == checksum(data + i, 45)) {
            *offset = i;
            return 47;
        }
    }
    return -1;
}

int BCT645_Splitairconditioning_DataPack(unsigned char *addr, unsigned char sign, BCT645_FC_T FC,
                                         Data_t *before_des_info, unsigned char *DESkey,
                                         Data_t *frameOut) {
    unsigned char subKey[192], tmp_len;
    unsigned char packBuff[200], packLen = 0;
    unsigned char packInfoBuff[200], packInfoLen = 0;

    packInfoBuff[0] = sign;
    packInfoLen += 1;

    if (before_des_info->len > sizeof(packBuff))
        return DLT645_ERR_LEN;

    memcpy(packBuff, before_des_info->data, before_des_info->len);
    packLen += before_des_info->len;

    memcpy(packInfoBuff + packInfoLen, packBuff, packLen);
    packInfoLen += packLen;

    packInfoBuff[packInfoLen] = checksum(packInfoBuff, packInfoLen);
    packInfoLen++;

    tmp_len = 8 - packInfoLen % 8;
    if (tmp_len) {
        packInfoBuff[packInfoLen] = 0x80;
        memset(packInfoBuff + packInfoLen + 1, 0, tmp_len - 1);
        packInfoLen += tmp_len;
    }

    SetSubKey(subKey, DESkey);
    Des_Go(packInfoBuff, packInfoBuff, packInfoLen, subKey, ENCRYPT);

    BCT645_info info;
    info.addr = addr;
    info.fc = FC;
    info.data.data = packInfoBuff;
    info.data.len = packInfoLen;
    return BCT645_FramePack(&info, frameOut);
}

void getDI(unsigned char *DI, unsigned long DI_tmp) {
    DI[0] = (unsigned char) (DI_tmp >> 24);
    DI[1] = (unsigned char) (DI_tmp >> 16);
    DI[2] = (unsigned char) (DI_tmp >> 8);
    DI[3] = (unsigned char) (DI_tmp);
}