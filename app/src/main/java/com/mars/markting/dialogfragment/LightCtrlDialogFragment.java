package com.mars.markting.dialogfragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.mars.markting.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class LightCtrlDialogFragment extends DialogFragment implements OnClickListener {

	private FragmentActivity mActivity;
	private SeekBar mSeekBarBrightness;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mActivity = getActivity();
		View inflate = inflater.inflate(R.layout.fragment_dialog_light_ctrl, null);
		mSeekBarBrightness = (SeekBar) inflate.findViewById(R.id.seekBar_light);
		inflate.findViewById(R.id.btn_ok).setOnClickListener(this);
		inflate.findViewById(R.id.btn_default).setOnClickListener(this);
		initBrightness();
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return inflate;
	}
	
	/**
	 * ���ȵ���
	 */
	private void initBrightness() {
		// ȡ�õ�ǰ����
		int normal = Settings.System.getInt(mActivity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, 255);
		// ������󶨵�ǰ����
		mSeekBarBrightness.setProgress(normal);
		mSeekBarBrightness
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// ȡ�õ�ǰ���
						int tmpInt = seekBar.getProgress();

						// �����С��80ʱ�����ó�80����ֹ̫�ڿ�����ĺ��
						if (tmpInt < 80) {
							tmpInt = 80;
						}
						// ��ݵ�ǰ��ȸı�����
						WindowManager.LayoutParams wl = mActivity.getWindow()
								.getAttributes();
						float tmpFloat = (float) tmpInt / 255;
						if (tmpFloat > 0 && tmpFloat <= 1) {
							wl.screenBrightness = tmpFloat;
						}
						mActivity.getWindow().setAttributes(wl);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
					}
				});

	}
	
	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.btn_ok:
			break;
		case R.id.btn_default:
			int tmpInt = Settings.System.getInt(mActivity.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, -1);
			WindowManager.LayoutParams wl = mActivity.getWindow().getAttributes();
			float tmpFloat = (float) tmpInt / 255;
			if (tmpFloat > 0 && tmpFloat <= 1) {
				wl.screenBrightness = tmpFloat;
			}
			mActivity.getWindow().setAttributes(wl);
			break;

		default:
			break;
		}
	}
	
}
