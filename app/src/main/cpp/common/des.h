#ifndef _DES_H_
#define _DES_H_

#define DES3

enum	{ ENCRYPT, DECRYPT };

#ifdef DES3
#define KeyLen 16
//extern unsigned char gcv_Key1[16];        /*主密钥*/
//extern unsigned char code gcv_Key2[16];       /*固定密钥*/
//extern unsigned char gcv_subKey1[192];     /*主密钥子密钥*/
//extern unsigned char gcv_subKey2[192];    /*固定密钥子密钥*/
#else
#define KeyLen 8
//extern unsigned char gcv_Key1[8];        /*主密钥*/
//extern unsigned char code gcv_Key2[8];       /*固定密钥*/
//extern unsigned char gcv_subKey1[96];     /*主密钥子密钥*/
//extern unsigned char code gcv_subKey2[96];    /*固定密钥子密钥*/
#endif
/*******接口函数********/

// Type—ENCRYPT:加密,DECRYPT:解密
// 输出缓冲区(Out)的长度 >= ((datalen+7)/8)*8,即比datalen大的且是8的倍数的最小正整数
// 加密时，当datalen不为8的整数倍时，则输出密文长度为 (datalen/8)*8 + 8
// 解密时， 当datalen不为8的整数倍时， 则直接退出。

#ifdef __cplusplus
extern "C" {
#endif

	void Des_Go(unsigned char *Out, unsigned char *In, unsigned char datalen, unsigned char *subKey, unsigned char Type);
	void SetSubKey(unsigned char *subKey, unsigned char *Key);

#ifdef __cplusplus
}
#endif

#endif
