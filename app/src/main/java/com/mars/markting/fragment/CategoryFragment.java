package com.mars.markting.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.mars.markting.R;
import com.mars.markting.activity.GoodsListActivity;
import com.mars.markting.activity.MainActivity;
import com.mars.markting.activity.SearchActivity;
import com.mars.markting.bean.CategoryMenu;
import com.mars.markting.bean.CategoryMenu.CategoryItem;
import com.mars.markting.utils.Constants;
import com.mars.markting.utils.ListViewForScrollView;
import com.google.gson.reflect.TypeToken;
import com.lib.json.JSONUtils;
import com.lib.uil.UILUtils;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;
import com.mars.markting.utils.MyLog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class CategoryFragment extends Fragment implements OnClickListener {
	private ArrayList<CategoryMenu> menuList = new ArrayList<CategoryMenu>();
	private List<CategoryItem> categoryitem;
	private View layout;
	private CategoryListAdapter mListAdapter;
	private CategoryGridAdapter mGridAdapter;
	private int selectedPosition;
	private ScrollView mScrollView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if (layout != null) {
			// ��ֹ���new��Ƭ�ζ������ͼƬ��������
			return layout;
		}
		initData();
		initGoodData();
		layout = inflater.inflate(R.layout.fragment_category, container, false);
		layout.findViewById(R.id.img_category_search_code).setOnClickListener(
				this);
		layout.findViewById(R.id.layout_category_search).setOnClickListener(this);
		mScrollView = (ScrollView) layout
				.findViewById(R.id.scrollView_category);
		initListView();
		initGridView();
		return layout;
	}

	/**
	 * ����JsonArray��ȡ�˵����
	 */
	private void initData() {
		// �첽����JSON
		HTTPUtils.getVolley(getActivity(), Constants.URL.MENUJSON,
				new VolleyListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}

					@Override
					public void onResponse(String arg0) {
						Log.d("michael", "arg0 = " + arg0);
						Type type = new TypeToken<ArrayList<CategoryMenu>>() {
						}.getType();
						ArrayList<CategoryMenu> list = JSONUtils.parseJSONArray(arg0, type);
						menuList.addAll(list);
						mListAdapter.notifyDataSetChanged();
						mGridAdapter.notifyDataSetChanged();
						layout.findViewById(R.id.progressBar1).setVisibility(View.GONE);

					}
				});
	}

	private void initGoodData() {
		// �첽����JSON

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// ��layout�Ӹ�������Ƴ�
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}

	private void initListView() {
		final ListViewForScrollView listView = (ListViewForScrollView) layout
				.findViewById(R.id.listView_category);
		mListAdapter = new CategoryListAdapter();
		listView.setAdapter(mListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if (selectedPosition == position) {
					return;
				}
				mScrollView.smoothScrollTo(0, position * view.getHeight());
				selectedPosition = position;
				mListAdapter.notifyDataSetChanged();
				mGridAdapter.notifyDataSetChanged();
			}
		});
	}

	private void initGridView() {
		GridView gridView = (GridView) layout.findViewById(R.id.gridView1);
		mGridAdapter = new CategoryGridAdapter();
		gridView.setAdapter(mGridAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO
				Intent intent = new Intent(getActivity(),
						GoodsListActivity.class);
				intent.putExtra(Constants.INTENT_KEY.MENU_TO_GOODS_LIST,
						categoryitem.get(position));
				startActivity(intent);
			}
		});
	}

	class CategoryListAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflate = getActivity().getLayoutInflater().inflate(
					R.layout.item_category_list, null);
			TextView tvCategoryList = (TextView) inflate
					.findViewById(R.id.tv_category_list);
			inflate.setTag(tvCategoryList);
			String categoryType = menuList.get(position).getCategory();
			tvCategoryList.setText(categoryType);
			if (selectedPosition == position) {
				inflate.setBackgroundResource(R.drawable.drawable_common_category_list_s);
				tvCategoryList.setTextColor(getResources()
						.getColor(R.color.red));
			} else {
				inflate.setBackgroundResource(R.drawable.drawable_common_category_list);
				tvCategoryList.setTextColor(getResources().getColor(
						R.color.text));

			}
			return inflate;
		}

		@Override
		public int getCount() {
			return menuList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	class CategoryGridAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflate = null;
			GridViewHolder holder = null;
			if (convertView == null) {
				holder = new GridViewHolder();
				inflate = getActivity().getLayoutInflater().inflate(
						R.layout.item_category_grid, null);
				holder.imgCategoryGrid = (ImageView) inflate
						.findViewById(R.id.img_category_grid);
				holder.tvCategoryGrid = (TextView) inflate
						.findViewById(R.id.tv_category_grid);
				inflate.setTag(holder);
			} else {
				inflate = convertView;
				holder = (GridViewHolder) inflate.getTag();
			}
			holder.tvCategoryGrid.setText(menuList.get(selectedPosition)
					.getCategoryitem().get(position).getTypename());
			UILUtils.displayImage(getActivity(), menuList.get(selectedPosition)
					.getCategoryitem().get(position).getImgurl(), holder.imgCategoryGrid);
			return inflate;
		}

		@Override
		public int getCount() {
			if (menuList.size() > 0) {
				categoryitem = menuList.get(selectedPosition).getCategoryitem();
				return categoryitem.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	class GridViewHolder {
		ImageView imgCategoryGrid;
		TextView tvCategoryGrid;
	}
	
	private void gotoSearch() {
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		startActivity(intent);
		// activity�����޶���
		getActivity().overridePendingTransition(0, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_category_search_code: // ��ά��ɨ��
			((MainActivity) getActivity()).scanQRCode();
			break;
		case R.id.layout_category_search:
			gotoSearch();
			break;

		default:
			break;
		}
	}
}
