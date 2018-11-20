#include "param.h"
#include "string.h"
#include "stdio.h"
#include "stdlib.h"
#include "../airswitch/dll.h"

#define PARA_KEY "KEY"

int str2hex(char *str, unsigned short len, unsigned short buffsize, unsigned char *buff) {
    int i, j;
    for (i = 0, j = 0; i < len && j < buffsize; j++) {
        if (str[i] <= '9')
            buff[j] = str[i] - '0';
        else if (str[i] <= 'F')
            buff[j] = str[i] - 'A' + 10;
        else if (str[i] <= 'f')
            buff[j] = str[i] - 'a' + 10;

        buff[j] <<= 4;
        i++;
        if (str[i] <= '9')
            buff[j] |= str[i] - '0';
        else if (str[i] <= 'F')
            buff[j] |= str[i] - 'A' + 10;
        else if (str[i] <= 'f')
            buff[j] |= str[i] - 'a' + 10;

        i++;
    }
    return j;
}

const char hexstr[16] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
                         'f'};

int hex2str(unsigned char *data, unsigned short len, unsigned short str_size, char *str) {
    int i, j;
    for (i = 0, j = 0; i < len && j < str_size; i++, j += 2) {
        str[j] = hexstr[0x0f & (data[i] >> 4)];
        str[j + 1] = hexstr[0x0f & data[i]];
    }
    return j;
}

void reverse_array(unsigned char *buff, unsigned short len) {
    unsigned char tmp;
    int i;
    for (i = 0; i < len / 2; i++) {
        tmp = buff[i];
        buff[i] = buff[len - i - 1];
        buff[len - i - 1] = tmp;
    }
}

int find_string(char ch, unsigned char no, char *data, int size, char **pdata) {
    char *p1, *p2;
    unsigned char id = 0;
    p1 = p2 = data;

    while (size) {
        if (*p2 == ch) {
            *pdata = p1;
            if (++id == no) {
                return p2 - p1;
            }
            p1 = p2 + 1;
        }
        p2++;
        size--;
    }
    if (++id == no) {
        *pdata = p1;
        return p2 - p1;
    }
    return 0;
}

int find_param(INPUT_STRU *in, char *paramName, void *value, int *valueLen) {
    int len;
    char *ptr = NULL;
    //   char tmp_str[1000];
    int err_code = 0;

    *valueLen = 0;
    //   sprintf(tmp_str,"can't find param : %s\n",paramName.c_str());
    //   status.err_code=-1;
    //   status.err=tmp_str;


    if (strcmp(paramName, "METER ADDR") == 0) {
        len = strlen(in->meterADDR);
        len = len > 14 ? 14 : len;//水表7字节
        *valueLen = str2hex(in->meterADDR, len, len / 2, (unsigned char *) value);
        err_code = 0;
    } else if (strcmp(paramName, "COLLECTOR1") == 0) {
        len = strlen(in->collector1);
        len = len > 12 ? 12 : len;
        *valueLen = str2hex(in->collector1, len, len / 2, (unsigned char *) value);
        err_code = 0;
    } else if (strcmp(paramName, "COLLECTOR2") == 0) {
        len = strlen(in->collector2);
        len = len > 12 ? 12 : len;
        *valueLen = str2hex(in->collector2, len, len / 2, (unsigned char *) value);
        err_code = 0;
    } else if (strcmp(paramName, "ISBCD") == 0) {
        *(bool *) value = !memcmp(in->isBCD, "BCD", 3);
        *valueLen = 1;
        err_code = 0;
    } else if (strcmp(paramName, "SUBSEQ") == 0) {
        *(unsigned char *) value = in->subSEQ;
        *valueLen = 1;
        err_code = 0;
    }
        /*
        else if (strcmp(paramName, "DATA") == 0)
        {
            if (in->info_datalen)
            {
                         if((len=find_string(',',0,in->info,strlen(in->info),&ptr))>0)
                if (len = strlen(in->info))
                {
                    *valueLen = str2hex(ptr, in->info_datalen, in->info_datalen / 2, (unsigned char *)value);
                    err_code = 0;
                }
                else
                {
                    err_code = -1;
                }
            }
            else
            {
                err_code = -1;
            }
        }
        */
    else if (strcmp(paramName, "DATA") == 0) {
        len = strlen(in->info);
        //len = len>8 ? 8 : len;
        *valueLen = str2hex(in->info, len, len / 2, (unsigned char *) value);
        err_code = 0;
    } else if (strcmp(paramName, "DI") == 0) {
        len = strlen(in->di);
        len = len > 8 ? 8 : len;
        *valueLen = str2hex(in->di, len, len / 2, (unsigned char *) value);
        err_code = 0;
    } else if (strcmp(paramName, "STYLE") == 0) {
        len = strlen(in->stle);
        len = len > 2 ? 2 : len;
        *valueLen = str2hex(in->stle, len, len / 2, (unsigned char *) value);
        err_code = 0;
    } else if (strcmp(paramName, "KEY") == 0) {
        len = strlen(in->key);
        *valueLen = str2hex(in->key, len, len / 2, (unsigned char *) value);
        err_code = 0;
    }

    return err_code;
}

void set_data(char *info, unsigned char *data, unsigned short len) {
    int i, findLen = 0, tmp1, tmp2;
    char *ptr;

    for (i = 1; i <= 15; i++) {
        if ((tmp1 = find_string(',', i, info, strlen(info), &ptr)) > 0 &&
            (tmp2 = find_string(',', i + 1, info, strlen(info), &ptr)) > 0) {
            findLen += (tmp1 + 1);
        } else {
            info[findLen] = ',';

            info[findLen + 1] = '\0';
            findLen += 1;
        }
        if (i == 15) {
            hex2str(data, len, len * 2, info + findLen);
        }
    }
}
