#ifndef BCT645_SAC_H
#define BCT645_SAC_H

#define FRMAESIZE_MIN    12
#define FRMAESIZE_MAX    267

#define DLT645_ERR_OK            0   /* 成功*/
#define DLT645_ERR_SUB            1    /* 有后续帧*/
#define DLT645_ERR_FAIL            2    /* 异常应答帧*/
#define DLT645_ERR_FORMAT        3    /* 帧格式错误*/
#define DLT645_ERR_ADDR            4    /* 表地址有误*/
#define DLT645_ERR_RSP            5    /* 非电表应答帧*/
#define DLT645_ERR_SEQ            6    /* 后续帧SEQ 不匹配*/
#define DLT645_ERR_CW            7    /* 控制字错误*/
#define DLT645_ERR_DI            8    /* DI 不匹配*/
#define DLT645_ERR_LEN            9    /* 缓存溢出*/
#define DLT645_ERR_DATA            10    /* 数据域长度异常*/

typedef enum {
    READ_DATA_97 = 0x01,
    CUSTOM_03H = 0x03,//认证、充值、改主密钥
    CUSTOM_04H = 0x04,//读主密钥、读购电次数
    BROADCAST = 0x08,
    IR_SEND = 0x0f,
    READ_DATA = 0x11,
    READ_SUBDATA = 0x12,
    READ_ADDR = 0x13,
    WRITE_DATA = 0x14,
    WRITE_ADDR = 0x15,
    FREEZE_CMD = 0x16,
    MOD_BAUD = 0x17,
    MOD_PSW = 0x18,
    DEMAND_CLR = 0x19,
    ALL_CLR = 0x1a,
    EVENT_CLR = 0x1b,
    CUSTOM_1CH = 0x1c,//保电、解除保电、跳闸、解除跳闸（合闸）、打开编程按键、清除金额
    CZZLFH = 0x83, //充值指令返回控制码
    WRITE_KZKT = 0x0f,        //发送红外编码控制空调
    WRITE_DK = 0x34,         //设置按键的灯控编码及控制方式(设置)
} BCT645_FC_T;

typedef enum {
    DI_ERROR = 0x05,
    AMOUNT_ILLEGAL = 0x06,
    PURCHASE_ELEC_ERR = 0x07,
    ADDR_ERR = 0x08,
    CKS_ERR = 0x09,
    RAND_NUM_ERR = 0x10,
    TIME_ABNORMAL = 0x12,
    MONEY_OVER = 0x13,
    POWER_PROTECTION_STATE = 0x14,
    TRIP_STATE = 0x16,
    REBATE_ERR = 0x17,
    TEMP_OVER = 0x21,
    E2_SAVE_ERR = 0x22,
} BCT645_EC;

/*for CW 1c*/
#define BD        0x3A//保电
#define JCBD    0x3B//解除保电
#define TZ        0x1A//跳闸
#define HZ        0x4B//合闸
#define BCAJ    0x4A//打开编程按键
#define FLDJ    0x6A//修改费率电价

/*for CW 03*/
#define RZ        0x1A//认证
#define CZ        0x2A//充值
#define GMY        0x3A//改密钥

/*for CW 04*/
#define DZMY    0x1A//读主密钥
#define DGDCS    0x2A//读购电次数

#ifndef _DATA_DEFINED
#define _DATA_DEFINED
typedef struct {
    unsigned char *data;
    unsigned short len;
} Data_t;
#endif

typedef struct {
    unsigned char *addr;
    BCT645_FC_T fc;
    Data_t data;
} BCT645_info;

#ifdef __cplusplus
extern "C" {
#endif

//设置错误代码
void set_err_code(char *err_code, Data_t frame);

//解析错误信息
const char *BCT645_PrintErrMsg(int errCode, char *errStr, int size, char *errWord);

//帧格式解析
int BCT645_Format(const unsigned char *data, unsigned short datasize, unsigned short *offset);

//协议封装
int BCT645_FramePack(BCT645_info *info, Data_t *frameOut);

//取出应用层数据
int BCT645_ApplicationData(unsigned char *addr, Data_t *frame, Data_t *dataOut);

//读数据的帧封装
int BCT645_ReadDataFramePack(unsigned char *addr, unsigned long DI, unsigned char SEQ,
                             Data_t *packData, Data_t *frameOut);

//解析读数据帧的内容
int BCT645_ReadDataFrameParse(unsigned char *addr, unsigned long DI, unsigned char SEQ,
                              Data_t *frame, Data_t *dataOut);

//写数据帧封装
int BCT645_WriteDataPack(unsigned char *addr, unsigned long DI,
                         unsigned char *psw, unsigned char *OPcode,
                         Data_t *packData, Data_t *frameOut);

//默认密码与操作者代码的写数据
int BCT645_WriteDataPackSimp(unsigned char *addr, unsigned long DI,
                             Data_t *packData, Data_t *frameOut);

//写数据解析
int BCT645_WriteDataFrameParse(unsigned char *addr, Data_t *frame, Data_t *dataOut);

//红外发射封装
int BCT645_IRSendFramePack(unsigned char *addr, unsigned long DI,
                           Data_t *packData, Data_t *frameout);

//红外发射解析
int BCT645_IRSendFrameParse(unsigned char *addr, unsigned long DI, unsigned char SEQ, Data_t *frame,
                            Data_t *dataOut);


int BCT645_DataFramePack(unsigned char *addr, unsigned long DI, unsigned char SEQ, BCT645_FC_T FC,
                         unsigned char *psw, unsigned char *OPcode,
                         Data_t *packData, Data_t *frameOut);

int
BCT645_DataFrameParse(unsigned char *addr, unsigned long DI, unsigned char SEQ, unsigned char CW,
                      int is_judge_identifier, int is_data_out,
                      Data_t *frame, Data_t *dataOut);


//数据生成
int BCT645_AllDataPack(unsigned char *addr, BCT645_FC_T FC, unsigned char *DI, int DI_len,
                       unsigned char SEQ,
                       unsigned char *psw, unsigned char *OPcode, Data_t *packData,
                       Data_t *frameOut);

//数据解析
int BCT645_AllDataParse(unsigned char *addr, BCT645_FC_T FC, unsigned char *DI, int DI_len,
                        unsigned char SEQ,
                        Data_t *frame, Data_t *dataOut);

//水表生成
int
Watermeter_ReadFramePack(unsigned char T, unsigned char *addr, unsigned char *DI, unsigned char ser,
                         Data_t *frameOut);

//水表解析
int
Watermeter_ApplicationData(unsigned char t, unsigned char *addr, unsigned char *DI, Data_t *frame,
                           Data_t *dataOut);

int Watermeter_Format(const unsigned char *data, unsigned short datasize, unsigned short *offset);

//北电水表协议 生成指令
int BeiDian_Watermeter_ReadFramePack(unsigned char T, unsigned char *addr, unsigned char pfc,
                                     unsigned char *DI, unsigned char *time_buf, Data_t *frameOut);

int BeiDian_Watermeter_ApplicationData(unsigned char t, unsigned char *addr, unsigned char pfc,
                                       unsigned char *DI, Data_t *frame, Data_t *dataOut);

int BeiDian_Watermeter_Format(const unsigned char *data, unsigned short datasize,
                              unsigned short *offset);

int BCT645_Splitairconditioning_DataPack(unsigned char *addr, unsigned char sign, BCT645_FC_T FC,
                                         Data_t *before_des_info, unsigned char *DESkey,
                                         Data_t *frameOut);

#ifdef __cplusplus
}
#endif

#endif
