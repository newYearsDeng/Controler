#include "des.h"
#include "string.h"

#ifdef DES3	 //定义了执行以下程序

//unsigned char gcv_Key1[16];       /*主密钥*/
//unsigned char gcv_subKey1[192];     /*主密钥子密钥*/

//固定密钥子密钥
unsigned char gcv_subKey2[192] =
{
	0x10, 0x00, 0x88, 0x57, 0x2a, 0xc2,
	0x10, 0x08, 0x80, 0x50, 0xa3, 0x47,
	0x10, 0x28, 0x00, 0xf6, 0x84, 0x8c,
	0x00, 0x24, 0x04, 0x48, 0x37, 0xcb,
	0x40, 0x04, 0x04, 0x3e, 0xf0, 0x29,
	0x40, 0x80, 0x20, 0x62, 0x5d, 0x62,
	0x80, 0x80, 0x22, 0x8c, 0xa9, 0x3a,
	0xa0, 0x02, 0x02, 0xe5, 0x5e, 0x50,
	0x20, 0x12, 0x02, 0xcb, 0x9a, 0x40,
	0x20, 0x10, 0x40, 0xd0, 0xc7, 0x3c,
	0x00, 0x40, 0x50, 0x19, 0x1e, 0x8c,
	0x04, 0x41, 0x10, 0xd8, 0x70, 0xb1,
	0x06, 0x01, 0x01, 0x23, 0x6a, 0x2d,
	0x0b, 0x00, 0x01, 0xb2, 0x39, 0x92,
	0x09, 0x00, 0x88, 0xa5, 0x03, 0x37,
	0x01, 0x00, 0x88, 0xa7, 0x43, 0xc0,
	0x00, 0x00, 0xa8, 0x5f, 0xf6, 0xea,
	0x10, 0x80, 0x80, 0x58, 0xff, 0x6f,
	0x10, 0x08, 0x02, 0x7e, 0xdc, 0xbc,
	0x20, 0x20, 0x04, 0xc9, 0x7d, 0xfb,
	0x00, 0x04, 0x04, 0xaf, 0xfa, 0x39,
	0x40, 0x00, 0x30, 0xf3, 0x5f, 0x76,
	0x84, 0x80, 0x20, 0x9d, 0x8b, 0xbe,
	0x82, 0x02, 0x02, 0xd5, 0x7e, 0xd5,
	0x20, 0x02, 0x03, 0xf9, 0xba, 0xc5,
	0x21, 0x10, 0x00, 0xf2, 0xe6, 0xbf,
	0x00, 0x00, 0xd0, 0x3f, 0x3f, 0x8f,
	0x14, 0x40, 0x10, 0xbe, 0x71, 0xf3,
	0x06, 0x01, 0x00, 0x67, 0xeb, 0x67,
	0x0a, 0x00, 0x05, 0xf6, 0xad, 0xda,
	0x09, 0x00, 0x08, 0xed, 0x97, 0x5f,
	0x41, 0x00, 0x88, 0xef, 0x97, 0xe8
};
#else  //否则执行以下程序
//上个长度的一半
//unsigned char gcv_Key1[8];       /*主密钥*/
//unsigned char gcv_subKey1[96];     /*主密钥子密钥*/
//固定密钥子密钥
unsigned char gcv_subKey2[96] =
{
	0x10, 0x00, 0x88, 0x57, 0x2a, 0xc2,
	0x10, 0x08, 0x80, 0x50, 0xa3, 0x47,
	0x10, 0x28, 0x00, 0xf6, 0x84, 0x8c,
	0x00, 0x24, 0x04, 0x48, 0x37, 0xcb,
	0x40, 0x04, 0x04, 0x3e, 0xf0, 0x29,
	0x40, 0x80, 0x20, 0x62, 0x5d, 0x62,
	0x80, 0x80, 0x22, 0x8c, 0xa9, 0x3a,
	0xa0, 0x02, 0x02, 0xe5, 0x5e, 0x50,
	0x20, 0x12, 0x02, 0xcb, 0x9a, 0x40,
	0x20, 0x10, 0x40, 0xd0, 0xc7, 0x3c,
	0x00, 0x40, 0x50, 0x19, 0x1e, 0x8c,
	0x04, 0x41, 0x10, 0xd8, 0x70, 0xb1,
	0x06, 0x01, 0x01, 0x23, 0x6a, 0x2d,
	0x0b, 0x00, 0x01, 0xb2, 0x39, 0x92,
	0x09, 0x00, 0x88, 0xa5, 0x03, 0x37,
	0x01, 0x00, 0x88, 0xa7, 0x43, 0xc0
};
#endif

