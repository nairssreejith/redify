//
// Created by SJ on 01-01-2022.
//

#include <string.h>
#include <jni.h>

extern "C" {

static void escalateRedPixel(int *pixels, int depth, float red, int width, int height){
    float R, G, B;

    for(int i=0; i < width * height; i++){
        R = (pixels[i] >> 16) & 0xFF;
        G = (pixels[i] >> 8) & 0xFF;
        B = (pixels[i]) & 0xFF;

        R += (depth * red);
        if(R > 255) { R = 255; }

        pixels[i] =  (pixels[i] & 0xFF000000) | (((int) R << 16) & 0x00FF0000) | (((int) G << 8) & 0x0000FF00) | ((int) B & 0x000000FF);
    }
}

static inline jint *getPointerArray(JNIEnv *env, jintArray buff) {
    jint *ptrBuff = NULL;
    if (buff != NULL)
        ptrBuff = env->GetIntArrayElements(buff, JNI_FALSE);
    return ptrBuff;
}

static inline jintArray jintToJintArray(JNIEnv *env, jint size, jint *arr) {
    jintArray result = env->NewIntArray(size);
    env->SetIntArrayRegion(result, 0, size, arr);
    return result;
}

static inline void releaseArray(JNIEnv *env, jintArray array1, jint *array2) {
    if (array1 != NULL)
        env->ReleaseIntArrayElements(array1, array2, 0);
}

JNIEXPORT jintArray Java_com_sreejithsnair_redify_view_MainActivity_redified(JNIEnv* env,jclass obj,jintArray pixels, jint depth,jfloat red, jint width, jint height){

    jint *pixelsBuff = getPointerArray(env, pixels);
    escalateRedPixel(pixelsBuff, depth, red, width, height);
    jintArray result = jintToJintArray(env, width * height, pixelsBuff);
    releaseArray(env, pixels, pixelsBuff);
    return result;

}
}
