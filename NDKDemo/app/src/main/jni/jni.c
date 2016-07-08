#include <string.h>
#include <jni.h>

jstring Java_com_studio_shitou_ndkdemo_MainActivity_getStringFromNative
        (JNIEnv *env, jobject obj){
    return (*env)->NewStringUTF(env,"the first NDKdemo success!");
}
