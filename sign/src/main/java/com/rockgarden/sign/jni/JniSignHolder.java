package com.rockgarden.sign.jni;

public class JniSignHolder {
    static {
        System.loadLibrary("sign");
    }

    public JniSignHolder() {
    }

    /**
     * native方法
     */
    public static native String getInfoMD5(String info);

    public static native String getCustomInfoMD5(String info);

    public native String stringFromJNI();

    public native String unimplementedStringFromJNI();

}