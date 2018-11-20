#ifndef _PUB_FUNC_H
#define _PUB_FUNC_H

#ifdef __cplusplus
extern "C" {
#endif

	void reverse_array(unsigned char *buff, unsigned short len);

	int bcd_to_decimal(unsigned char bcd);
	unsigned char decimal_to_bcd(int dec);
	bool is_char_have_letter(const unsigned char data);
	bool is_have_letter(const unsigned char *data, const unsigned char byte_len);
	void frame_to_string_templet(char *str_in, char *data_str, unsigned char *frame_data, int &bit_len, int byte_len, int decimal_len, unsigned char is_sign_bit);
	void reverse_data_frame_to_string(unsigned char *data, char *out, unsigned char decimal, unsigned char byte_size, unsigned char is_sign_bit);
	int count_data_sum(unsigned char *data, unsigned char byte_size);

#ifdef __cplusplus
}
#endif

#endif
