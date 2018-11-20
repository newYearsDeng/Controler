#include "jni.h"
#include "dll.h"
#include <string.h>

JNIEXPORT int JNICALL Java_com_jmesh_controler_jni_SocketJniConnector_transCmd(
        JNIEnv *env,
        jobject jo, jbyteArray meterCode, jbyteArray di,
        jbyteArray dataResult) {

    int meterCodeLength = (*env)->GetArrayLength(env, meterCode);
    char meterCodeBuff[200];
    (*env)->GetByteArrayRegion(env, meterCode, 0, meterCodeLength, meterCodeBuff);
    meterCodeBuff[meterCodeLength] = '\0';

    int diLength = (*env)->GetArrayLength(env, di);
    char diBuff[200];
    (*env)->GetByteArrayRegion(env, di, 0, diLength, diBuff);
    diBuff[diLength] = '\0';


    INPUT_STRU in = {0};
    in.meterADDR = meterCodeBuff;
    in.di = diBuff;
    char data[200];
    char frame[200];
    char error[132];
    int data_len = 0;
    int frame_len = 0;
    int err_len = 0;
    OUTPUT_STRU output_stru = {data, frame, error, data_len, frame_len, err_len};
    data_pack_socket(&in, &output_stru);

    (*env)->SetByteArrayRegion(env, dataResult, 0, strlen(output_stru.frame), output_stru.frame);
    return strlen(output_stru.frame);
}


JNIEXPORT int JNICALL Java_com_jmesh_controler_jni_SocketJniConnector_resolveResult(
        JNIEnv *env,
        jobject jo, jbyteArray meterCode, jbyteArray dataSrc,
        jbyteArray dataResult) {

    int dataSrcLength = (*env)->GetArrayLength(env, dataSrc);
    char dataSrcCodeBuff[200];
    (*env)->GetByteArrayRegion(env, dataSrc, 0, dataSrcLength, dataSrcCodeBuff);
    dataSrcCodeBuff[dataSrcLength] = '\0';

    int meterCodeLength = (*env)->GetArrayLength(env, meterCode);
    char meterCodeBuff[200];
    (*env)->GetByteArrayRegion(env, meterCode, 0, meterCodeLength, meterCodeBuff);
    meterCodeBuff[meterCodeLength] = '\0';


    INPUT_STRU input_stru = {0};
    input_stru.meterADDR = meterCodeBuff;

    char data[200];
    char *frame = dataSrcCodeBuff;
    char error[132];
    int data_len = 0;
    int frame_len = strlen(frame);
    int err_len = 0;
    OUTPUT_STRU output_stru = {data, frame, error, data_len, frame_len, err_len};
    int flag = data_result_socket(&input_stru, &output_stru);
    (*env)->SetByteArrayRegion(env, dataResult, 0, strlen(output_stru.data), output_stru.data);
    return strlen(output_stru.data);
}

