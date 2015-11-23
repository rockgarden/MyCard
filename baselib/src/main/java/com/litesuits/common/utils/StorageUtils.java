package com.litesuits.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.litesuits.android.log.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Provides application storage paths
 * Get SD card info.
 *
 * @author MaTianyu
 * @date 2015-04-19
 */
public class StorageUtils {

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String TAG = StorageUtils.class.getSimpleName();

    private StorageUtils() {

    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * is sd card available.
     * @return true if available
     */
    public static boolean isSdCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Get {@link android.os.StatFs}.
     */
    public static StatFs getStatFs(String path) {
        return new StatFs(path);
    }

    /**
     * Get phone data path.
     */
    public static String getDataPath() {
        return Environment.getDataDirectory().getPath();

    }

    /**
     * Get SD card path.
     */
    public static String getNormalSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * Get SD card path by CMD.
     */
    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        String sdcard = null;
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        BufferedReader bufferedReader = null;
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getInputStream())));
            String lineStr;
            while ((lineStr = bufferedReader.readLine()) != null) {
                Log.i(TAG, "proc/mounts:   " + lineStr);
                if (lineStr.contains("sdcard")
                    && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray.length >= 5) {
                        sdcard = strArray[1].replace("/.android_secure", "");
                        Log.i(TAG, "find sd card path:   " + sdcard);
                        return sdcard;
                    }
                }
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                    Log.e(TAG, cmd + " 命令执行失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sdcard = Environment.getExternalStorageDirectory().getPath();
        Log.i(TAG, "not find sd card path return default:   " + sdcard);
        return sdcard;
    }

    /**
     * Get SD card path list.
     */
    public static ArrayList<String> getSDCardPathEx() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                Log.i(TAG, "mount:  " + line);
                if (line.contains("secure")) {
                    continue;
                }
                if (line.contains("asec")) {
                    continue;
                }
                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns.length > 1) {
                        list.add("*" + columns[1]);
                    }
                } else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns.length > 1) {
                        list.add(columns[1]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                Log.w(TAG, "Unable to create external cache directory");
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                Log.i(TAG, "Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted and app has appropriate permission. Else -
     * Android defines cache directory on device's file system.
     *
     * @param context Application context
     * @return Cache {@link File directory}
     */
    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (hasExternalStoragePermission(context)) {
            if (isSdCardAvailable()) {
                appCacheDir = getExternalCacheDir(context);
            }
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            android.util.Log.w(TAG, "Can't define system cache directory! The app should be re-installed.");
        }
        return appCacheDir;
    }

    /**
     * Get available size of SD card.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getAvailableSize(String path) {
        try {
            File base = new File(path);
            StatFs stat = new StatFs(base.getPath());
            return stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get SD card info detail.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static SDCardInfo getSDCardInfo() {
        SDCardInfo sd = new SDCardInfo();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            sd.isExist = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                File sdcardDir = Environment.getExternalStorageDirectory();
                StatFs sf = new StatFs(sdcardDir.getPath());

                sd.totalBlocks = sf.getBlockCountLong();
                sd.blockByteSize = sf.getBlockSizeLong();

                sd.availableBlocks = sf.getAvailableBlocksLong();
                sd.availableBytes = sf.getAvailableBytes();

                sd.freeBlocks = sf.getFreeBlocksLong();
                sd.freeBytes = sf.getFreeBytes();

                sd.totalBytes = sf.getTotalBytes();
            }
        }
        if (Log.isPrint) {
            Log.i(TAG, sd.toString());
        }
        return sd;
    }


    /**
     * see more {@link android.os.StatFs}
     */
    public static class SDCardInfo {
        public boolean isExist;
        public long totalBlocks;
        public long freeBlocks;
        public long availableBlocks;

        public long blockByteSize;

        public long totalBytes;
        public long freeBytes;
        public long availableBytes;

        @Override
        public String toString() {
            return "SDCardInfo{" +
                   "isExist=" + isExist +
                   ", totalBlocks=" + totalBlocks +
                   ", freeBlocks=" + freeBlocks +
                   ", availableBlocks=" + availableBlocks +
                   ", blockByteSize=" + blockByteSize +
                   ", totalBytes=" + totalBytes +
                   ", freeBytes=" + freeBytes +
                   ", availableBytes=" + availableBytes +
                   '}';
        }
    }
}
