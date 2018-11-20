#ifndef _DES_H_
#define _DES_H_

#define DES3

enum	{ ENCRYPT, DECRYPT };

#ifdef DES3
#define KeyLen 16
//extern unsigned char gcv_Key1[16];        /*����Կ*/
//extern unsigned char code gcv_Key2[16];       /*�̶���Կ*/
//extern unsigned char gcv_subKey1[192];     /*����Կ����Կ*/
//extern unsigned char gcv_subKey2[192];    /*�̶���Կ����Կ*/
#else
#define KeyLen 8
//extern unsigned char gcv_Key1[8];        /*����Կ*/
//extern unsigned char code gcv_Key2[8];       /*�̶���Կ*/
//extern unsigned char gcv_subKey1[96];     /*����Կ����Կ*/
//extern unsigned char code gcv_subKey2[96];    /*�̶���Կ����Կ*/
#endif
/*******�ӿں���********/

// Type��ENCRYPT:����,DECRYPT:����
// ���������(Out)�ĳ��� >= ((datalen+7)/8)*8,����datalen�������8�ı�������С������
// ����ʱ����datalen��Ϊ8��������ʱ����������ĳ���Ϊ (datalen/8)*8 + 8
// ����ʱ�� ��datalen��Ϊ8��������ʱ�� ��ֱ���˳���

#ifdef __cplusplus
extern "C" {
#endif

	void Des_Go(unsigned char *Out, unsigned char *In, unsigned char datalen, unsigned char *subKey, unsigned char Type);
	void SetSubKey(unsigned char *subKey, unsigned char *Key);

#ifdef __cplusplus
}
#endif

#endif
