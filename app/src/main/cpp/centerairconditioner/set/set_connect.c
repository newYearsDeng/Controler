#include "jni.h"
#include "dll.h"
#include <string.h>

JNIEXPORT int JNICALL
Java_com_jmesh_lib645_jni_CenterAirConditionerConnector_setAirConditioner(
        JNIEnv *env,
        jobject jo, jbyteArray meterCode, jbyteArray di, jbyteArray srcData,
        jbyteArray dataResult) {

    int meterCodeLength = (*env)->GetArrayLength(env, meterCode);
    char meterCodeBuff[200];
    (*env)->GetByteArrayRegion(env, meterCode, 0, meterCodeLength, meterCodeBuff);
    meterCodeBuff[meterCodeLength] = '\0';


    int diLength = (*env)->GetArrayLength(env, di);
    char diBuff[200];
    (*env)->GetByteArrayRegion(env, di, 0, diLength, diBuff);
    diBuff[diLength] = '\0';

    int srcDataLength = (*env)->GetArrayLength(env, srcData);
    char srcDataBuff[200];
    (*env)->GetByteArrayRegion(env, srcData, 0, srcDataLength, srcDataBuff);
    srcDataBuff[srcDataLength] = '\0';


    INPUT_STRU in = {0};
    in.meterADDR = meterCodeBuff;
    in.di = diBuff;
    in.info =srcDataBuff;
    in.info_datalen = strlen(srcDataBuff);

    char data[200];
    char frame[200];
    char error[132];
    int data_len = 0;
    int frame_len = 0;
    int err_len = 0;
    OUTPUT_STRU output_stru = {data, frame, error, data_len, frame_len, err_len};
    data_pack_setZhongYangKongTiao(&in, &output_stru);
    (*env)->SetByteArrayRegion(env, dataResult, 0, strlen(output_stru.frame), output_stru.frame);
    return strlen(output_stru.frame);
}

