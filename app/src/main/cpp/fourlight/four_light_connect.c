#include "jni.h"
#include "getdll.h"
#include <string.h>

JNIEXPORT int JNICALL Java_com_jmesh_lib645_jni_FourLightConnector_getLightSwitchStateCmd(
        JNIEnv *env,
        jobject jo, jbyteArray meterCode,
        jbyteArray dataResult) {

    int meterCodeLength = (*env)->GetArrayLength(env, meterCode);
    char meterCodeBuff[200];
    (*env)->GetByteArrayRegion(env, meterCode, 0, meterCodeLength, meterCodeBuff);
    meterCodeBuff[meterCodeLength] = '\0';

    INPUT_STRU in = {0};
    in.meterADDR = meterCodeBuff;
    in.di = "bcdc0001";


    char data[200];
    char frame[200];
    char error[132];
    int data_len = 0;
    int frame_len = 0;
    int err_len = 0;
    OUTPUT_STRU output_stru = {data, frame, error, data_len, frame_len, err_len};
    data_pack_getsiLuDengKong(&in, &output_stru);

    (*env)->SetByteArrayRegion(env, dataResult, 0, strlen(output_stru.frame), output_stru.frame);
    return strlen(output_stru.frame);
}

JNIEXPORT int JNICALL Java_com_jmesh_lib645_jni_FourLightConnector_resolveLightSwitchStateResult(
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
    input_stru.di = "bcdc0001";

    char data[200];
    char *frame = dataSrcBuff;
    char error[132];
    int data_len = 0;
    int frame_len = strlen(frame);
    int err_len = 0;
    OUTPUT_STRU output_stru = {data, frame, error, data_len, frame_len, err_len};
    int flag = data_result_getsiLuDengKong(&input_stru, &output_stru);
    (*env)->SetByteArrayRegion(env, dataResult, 0, strlen(output_stru.data), output_stru.data);
    return strlen(output_stru.data);
}
