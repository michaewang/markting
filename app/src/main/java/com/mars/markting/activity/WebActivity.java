package com.mars.markting.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mars.markting.R;

public class WebActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		findViewById(R.id.img_back).setOnClickListener(this);
		initWebView();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		//��ַ
		int direction = getIntent().getIntExtra("direction",0);
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		WebView mWebView = (WebView) findViewById(R.id.webView1);
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		String url = "";
		switch (direction) {
		case 1:
			tvTitle.setText("��ֵ����");
			url = "http://vm.m.jd.com/chongzhi/index.action?v=t&sid=";
			break;
		case 2:
			tvTitle.setText("��Ϸ��ֵ");
			url = "http://m.jd.com/ware/search.action?sid=&keyword=%E6%B8%B8%E6%88%8F%E5%85%85%E5%80%BC";
			break;
		case 3:
			tvTitle.setText("��ӰƱ");
			url = "http://movie.m.jd.com";
			break;
		case 4:
			tvTitle.setText("�����쾩��");
			url = "http://m.jd.com/ware/search.action?sid=&keyword=%E4%BA%AC%E8%B1%86";
			break;
		case 5:
			tvTitle.setText("���ķ���");
			url = "http://life.jd.com/";
			break;
		case 6:
			tvTitle.setText("С��");
			url = "http://m.weibo.cn/n/%E5%B0%8F%E5%86%B0";
			break;
		case 7:
			tvTitle.setText("����");
			url = "http://m.weibo.cn/u/1740522895";
			break;
		case 8:
			tvTitle.setText("���Ż");
			url = "http://sale.jd.com/m/act/Q1xhRXqwuJZfbj.html";
			break;
		case 9:
			tvTitle.setText("�ҵ�ԤԼ");
			url = "https://passport.m.jd.com/user/login.action";
			break;
		case 10:
			tvTitle.setText("����ܼ�");
			url = "https://passport.m.jd.com/user/login.action";
			break;
		case 11:
			tvTitle.setText("������Ʒ");
			url = "https://passport.m.jd.com/user/login.action";
			break;
		case 12:
			tvTitle.setText("�˻��밲ȫ");
			url = "https://passport.m.jd.com/user/login.action";
			break;
			
		default:
			break;
		}
		mWebView.loadUrl(url);
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				progressBar.setProgress(newProgress);
				if(progressBar.getProgress() == 100) {
					progressBar.setVisibility(View.GONE);
				}
			}
		});
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
	}

}
