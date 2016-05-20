package com.rockgarden.sign.jni;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class JniCallBack {
	public static String GetPackageName(Context context) {
		String packageName = "";
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			packageName = info.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageName;
	}

	public static String GetPrefsName() {
		return "com.rockgarden.sign";
	}
}
