#ifndef __ZJJ_DLL_H__
#define __ZJJ_DLL_H__

#ifdef __cplusplus
extern "C"
{
#endif

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
    char *pwd;
} INPUT_STRU;

typedef struct {
    char *data;
    char *frame;
    char *error;
    int data_len;
    int frame_len;
    int err_len;
} OUTPUT_STRU;

int data_pack_light(INPUT_STRU *in, OUTPUT_STRU *out);
int data_result_light(INPUT_STRU *in, OUTPUT_STRU *out);

#ifdef __cplusplus
}
#endif

#endif
