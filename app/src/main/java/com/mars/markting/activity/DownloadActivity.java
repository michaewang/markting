package com.mars.markting.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.VolleyError;
import com.mars.markting.R;
import com.mars.markting.utils.Constants;
import com.mars.markting.utils.Utils;
import com.google.gson.reflect.TypeToken;
import com.lib.json.JSONUtils;
import com.lib.uil.UILUtils;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;

/*import example.filedownload.pub.DownloadMgr;
import example.filedownload.pub.DownloadTask;
import example.filedownload.pub.DownloadTaskListener;*/

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadActivity extends FragmentActivity implements
		OnClickListener {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		Log.d("michael","DownloadActivity is delete ~~~~~~~~~~~~~~~~~~~~~");
	}

	@Override
	public void onClick(View v) {

	}

	/** Called when the activity is first created. */
	/*private static final String TAG = "DownloadActivity";//michael
	private static final int MSG_START_DOWNLOAD = 1;
	private static final int MSG_STOP_DOWNLOAD = 2;
	private static final int MSG_PAUSE_DOWNLOAD = 3;
	private static final int MSG_CONTINUE_DOWNLOAD = 4;
	private static final int MSG_INSTALL_APK = 5;
	private static final int MSG_CLOSE_ALL_DOWNLOAD_TASK = 6;

	private DownloadMgr mgr;

	private DownloadTask tasks[];
	// private DownloadTask tasks[];
	// private DownloadMgr tasks[];
	private DownloadListAdapter adapter;
	private DownloadTaskListener downloadTaskListener = new DownloadTaskListener() {

		@Override
		public void updateProcess(DownloadTask mgr) {
			for (int i = 0; i < mData.size(); i++) {
				if (mData.get(i).appurl.equalsIgnoreCase(mgr.getUrl())) {
					DownloadActivity.this.updateDownload(i, mgr);
				}
			}
		}

		@Override
		public void finishDownload(DownloadTask mgr) {
			for (int i = 0; i < mData.size(); i++) {
				if (mData.get(i).appurl.equalsIgnoreCase(mgr.getUrl())) {
					Button btnStart = (Button) adapter.viewList.get(i)
							.findViewById(R.id.btn_start);
					Button btnPause = (Button) adapter.viewList.get(i)
							.findViewById(R.id.btn_pause);
					Button btnStop = (Button) adapter.viewList.get(i)
							.findViewById(R.id.btn_stop);
					Button btnContinue = (Button) adapter.viewList.get(i)
							.findViewById(R.id.btn_continue);

					btnStart.setVisibility(0);
					btnPause.setVisibility(8);
					btnStop.setVisibility(8);
					btnContinue.setVisibility(8);
					Log.i("TEST",
							"" + mgr.getTotalSize() + " " + mgr.getTotalTime()
									+ " " + mgr.getDownloadSpeed());
					DownloadActivity.this.installAPK(i);

					// Log.v(null, "Test threadNum:" + Downloader.THREADNUM +
					// " totalSize:" + mgr.getTotalSize() +
					// " totalTIme:" + mgr.getTotalTime() +
					// " speed:" + mgr.getDownloadSpeed()
					// );

					// if (Downloader.THREADNUM <= 30) {
					// Downloader.THREADNUM += 5;
					// startDownload(i);
					// }
				}
			}
		}

		@Override
		public void preDownload() {
			Log.i(TAG, "preDownload");
		}

		@Override
		public void errorDownload(int error) {
			Log.i(TAG, "errorDownload");
		}
	};

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			while (!Utils.isNetworkAvailabel(DownloadActivity.this)) {
				Toast.makeText(DownloadActivity.this, "�����ѶϿ�",
						Toast.LENGTH_LONG).show();
				Message msg = new Message();
				msg.what = MSG_CLOSE_ALL_DOWNLOAD_TASK;
				handler.sendMessage(msg);
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_START_DOWNLOAD:
				startDownload(msg.arg1);
				break;
			case MSG_STOP_DOWNLOAD:
				stopDownload(msg.arg1);
				break;
			case MSG_PAUSE_DOWNLOAD:
				pauseDownload(msg.arg1);
				break;
			case MSG_CONTINUE_DOWNLOAD:
				continueDownload(msg.arg1);
				break;
			case MSG_INSTALL_APK:
				Button btnStart = (Button) adapter.viewList.get(msg.arg1)
						.findViewById(R.id.btn_start);
				Button btnPause = (Button) adapter.viewList.get(msg.arg1)
						.findViewById(R.id.btn_pause);
				Button btnStop = (Button) adapter.viewList.get(msg.arg1)
						.findViewById(R.id.btn_stop);
				Button btnContinue = (Button) adapter.viewList.get(msg.arg1)
						.findViewById(R.id.btn_continue);

				btnStart.setVisibility(0);
				btnPause.setVisibility(8);
				btnStop.setVisibility(8);
				btnContinue.setVisibility(8);
				installAPK(msg.arg1);
				break;

			case MSG_CLOSE_ALL_DOWNLOAD_TASK:
				for (int i = 0; i < mData.size(); i++) {
					if (tasks[i] != null) {
						// tasks[i].pause();
					}
				}
				break;
			}
		}
	};
	private ListView mListView;
	private ArrayList<MyApps> mData = new ArrayList<MyApps>();
	private ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		// �첽����JSON
		HTTPUtils.getVolley(this, Constants.URL.APPS, new VolleyListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
			}

			@Override
			public void onResponse(String arg0) {
				Type type = new TypeToken<ArrayList<MyApps>>() {
				}.getType();
				// ����JSON
				ArrayList<MyApps> data = JSONUtils.parseJSONArray(arg0, type);
				mData.addAll(data);
				adapter.notifyDataSetChanged();
				mProgressBar.setVisibility(View.GONE);
				// TODO ��ʼ��task��û�г�ʼ�����ػ��ָ��
				tasks = new DownloadTask[mData.size()];
				handler.post(runnable);
			}
		});
		findViewById(R.id.img_back).setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.listView_download);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		adapter = new DownloadListAdapter(this);
		mListView.setAdapter(adapter);
	}

	public void updateDownload(int viewPos, DownloadTask mgr) {
		View convertView = adapter.getView(viewPos, mListView, null);
		// ProgressBar pb = (ProgressBar) convertView
		// .findViewById(R.id.progressBar);

		// pb.setProgress((int) mgr.getDownloadPercent());

		TextView view = (TextView) convertView
				.findViewById(R.id.progress_text_view);
		view.setText(Utils.size(mgr.getDownloadSize()) + "/"
				+ Utils.size(mgr.getTotalSize()));

		// Log.i(TAG,viewPos + " " + (int) tasks[viewPos].getDownloadPercent());
	}

	public synchronized void startDownload(int viewPos) {
		if (!Utils.isSDCardPresent()) {
			Toast.makeText(this, "δ����SD��", Toast.LENGTH_LONG).show();
			return;
		}

		if (!Utils.isSdCardWrittenable()) {
			Toast.makeText(this, "SD�����ܶ�д", Toast.LENGTH_LONG).show();
			return;
		}

		// if (mgr == null) {
		// mgr = new DownloadMgr(this, "umeng", downloadTaskListener);
		// mgr.start();
		// }
		//
		// mgr.post(Utils.url[viewPos]);

		File file = new File(Utils.APK_ROOT
				+ Utils.getFileNameFromUrl(mData.get(viewPos).appurl));
		if (file.exists())
			file.delete();
		try {
			Log.e("", "mData.get(viewPos).appurl" + mData.get(viewPos).appurl);
			Log.e("", "tasks[viewPos]=" + tasks[viewPos]);
			tasks[viewPos] = new DownloadTask(this, mData.get(viewPos).appurl,
					Utils.APK_ROOT, downloadTaskListener);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		tasks[viewPos].execute();

		// if (!Utils.isSDCardPresent()) {
		// Toast.makeText(this, "δ����SD��", Toast.LENGTH_LONG);
		// return;
		// }
		//
		// if (!Utils.isSdCardWrittenable()) {
		// Toast.makeText(this, "SD�����ܶ�д", Toast.LENGTH_LONG);
		// return;
		// }
		//
		// if (tasks[viewPos] != null) {
		// tasks[viewPos].pause();
		// }
		//
		// File file = new File(Utils.APK_ROOT +
		// Utils.getFileNameFromUrl(Utils.url[viewPos]));
		// if (file.exists()) file.delete();
		// // ���¼�¼
		//
		// tasks[viewPos] = new DownloadMgr(
		// Utils.url[viewPos],
		// Utils.APK_ROOT+Utils.getFileNameFromUrl(Utils.url[viewPos]),
		// getSharedPreferences(TAG,Context.MODE_PRIVATE),
		// downloadListener);
		// tasks[viewPos].clearDownloadRecord();
		// tasks[viewPos].start();
	}

	public synchronized void pauseDownload(int viewPos) {
		if (tasks[viewPos] != null) {
			tasks[viewPos].onCancelled();
		}

		// if (mgr != null) {
		// mgr.pause();
		// }
	}

	public synchronized void stopDownload(int viewPos) {
		File file = new File(Utils.APK_ROOT
				+ Utils.getFileNameFromUrl(mData.get(viewPos).appurl));
		if (file.exists())
			file.delete();

		if (tasks[viewPos] != null) {
			tasks[viewPos].onCancelled();
			// tasks[viewPos].pause();
		}
		tasks[viewPos] = null;
		//TODO ȡ������
		//�������ʾ��Ϊ��ʼֵ
		TextView tvProgress = (TextView) adapter.viewList.get(viewPos)
				.findViewById(R.id.progress_text_view);
		tvProgress.setText(mData.get(viewPos).size);
	}

	public synchronized void continueDownload(int viewPos) {
		// tasks[viewPos].start();
		// startDownload(viewPos);
		tasks[viewPos] = null;
		try {
			tasks[viewPos] = new DownloadTask(this, mData.get(viewPos).appurl,
					Utils.APK_ROOT, downloadTaskListener);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		tasks[viewPos].execute();
	}

	public void installAPK(int viewPos) {
		if (tasks[viewPos] != null) {
			tasks[viewPos] = null;
		}
		Utils.installAPK(DownloadActivity.this, mData.get(viewPos).appurl);

		// Intent intent = new Intent(FileDownloadActivity.this,
		// ImageActivity.class);
		// intent.putExtra("url", viewPos);
		// startActivity(intent);
	}

	private class DownloadListAdapter extends BaseAdapter {
		private Context context;
		public List<View> viewList = new ArrayList<View>();

		public DownloadListAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position < viewList.size()) {
				View view = viewList.get(position);
				if (view != null) {
					return view;
				}
			}

			// if (convertView == null) {
			convertView = View.inflate(this.context,
					R.layout.item_download_list, null);
			viewList.add(convertView);
			ImageView imgIcon = (ImageView) convertView
					.findViewById(R.id.img_icon);
			TextView tvAppName = (TextView) convertView
					.findViewById(R.id.tv_appName);
			TextView tvAppSize = (TextView) convertView
					.findViewById(R.id.progress_text_view);
			Button btnStart = (Button) convertView.findViewById(R.id.btn_start);
			Button btnPause = (Button) convertView.findViewById(R.id.btn_pause);
			Button btnStop = (Button) convertView.findViewById(R.id.btn_stop);
			Button btnContinue = (Button) convertView
					.findViewById(R.id.btn_continue);
			MyApps apps = mData.get(position);
			tvAppName.setText(apps.name);
			tvAppSize.setText(apps.size);
			UILUtils.displayImage(DownloadActivity.this, apps.imgurl, imgIcon);
			btnStart.setVisibility(0);
			btnPause.setVisibility(8);
			btnStop.setVisibility(8);
			btnContinue.setVisibility(8);

			btnStart.setOnClickListener(new BtnListener(position));
			btnPause.setOnClickListener(new BtnListener(position));
			btnStop.setOnClickListener(new BtnListener(position));
			btnContinue.setOnClickListener(new BtnListener(position));
			// }

			return convertView;
		}

		private class BtnListener implements View.OnClickListener {
			int viewPos;

			public BtnListener(int pos) {
				this.viewPos = pos;
			}

			@Override
			public void onClick(View v) {
				Message message;
				switch (v.getId()) {
				case R.id.btn_continue: {
					message = new Message();
					message.what = MSG_CONTINUE_DOWNLOAD;
					message.arg1 = viewPos;
					handler.sendMessage(message);

					Button btnStart = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_start);
					Button btnPause = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_pause);
					Button btnStop = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_stop);
					Button btnContinue = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_continue);

					// ���ð�ť�ؼ��Ŀɼ��� 0 �ɼ�4 ��ռλ���ɼ� ��8 ռλ���ɼ�
					btnStart.setVisibility(8);
					btnPause.setVisibility(0);
					btnStop.setVisibility(8);
					btnContinue.setVisibility(8);
				}

					break;
				case R.id.btn_pause: {
					message = new Message();
					message.what = MSG_PAUSE_DOWNLOAD;
					message.arg1 = viewPos;
					handler.sendMessage(message);

					Button btnStart = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_start);
					Button btnPause = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_pause);
					Button btnStop = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_stop);
					Button btnContinue = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_continue);

					btnStart.setVisibility(8);
					btnPause.setVisibility(8);
					btnStop.setVisibility(8);
					btnContinue.setVisibility(0);
				}
					break;
				case R.id.btn_start: {
					message = new Message();
					message.what = MSG_START_DOWNLOAD;
					message.arg1 = viewPos;
					handler.sendMessage(message);
					Button btnStart = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_start);
					Button btnPause = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_pause);
					Button btnStop = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_stop);
					Button btnContinue = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_continue);

					btnStart.setVisibility(8);
					btnPause.setVisibility(0);
					btnStop.setVisibility(8);
					btnContinue.setVisibility(8);
				}
					break;
				case R.id.btn_stop: {
					Toast.makeText(DownloadActivity.this, "������ȡ��",
							Toast.LENGTH_SHORT).show();
					message = new Message();
					message.what = MSG_STOP_DOWNLOAD;
					message.arg1 = viewPos;
					handler.sendMessage(message);
					Button btnStart = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_start);
					Button btnPause = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_pause);
					Button btnStop = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_stop);
					Button btnContinue = (Button) viewList.get(viewPos)
							.findViewById(R.id.btn_continue);
					btnStart.setVisibility(0);
					btnPause.setVisibility(8);
					btnStop.setVisibility(8);
					btnContinue.setVisibility(8);
				}
					break;
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;

		default:
			break;
		}
	}*/

	/*class MyApps {
		String name;
		String size;
		String imgurl;
		String appurl;
	}*/

}