// initial permutation IP
// 对输入明文或带解密数据进行IP置换。
unsigned char IP_Table[64] =
{
	58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
	62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
	57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
	61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7
};
// final permutation IP^-1
unsigned char IPR_Table[64] =
{
	40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31,
	38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
	36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
	34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25
};
// expansion operation matrix
unsigned char E_Table[48] =
{
	32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9,
	8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
	16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
	24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1
};
// 32-bit permutation function P used on the output of the S-boxes
unsigned char P_Table[32] =
{
	16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10,
	2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25
};
// permuted choice table (key)
unsigned char PC1_Table[56] =
{
	57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18,
	10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36,
	63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22,
	14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4
};
// permuted choice key (table)
unsigned char PC2_Table[48] =
{
	14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10,
	23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2,
	41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
	44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
};
// number left rotations of pc1
unsigned char LOOPL_Table[16] =
{
	1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
};
// number right rotations of pc1
unsigned LOOPR_Table[16] =
{
	0, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
};

// The (in)famous S-boxes
unsigned char S_Box[512] =
{
	// S1
	14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
	0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
	4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
	15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13,
	// S2
	15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
	3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
	0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
	13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9,
	// S3
	10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
	13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
	13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
	1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12,
	// S4
	7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
	13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
	10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
	3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14,
	// S5
	2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
	14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
	4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
	11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3,
	// S6
	12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
	10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
	9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
	4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13,
	// S7
	4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
	13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
	1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
	6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12,
	// S8
	13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
	1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
	7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
	2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
};

void DES(unsigned char *Out, unsigned char *In, unsigned char *subKey, unsigned char Type); //单DES函数
void S_func(unsigned char *Out, unsigned char *In);// S 盒代替
void Transform(unsigned char *Out, unsigned char *In, const unsigned char *Table, unsigned char len);// 变换
void Xor(unsigned char *InA, unsigned char *InB, unsigned char len);// 异或
void RotateL(unsigned char *In, unsigned char loop);// 循环左移


/*************************************************
Function:       Des_Go()
Description:    DES/3DES
Calls:          SetKey()， DES()
Called By:      external call
Table Accessed: //
Table Updated:  //
Input:          *In      -- buffer pointer of plaintext;
datalen  -- length of plaintext;
*subKey  -- buffer pointer of Key;
Type     -- mode for do DES or 3DES.

Output:         *Out     -- result which is ciphertext or plaintext after DES/3DES.
Return:         false(0) -- when the input data is wrong;
true (1) -- when the input data is wright.
Others:
*************************************************/
void Des_Go(unsigned char *Out, unsigned char *In, unsigned char datalen, unsigned char *subKey, unsigned char Type)
{
	unsigned char  i, j, k;
	unsigned char temp = datalen;

	if (Type == ENCRYPT)
	{
		if (temp == 0)           //判断输入是否满足条件
			return;
		if (temp % 8)                                                       //检查明文数据长度是否为8字节的整数倍。如果不是，
		{
			i = temp % 8;												//则用最少的数据补齐尾部使满足8字节整数倍，补充数据
			if (i == 7)  													//的顺序依次为：+ 0x80 0x00 ....0x00，即先不0x80,再补0x00
			{
				*(In + temp) = 0x80;
			}
			else
			{
				*(In + temp) = 0x80;
				for (j = 1; j<(8 - i); j++)
				{
					*(In + temp + j) = 0x00;
				}

			}
			temp += (8 - i);
		}
	}
	else
	{
		if ((temp & 0x07) || (temp == 0))
			return;
	}
	j = temp >> 3;

	// 3次DES 加密:加(key0)-解(key1)-加(key0) 解密::解(key0)-加(key1)-解(key0)
	for (i = 0; i<j; i++)
	{
		k = i << 3;
		DES((Out + k), (In + k), subKey, Type);
#ifdef DES3
		DES((Out + k), (Out + k), (subKey + 96), !Type);
		DES((Out + k), (Out + k), subKey, Type);
#endif
	}
	return;
}

