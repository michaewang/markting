package com.lib.uil;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.yuchen.R;

public final class ToastUtils {
	private static Toast result;

	/**
	 *
	 * @param context
	 * @param text
	 */
	public static void showToast(Context context, String text) {
		if (result != null) {
			result.cancel();
		}
		result = new Toast(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(
				R.layout.custom_toast, null);
		TextView tv = (TextView) v.findViewById(R.id.textView1);
		tv.setText(text);
		result.setView(v);
		result.setDuration(Toast.LENGTH_SHORT);
		result.show();
	}

	private ToastUtils() {
	}
}
