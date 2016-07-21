package com.mars.markting.utils;

import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;


public final class CacheUtils {
	
	private CacheUtils(){
	}
	
	public static String getCacheSize(Context context) {
		File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
		File cachePath = new File(dataDir + "/" + context.getPackageName() +"/cache/uil-images");

		// File cachePath = StorageUtils
		// .getIndividualCacheDirectory(getActivity());
//		Log.e("UIL", "cachePath: " + cachePath);

		long fileLen = getFileLen(cachePath);
		return formatStr(fileLen);
	}
	
	public static void clearCache() {
		ImageLoader.getInstance().clearDiskCache();
	}

	private static String formatStr(long fileLen) {
		String strLen = null;
		if (fileLen < 1024) {
//			strLen = fileLen + ".";
			strLen = "0KB";
		} else if (fileLen < 1024 * 1024) {
			fileLen /= 1024;
			strLen = fileLen + "KB";
		} else if (fileLen < 1024 * 1024 * 1024) {
			fileLen /= 1024 * 1024;
			strLen = fileLen + "MB";
		}
		return strLen;
	}

	private static long getFileLen(File cachePath) {
		long fileLen = 0;
		File[] files = cachePath.listFiles();
		for (int i = 0; i < files.length; i++) {
			fileLen += files[i].length();
		}
		return fileLen;
	}
	
}
