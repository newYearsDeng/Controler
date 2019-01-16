#include "jni.h"
#include "setdll.h"
#include <string.h>


JNIEXPORT int JNICALL Java_com_jmesh_lib645_jni_FourLightConnector_setLightStateEnable(
        JNIEnv *env,
        jobject jo, jbyteArray meterCode, jbyteArray dataSrc,
        jbyteArray dataResult) {

    int meterCodeLength = (*env)->GetArrayLength(env, meterCode);
    char meterCodeBuff[200];
    (*env)->GetByteArrayRegion(env, meterCode, 0, meterCodeLength, meterCodeBuff);
    meterCodeBuff[meterCodeLength] = '\0';

    int dataSrcLength = (*env)->GetArrayLength(env, dataSrc);
    char dataSrcBuff[20];
    (*env)->GetByteArrayRegion(env, dataSrc, 0, dataSrcLength, dataSrcBuff);
    dataSrcBuff[dataSrcLength] = '\0';

    INPUT_STRU input_stru = {0};
    input_stru.meterADDR = meterCodeBuff;
    input_stru.di = "97200102";
    input_stru.info = dataSrcBuff;
    input_stru.info_datalen = strlen(dataSrcBuff);


    char data[200];
    char *frame = dataSrcBuff;
    char error[132];
    int data_len = 0;
    int frame_len = strlen(frame);
    int err_len = 0;
    OUTPUT_STRU output_stru = {data, frame, error, data_len, frame_len, err_len};
    int flag = data_pack_setsiLuDengKong(&input_stru, &output_stru);
    (*env)->SetByteArrayRegion(env, dataResult, 0, strlen(output_stru.frame), output_stru.frame);
    return strlen(output_stru.frame);
}

JNIEXPORT int JNICALL Java_com_jmesh_lib645_jni_FourLightConnector_setLightStatedisable(
        JNIEnv *env,
        jobject jo, jbyteArray meterCode, jbyteArray dataSrc,
        jbyteArray dataResult) {

    int meterCodeLength = (*env)->GetArrayLength(env, meterCode);
    char meterCodeBuff[200];
    (*env)->GetByteArrayRegion(env, meterCode, 0, meterCodeLength, meterCodeBuff);
    meterCodeBuff[meterCodeLength] = '\0';

    int dataSrcLength = (*env)->GetArrayLength(env, dataSrc);
    char dataSrcBuff[200];
    (*env)->GetByteArrayRegion(env, dataSrc, 0, dataSrcLength, dataSrcBuff);
    dataSrcBuff[dataSrcLength] = '\0';

    INPUT_STRU input_stru = {0};
    input_stru.meterADDR = meterCodeBuff;
    input_stru.di = "97200101";
    input_stru.info = dataSrcBuff;
    input_stru.info_datalen = strlen(dataSrcBuff);


    char data[200];
    char *frame = dataSrcBuff;
    char error[132];
    int data_len = 0;
    int frame_len = strlen(frame);
    int err_len = 0;
    OUTPUT_STRU output_stru = {data, frame, error, data_len, frame_len, err_len};
    int flag = data_pack_setsiLuDengKong(&input_stru, &output_stru);
    (*env)->SetByteArrayRegion(env, dataResult, 0, strlen(output_stru.frame), output_stru.frame);
    return strlen(output_stru.frame);
}