/*************************************************
Function:       DES()
Description:    singal DES for encryption or decryption
Calls:             Transform(), F_func(), Xor(), BitToByte()......;
Called By:      Des_Go();
Table Accessed: //
Table Updated:  //
Input:          *In      -- buffer pointer of plaintext;
*subKey  -- buffer pointer of 8-bytes Key;
Type     -- mode for do DES or 3DES.

Output:         *Out     -- result which is ciphertext or plaintext after singal DES.
Return:         None
Others:
*************************************************/
void DES(unsigned char *Out, unsigned char *In, unsigned char *subKey, unsigned char Type)
{
	unsigned char step;

	unsigned char MR[6];
	unsigned char MM[8];
	unsigned char TEM[8];
	unsigned char Temp[4];
	unsigned char *Li, *Ri;

	Li = MM;
	Ri = MM + 4;
	Transform(MM, In, IP_Table, 64);              // 对输入明文或带解密数据进行IP置换。

	;//QD_Watchdog();                 //看门狗

	// 循环16次进行加密或解密。
	for (step = 0; step < 16; step++)
	{
		//ramtoram(4, TEM, Ri);
		memcpy(TEM, Ri, 4);
		Transform(MR, Ri, E_Table, 48);
		if (Type == ENCRYPT)
		{
			Xor(MR, subKey + step * 6, 6);
		}
		else
		{
			Xor(MR, subKey + 90 - step * 6, 6);
		}
		//		S_func(Ri, MR);
		//		Transform(Ri, Ri, P_Table, 32);
		S_func(Temp, MR);
		Transform(Ri, Temp, P_Table, 32);

		Xor(Ri, Li, 4);
		//ramtoram(4, Li, TEM);
		memcpy(Li, TEM, 4);
		if ((step == 7) || (step == 15))
		{
			;//QD_Watchdog(); 			   //看门狗
		}
	}

	//ramtoram(8, TEM, MM);                                        //调换R16与L16
	//ramtoram(4, MM, TEM+4);
	//ramtoram(4, MM+4, TEM);
	memcpy(TEM, MM, 8);
	memcpy(MM, TEM + 4, 4);
	memcpy(MM + 4, TEM, 4);
	Transform(Out, MM, IPR_Table, 64);
}

/*************************************************
Function:       SetSubKey()
Description:    get a groupt of 16 sub key.
Calls:            Transform(), RotateL();
Called By:      SetKey();
Table Accessed: //
Table Updated:  //
Input:          Key[8]   -- buffer of 64-bits Key;

Output:         pSubKey  -- buffer pointer of a group of 16 sub Key;
Return:         None
Others:
*************************************************/
void SetSubKey(unsigned char *subKey, unsigned char *Key)
{
	unsigned char i;
	unsigned char PC1[7];
	//QD_Watchdog();					//喂狗
	Transform(PC1, Key, PC1_Table, 56); 					// 把64位的密钥变换成56位
	for (i = 0; i<16; i++)
	{
		RotateL(PC1, LOOPL_Table[i]);
		Transform(subKey + i * 6, PC1, PC2_Table, 48);
	}
	//QD_Watchdog();					//喂狗
#ifdef DES3
	Transform(PC1, Key + 8, PC1_Table, 56);						// 把64位的密钥变换成56位
	for (i = 0; i<16; i++)
	{
		RotateL(PC1, LOOPL_Table[i]);
		Transform(subKey + 96 + i * 6, PC1, PC2_Table, 48);
	}
	//QD_Watchdog();					//喂狗
#endif
}

/*************************************************
Function:       S_func()
Description:    Substitution Box to get B[1]...B[8]
Calls:
Called By:      F_func();
Table Accessed: //
Table Updated:  //
Input:          In[6]   -- buffer of Expanded R(i);

Output:         Out[4]  -- concatenation of B[1] to B[8].
Return:         None.
Others:
*************************************************/
void S_func(unsigned char *Out, unsigned char *In)
{
	unsigned char t1, t2, j, k, temp;

	temp = *In; 				 // 6 bits		 B1
	j = ((temp & 0x80) >> 2) | ((temp & 0x04) << 2);
	k = (temp & 0x78) >> 3;
	t1 = (S_Box[j + k]) << 4;

	temp = (*In << 6) | (*(In + 1) >> 2);  // 2+4 bits	 // B2
	j = ((temp & 0x80) >> 2) | ((temp & 0x04) << 2);
	k = (temp & 0x78) >> 3;
	t2 = S_Box[64 + j + k];

	*Out = t1 | t2;

	temp = (*(In + 1) << 4) | (*(In + 2) >> 4); // 4+2 bits    B3
	j = ((temp & 0x80) >> 2) | ((temp & 0x04) << 2);
	k = (temp & 0x78) >> 3;
	t1 = (S_Box[128 + j + k]) << 4;

	temp = *(In + 2) << 2;				//	6 bits		B4
	j = ((temp & 0x80) >> 2) | ((temp & 0x04) << 2);
	k = (temp & 0x78) >> 3;
	t2 = S_Box[192 + j + k];

	*(Out + 1) = t1 | t2;

	temp = *(In + 3); 				//	6 bits		 B5
	j = ((temp & 0x80) >> 2) | ((temp & 0x04) << 2);
	k = (temp & 0x78) >> 3;
	t1 = (S_Box[256 + j + k]) << 4;

	temp = (*(In + 3) << 6) | (*(In + 4) >> 2);  // 2+4 bits	  B6
	j = ((temp & 0x80) >> 2) | ((temp & 0x04) << 2);
	k = (temp & 0x78) >> 3;
	t2 = S_Box[320 + j + k];

	*(Out + 2) = t1 | t2;

	temp = (*(In + 4) << 4) | (*(In + 5) >> 4); // 4+2 bits 	   B7
	j = ((temp & 0x80) >> 2) | ((temp & 0x04) << 2);
	k = (temp & 0x78) >> 3;
	t1 = (S_Box[384 + j + k]) << 4;

	temp = *(In + 5) << 2;				 // 6 bits			B8
	j = ((temp & 0x80) >> 2) | ((temp & 0x04) << 2);
	k = (temp & 0x78) >> 3;
	t2 = S_Box[448 + j + k];

	*(Out + 3) = t1 | t2;
}

