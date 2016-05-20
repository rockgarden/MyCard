package com.eastcom.sign.jni;

public class Jni {
	static {
		System.loadLibrary("sign");
	}

	/**
	 * getInfoMD5: native方法，在C代码里实现
	 */
	public static native String getInfoMD5(String info);
	
	public static native String getCustomInfoMD5(String info);
	
}