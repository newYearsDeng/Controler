#ifndef __ZJJ_DLL_H__
#define __ZJJ_DLL_H__

#include <stdbool.h>

typedef struct {
    char *meterADDR;
    char *collector2;
    char *collector1;
    char *info;
    int info_datalen;
    char *route1;
    char *route2;
    char *isBCD;
    unsigned char subSEQ;
    char *di;
    char *stle;
    char *key;
} INPUT_STRU;

typedef struct {
    char *data;
    char *frame;
    char *error;
    int data_len;
    int frame_len;
    int err_len;
} OUTPUT_STRU;

#ifdef __cplusplus
extern "C"
{
#endif

int data_pack_air_switch(INPUT_STRU *in, OUTPUT_STRU *out);

int data_result_air_switch(INPUT_STRU *in, OUTPUT_STRU *out);

int data_result_tz_air_switch(INPUT_STRU *in, OUTPUT_STRU *out);

#ifdef __cplusplus
}
#endif

#endif
