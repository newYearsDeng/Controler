#ifndef _PUB_FUNC_H
#define _PUB_FUNC_H

#include "../airswitch/dll.h"

#define BCD_BIN(val) (((val) & 0x0f) + ((val)>>4)*10)
#define BIN_BCD(val) ((((val)/10)<<4) + (val)%10)


#define PARA_METER_ADDR            "METER ADDR"
#define PARA_COLLECTOR1_ADDR    "COLLECTOR1"
#define PARA_BROADCAST            "BROADCAST"
#define PARA_COLLECTOR2_ADDR    "COLLECTOR2"
#define    PARA_ROUTER1_ADDR        "ROUTER1"
#define PARA_ROUTER2_ADDR        "ROUTER2"
#define PARA_IS_BCD                "ISBCD"
#define PARA_SUB_SEQ            "SUBSEQ"

#define    PARA_INFO_DATA            "DATA"
#define PARA_DI                 "DI"
#define PARA_STYLE              "STYLE"
#define PARA_KEY                "KEY"

#ifdef __cplusplus
extern "C" {
#endif

int str2hex(char *str, unsigned short len, unsigned short buffsize, unsigned char *buff);

int hex2str(unsigned char *data, unsigned short len, unsigned short str_size, char *str);

void reverse_array(unsigned char *buff, unsigned short len);

int find_param(INPUT_STRU *in, char *paramName, void *value, int *valueLen);

void set_data(char *info, unsigned char *data, unsigned short len);

#ifdef __cplusplus
}
#endif

#endif
