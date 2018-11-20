#include "jni.h"
#include "dll.h"
#include <string.h>

//000102030405060708090a0b0c0d0e0f key
//跳闸 1A 合闸 4B
//1 调用 data_pack 传入key 表地址 style style为1A
//2从frame中拿到数据后发送到空调控制器，
//3把空调控制器返回的数据调用data_result存到out.frame里面。
//4从out.中拿到数据，发送到蓝牙控制器
//5拿到数据后再次调用data_result解析结果

JNIEXPORT int JNICALL
Java_com_jmesh_controler_jni_AirConditionerSwitchConnector_getAirConditionerSwitchCmd(
        JNIEnv *env,
        jobject jo, jbyteArray meterCode, jbyteArray style,
        jbyteArray dataResult) {

    int meterCodeLength = (*env)->GetArrayLength(env, meterCode);
    char meterCodeBuff[200];
    (*env)->GetByteArrayRegion(env, meterCode, 0, meterCodeLength, meterCodeBuff);
    meterCodeBuff[meterCodeLength] = '\0';

    int styleLength = (*env)->GetArrayLength(env, style);
    char styleBuff[200];
    (*env)->GetByteArrayRegion(env, style, 0, styleLength, styleBuff);
    styleBuff[styleLength] = '\0';


    INPUT_STRU in = {0};
    in.meterADDR = meterCodeBuff;
    in.stle = styleBuff;
    in.key = "000102030405060708090a0b0c0d0e0f";
//    in.key = "0f0e0d0c0b0a09080706050403020100";
    char data[200];
    char frame[200];
    char error[132];
    int data_len = 0;
    int frame_len = 0;
    int err_len = 0;
    OUTPUT_STRU output_stru = {data, frame, error, data_len, frame_len, err_len};
    data_pack_air_switch(&in, &output_stru);
    (*env)->SetByteArrayRegion(env, dataResult, 0, strlen(output_stru.frame), output_stru.frame);
    return strlen(output_stru.frame);
}


JNIEXPORT int JNICALL
Java_com_jmesh_controler_jni_AirConditionerSwitchConnector_resolveAirConditionerSwitchCmd(
        JNIEnv *env,
        jobject jo, jbyteArray meterCode, jbyteArray style, jbyteArray dataSrc,
        jbyteArray dataResult) {

    int dataSrcLength = (*env)->GetArrayLength(env, dataSrc);
    char dataSrcCodeBuff[200];
    (*env)->GetByteArrayRegion(env, dataSrc, 0, dataSrcLength, dataSrcCodeBuff);
    dataSrcCodeBuff[dataSrcLength] = '\0';

    int meterCodeLength = (*env)->GetArrayLength(env, meterCode);
    char meterCodeBuff[200];
    (*env)->GetByteArrayRegion(env, meterCode, 0, meterCodeLength, meterCodeBuff);
    meterCodeBuff[meterCodeLength] = '\0';

    int styleLength = (*env)->GetArrayLength(env, style);
    char styleBuff[10];
    (*env)->GetByteArrayRegion(env, style, 0, styleLength, styleBuff);
    styleBuff[styleLength] = '\0';


    INPUT_STRU input_stru = {0};
    input_stru.meterADDR = meterCodeBuff;
    input_stru.stle = styleBuff;
    input_stru.key = "000102030405060708090a0b0c0d0e0f";
//    input_stru.key = "0f0e0d0c0b0a09080706050403020100";

    char data[200];
    char *frame = dataSrcCodeBuff;
    char error[132];
    int data_len = 0;
    int frame_len = strlen(frame);
    int err_len = 0;
    OUTPUT_STRU output_stru = {data, frame, error, data_len, frame_len, err_len};
    int flag = data_result_air_switch(&input_stru, &output_stru);
    (*env)->SetByteArrayRegion(env, dataResult, 0, strlen(output_stru.frame), output_stru.frame);
    return strlen(output_stru.frame);
}

