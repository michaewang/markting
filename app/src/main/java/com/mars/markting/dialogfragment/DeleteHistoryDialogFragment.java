package com.mars.markting.dialogfragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

import com.mars.markting.R;
import com.mars.markting.activity.HistoryActivity;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class DeleteHistoryDialogFragment extends DialogFragment implements OnClickListener {

	private HistoryActivity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mActivity = (HistoryActivity) getActivity();
		View inflate = inflater.inflate(R.layout.fragment_dialog_delete_history, null);
		inflate.findViewById(R.id.btn_ok).setOnClickListener(this);
		inflate.findViewById(R.id.btn_cancel).setOnClickListener(this);
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return inflate;
	}
	

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.btn_ok:
			mActivity.clearHistory();
			break;

		default:
			break;
		}
	}
	
}
