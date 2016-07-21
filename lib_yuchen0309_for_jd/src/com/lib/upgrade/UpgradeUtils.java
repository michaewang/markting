package com.lib.upgrade;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentManager;

import com.android.volley.VolleyError;
import com.lib.json.JSONUtils;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;

public final class UpgradeUtils {
	public static void checkUpgrade(final Context context,
		String url, final FragmentManager fm){
		HTTPUtils.getVolley(context, url, new VolleyListener() {
			public void onErrorResponse(VolleyError arg0) {
			}

			public void onResponse(String response) {
				VersionInfo verInfo = JSONUtils.parseJSON(response,
						VersionInfo.class);
				int currVersion = getCurrVersion(context);
				if (verInfo.new_version > currVersion) {
					// json
					MyDialogFragment dlg = new MyDialogFragment();
					dlg.setVersionInfo(verInfo);
					dlg.show(fm, null);
				}
			}
		});
		
	}
	public static int getCurrVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(context
					.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
		}
		return 0;
	}

	private UpgradeUtils() {
	}
}
