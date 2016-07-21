package com.mars.markting.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.mars.markting.R;
import com.mars.markting.activity.GoodsListActivity;
import com.mars.markting.bean.BrandInfo;
import com.mars.markting.bean.CategoryMenu.CategoryItem.Menu;
import com.mars.markting.bean.CategoryMenu.CategoryItem.Menu.MenuItem;
import com.mars.markting.utils.MySideBar;
import com.mars.markting.utils.MySideBar.OnTouchingLetterChangedListener;
import com.mars.markting.utils.PinyinUtils;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class FilterMenuFragment2 extends Fragment implements OnClickListener,
		OnTouchingLetterChangedListener {

	private ArrayList<String> list = new ArrayList<String>();
	private List<BrandInfo> brandInfos;
	private int position;
	private String title;
	private View layout;
	private FilterListAdapter mAdapter;
	private ListView mListView;
	private boolean isSortByLetters;
	private MySideBar mMySideBar;
	private View mLayoutSortBy;
	private Menu menu;
	private TextView mTvRecom;
	private TextView mTvSort;
	private View mIndicator;
	private int lastIndex; // ���ڼ�¼�ϴα�����ı����ǩ
	private int width; // �˵�һ��Ŀ��
	private final long duration = 300; // ����ʱ��

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		initData();
		layout = inflater.inflate(R.layout.fragment_filter_menu2, container,
				false);
		initListView();
		initViews();
		if (position == 0) {
			initSider();
		} else {
			mLayoutSortBy.setVisibility(View.GONE);
		}
		return layout;
	}

	// ����ָʾ����λ�úͳ���
	@Override
	public void onResume() {
		super.onResume();
		mLayoutSortBy.postDelayed(new Runnable() {

			@Override
			public void run() {
				width = mLayoutSortBy.getWidth() / 4;
				LayoutParams params = (LayoutParams) mIndicator
						.getLayoutParams();
				params.width = width;
				params.leftMargin = width / 2;
				mIndicator.setLayoutParams(params);
			}
		}, 50);
	}

	/**
	 * indicator����
	 * 
	 * @param index
	 */
	private void moveIndicator(int index) {
		int width2 = width * 2;
		ObjectAnimator
				.ofFloat(mIndicator, "translationX",
						lastIndex * width2, index * width2)
				.setDuration(duration).start();
		Log.e("moveIndicator", "lastIndex="+lastIndex +"index=" +index +"width="+width +"width2="+width2);
	}

	private void initViews() {
		TextView tvTitle = (TextView) layout
				.findViewById(R.id.tv_filter_menu2_title);
		tvTitle.setText(title);
		mIndicator = layout.findViewById(R.id.indicator);
		mLayoutSortBy = layout.findViewById(R.id.layout_sort_by);
		mTvRecom = (TextView) layout.findViewById(R.id.tv_recom_brand);
		mTvSort = (TextView) layout.findViewById(R.id.tv_sort_by_letter);
		mTvRecom.setOnClickListener(this);
		mTvSort.setOnClickListener(this);
		layout.findViewById(R.id.img_back).setOnClickListener(this);
	}

	private void initListView() {
		mListView = (ListView) layout.findViewById(R.id.listView_filter_menu2);
		mAdapter = new FilterListAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String result = "";
				if (isSortByLetters) {
					result = brandInfos.get(position).getBrand();
				} else {
					result = list.get(position);
				}
				((GoodsListActivity) getActivity()).setSelectedResult(result);
				ViewHolder holder = (ViewHolder) view.getTag();
				holder.tvListItems.setTextColor(Color.RED);
				ImageView imgSelected = (ImageView) view
						.findViewById(R.id.img_selected);
				imgSelected.setVisibility(View.VISIBLE);
				imgSelected.postDelayed(new Runnable() {

					@Override
					public void run() {
						popFragment();
					}
				}, 100);
			}
		});
	}

	private void initData() {
		List<MenuItem> menuitem = menu.getMenuitem();
		for (int i = 0; i < menuitem.size(); i++) {
			list.add(menuitem.get(i).getItem());
		}
	}

	private void initSider() {
		mMySideBar = (MySideBar) layout.findViewById(R.id.mySideBar1);
		mMySideBar.setOnTouchingLetterChangedListener(this);
	}

	/**
	 * ����ĸ����
	 */
	private void sortByLetters() {
		if (lastIndex == 1) {
			// ������ε��ͬһ����ǩ����������
			return;
		}
		moveIndicator(1);
		mTvRecom.setTextColor(Color.BLACK);
		mTvSort.setTextColor(Color.RED);
		isSortByLetters = true;
		mMySideBar.setVisibility(View.VISIBLE);
		BrandInfo[] brandInfoArray = new BrandInfo[list.size() + 1];
		// ʹ��һ����"��ͷ������ʱ��������ǰ��
		brandInfoArray[0] = new BrandInfo("ȫ��", "\"");
		for (int i = 1; i < brandInfoArray.length; i++) {
			String brand = list.get(i - 1);
			brandInfoArray[i] = new BrandInfo(brand,
					PinyinUtils.getAlpha(brand));
		}
		Arrays.sort(brandInfoArray, new Comparator<BrandInfo>() {
			public int compare(BrandInfo lhs, BrandInfo rhs) {
				return lhs.getPy().compareTo(rhs.getPy());
			}
		});
		brandInfos = Arrays.asList(brandInfoArray);
		mAdapter.notifyDataSetChanged();
		lastIndex = 1;
	}

	/**
	 * �Ƽ�Ʒ��
	 */
	private void recomBrand() {
		if (lastIndex == 0) {
			// ������ε��ͬһ����ǩ����������
			return;
		}
		moveIndicator(0);
		mTvRecom.setTextColor(Color.RED);
		mTvSort.setTextColor(Color.BLACK);
		isSortByLetters = false;
		mMySideBar.setVisibility(View.GONE);
		mAdapter.notifyDataSetChanged();
		lastIndex = 0;
	}

	/**
	 * �������ĸ�仯ʱ
	 */
	@Override
	public void onTouchingLetterChanged(String s) {
		// �ı�ListViewѡ��λ��
		int position = findIndex(s);
		if (position >= 0) {
			mListView.setSelection(position);
		}
	}

	/**
	 * �������в�����������ĸ��ͷ����
	 * 
	 * @param s
	 * @return
	 */
	private int findIndex(String s) {
		for (int i = 0; i < brandInfos.size(); i++) {
			BrandInfo info = brandInfos.get(i);
			if (info.getPy().startsWith(s)) {
				return i;
			}

		}
		return -1;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_recom_brand: // �Ƽ�Ʒ��
			recomBrand();
			break;
		case R.id.tv_sort_by_letter: // ����ĸ����
			sortByLetters();
			break;
		case R.id.img_back: // ����ǰƬ�ε���
			popFragment();
			break;

		default:
			break;
		}
	}

	/**
	 * ����ǰƬ�ε���ջ
	 */
	private void popFragment() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		fm.popBackStack();
	}

	class FilterListAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflate = null;
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				inflate = getActivity().getLayoutInflater().inflate(
						R.layout.item_fragment_filter_menu2_list, null);
				holder.tvListItems = (TextView) inflate
						.findViewById(R.id.tv_items);
				holder.imgSelected = (ImageView) inflate
						.findViewById(R.id.img_selected);
				holder.tvCatalog = (TextView) inflate
						.findViewById(R.id.tv_catalog);
				holder.catalog = inflate.findViewById(R.id.layout_catalog);
				inflate.setTag(holder);
			} else {
				inflate = convertView;
				holder = (ViewHolder) inflate.getTag();
			}
			holder.catalog.setVisibility(View.VISIBLE);
			if (isSortByLetters) {
				if (position == 0) {
					holder.catalog.setVisibility(View.GONE);
				}
				BrandInfo brandInfo = brandInfos.get(position);
				char charAt = brandInfo.getPy().charAt(0);
				String mLastChar = "";
				if (position > 0) {
					BrandInfo brandInfoLast = brandInfos.get(position - 1);
					char charAt2 = brandInfoLast.getPy().charAt(0);
					mLastChar = "" + charAt2;
				}
				if (("" + charAt).equals(mLastChar)) {
					holder.catalog.setVisibility(View.GONE);
				} else {
					mLastChar = "" + charAt;
					// �������ĸ����ʾ#
					if (Character.isLetter(charAt)) {
						holder.tvCatalog.setText("" + charAt);
					} else {
						holder.tvCatalog.setText("#");
					}
				}
				holder.tvListItems.setText(brandInfo.getBrand());
			} else {
				holder.catalog.setVisibility(View.GONE);
				holder.tvListItems.setText(list.get(position));
			}
			return inflate;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	class ViewHolder {
		TextView tvCatalog;
		TextView tvListItems;
		ImageView imgSelected;
		View catalog;
	}

	public void setMenu(Menu menu, String title, int position) {
		this.menu = menu;
		this.title = title;
		this.position = position;
	}
}
