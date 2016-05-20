package com.rockgarden.sign.jni;

public class Jni {
	static {
		System.loadLibrary("sign");
	}

	/**
	 * native方法
	 */
	public static native String getInfoMD5(String info);
	public static native String getCustomInfoMD5(String info);
	
}