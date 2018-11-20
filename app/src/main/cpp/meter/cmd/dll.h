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
    char *key;
    int decimal_len;
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

int data_pack_cmd(INPUT_STRU *in, OUTPUT_STRU *out);

int data_result_cmd(INPUT_STRU *in, OUTPUT_STRU *out);

void out_data_to_string(unsigned char *data_buf, int byte_len, unsigned char len, char *out_data);


int bcd_to_decimal(unsigned char bcd);

unsigned char decimal_to_bcd(int dec);

bool is_char_have_letter(const unsigned char data);

bool is_have_letter(const unsigned char *data, const unsigned char byte_len);

void frame_to_string_templet(char *str_in, char *data_str, unsigned char *frame_data, int *bit_len,
                             int byte_len, int decimal_len, unsigned char is_sign_bit);

void reverse_data_frame_to_string(unsigned char *data, char *out, unsigned char decimal,
                                  unsigned char byte_size, unsigned char is_sign_bit);

int count_data_sum(unsigned char *data, unsigned char byte_size);

#ifdef __cplusplus
}
#endif

#endif