/*************************************************
Function:       Transform()
Description:    According to the Permutation table, adjust data sequence.
Calls:            SetBuf(), BufCpy();
Called By:      F_func(), SetSubKey(), DES();
Table Accessed: //
Table Updated:  //
Input:          *In      -- buffer pointer of input data;
*Table   -- the table for Permutation;
len      -- length of In or Out;

Output:         *Out     -- result after Permutation.
Return:         None.
Others:
*************************************************/
void Transform(unsigned char *Out, unsigned char *In, const unsigned char *Table, unsigned char len)
{
	unsigned char i, j, k;
	unsigned char temp;
	unsigned char Tmp[8];

	//SetArray(Tmp, 0x00, 8);
	memset(Tmp, 0x00, 8);
	for (i = 0; i<len; i++)
	{

		j = *(Table + i) - 1;
		k = j & 0x07;
		j = j >> 3;
		temp = (*(In + j) & (0x80 >> k)) << k;

		k = i & 0x07;
		j = i >> 3;
		*(Tmp + j) |= temp >> k;
	}
	j = len >> 3;
	//ramtoram(j, Out, Tmp);
	memcpy(Out, Tmp, j);
}

/*************************************************
Function:       Xor()
Description:    XOR between array InA and InB.
Calls:          None.
Called By:      F_func(), DES();
Table Accessed: //
Table Updated:  //
Input:          *InA      -- buffer pointer of array InA;
*InB      -- buffer pointer of array InB;
len      -- length of InA or InB;

Output:         *InA      -- buffer pointer of array InA after XOR.
Return:         None.
Others:
*************************************************/
void Xor(unsigned char *InA, unsigned char *InB, unsigned char len)
{
	unsigned char i;
	for (i = 0; i<len; i++)
		*(InA + i) ^= *(InB + i);
}
/*************************************************
Function:       RotateL()
Description:    circular left shift.
Calls:
Called By:      SetSubKey();
Table Accessed: //
Table Updated:  //
Input:          *In      -- buffer pointer;
loop   -- offset .


Output:         *In      -- buffer pointer after some circular left shifts.
Return:         None.
Others:
*************************************************/
void RotateL(unsigned char *In, unsigned char loop)
{
	unsigned char i;
	unsigned char Tmp[8];

	if (loop > 2)
		return;
	//SetArray(Tmp, 0x00,7);
	memset(Tmp, 0x00, 7);
	for (i = 0; i<7; i++)      //循环左移 56bits
	{
		if (i == 3)                                                                //为什么相加不可以？？？ !!!!!
		{
			*(Tmp + 3) = ((*(In + 3) & 0xf0) << loop) | ((*In >> (4 - loop)) & 0xf0);     //字节3: 4bits --- bit7-bit4
			*(Tmp + 3) |= ((*(In + 3) << loop) & 0x0f) | ((*(In + 4) >> (8 - loop)));   //字节3: 4bits --- but3-bit0
		}
		else if (i == 6)
		{
			*(Tmp + 6) = (*(In + 6) << loop) | ((*(In + 3) & 0x0f) >> (4 - loop));        //字节6: 8bits
		}
		else
		{
			*(Tmp + i) = (*(In + i) << loop) | (*(In + i + 1) >> (8 - loop));           //字节0-2： 24bits
			//字节4-5： 16bits
		}
	}
	//ramtoram(7, In, Tmp);
	memcpy(In, Tmp, 7);
}

