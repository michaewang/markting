package com.mars.markting.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.an.cityselect.City;
import com.an.cityselect.CityConstant;
import com.an.cityselect.CitySelectActivity;
import com.mars.markting.R;
import com.mars.markting.activity.GoodsListActivity;
import com.mars.markting.bean.CategoryMenu.CategoryItem;
import com.mars.markting.bean.CategoryMenu.CategoryItem.Menu;
import com.mars.markting.utils.ListViewForScrollView;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class FilterMenuFragment extends Fragment implements OnClickListener,
		OnCheckedChangeListener {

	// private ArrayList<String> mTypes = new ArrayList<String>();
	private View layout;
	private FilterListAdapter mAdapter;
	private ToggleButton mTgBtnSendJD; // ��������
	private ToggleButton mTgBtnReadLeftOnly; // �����л�
	private ToggleButton mTgBtnCOD; // ��������

	private boolean isSendJD; // �Ƿ񾩶�����
	private boolean isReadLeftOnly; // �Ƿ�����л�
	private boolean isCOD; // �Ƿ��������
	private CategoryItem categoryItem;
	private List<Menu> menu;

	private City city;
	private TextView mTvSendCity; // ��ַ
	private int selectedPosition;	//����˵���position
	
	private HashMap<Integer, String> resultMap = new HashMap<Integer, String>();	//�洢ѡ����

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		initFilterTypes();
		layout = inflater.inflate(R.layout.fragment_filter_menu, container,
				false);
		initView();
		setOnListener();
		initListView();
		return layout;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == CityConstant.RESULT_CITY
				&& requestCode == CityConstant.REQUEST_CITY) {
			City resultCity = data.getParcelableExtra(CityConstant.CITY_CODE);
			if (resultCity.getProvince().equals("")) {
				// ���û��ѡ��ʡ�ݣ��򱣳�ԭ���ĵ�ַ
				return;
			}
			city = resultCity;
			mTvSendCity.setText(city.getProvince() + city.getCity()
					+ city.getDistrict());
		}
	}

	private void initView() {
		mTgBtnSendJD = (ToggleButton) layout
				.findViewById(R.id.tgbtn_filter_send_jd);
		mTgBtnReadLeftOnly = (ToggleButton) layout
				.findViewById(R.id.tgbtn_filter_read_left_only);
		mTgBtnCOD = (ToggleButton) layout.findViewById(R.id.tgbtn_filter_cod);
		mTvSendCity = (TextView) layout.findViewById(R.id.tv_send_city);
		//���ñ���ĵ�ַ
		SharedPreferences sp = getActivity().getSharedPreferences(
				"baidumap_location", Context.MODE_PRIVATE);
		String location = sp.getString("location", "������");
		mTvSendCity.setText(location);
	}

	private void setOnListener() {
		mTgBtnSendJD.setOnCheckedChangeListener(this);
		mTgBtnReadLeftOnly.setOnCheckedChangeListener(this);
		mTgBtnCOD.setOnCheckedChangeListener(this);
		layout.findViewById(R.id.tv_ok).setOnClickListener(this);
		layout.findViewById(R.id.tv_cancel).setOnClickListener(this);
		layout.findViewById(R.id.btn_reset_menu).setOnClickListener(this);
		layout.findViewById(R.id.layout_destination).setOnClickListener(this);
	}

	/**
	 * ��ʼ������ѡ����
	 */
	private void initListView() {
		ListViewForScrollView listView = (ListViewForScrollView) layout
				.findViewById(R.id.listView_filter_menu);
		mAdapter = new FilterListAdapter();
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				selectedPosition = position;
				addToStack(position, menu.get(position).getMenuname());
			}
		});
	}

	/**
	 * ��ʼ���������
	 */
	private void initFilterTypes() {
		menu = categoryItem.getMenu();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ok:
		case R.id.tv_cancel:
			((GoodsListActivity) getActivity()).toggleFilterMenu();
			break;
		case R.id.btn_reset_menu: // ���ù��˲˵�
			resetMenu();
			break;
		case R.id.layout_destination: // ����ѡ��
			selectCity();
			break;

		default:
			break;
		}
	}

	/**
	 * ����ѡ��
	 */
	private void selectCity() {
		Intent in = new Intent(getActivity(), CitySelectActivity.class);
		in.putExtra(CityConstant.CITY_CODE, city);
		startActivityForResult(in, CityConstant.REQUEST_CITY);
		getActivity().overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO
		switch (buttonView.getId()) {
		case R.id.tgbtn_filter_send_jd: // ��������
			isSendJD = !isSendJD;
			break;
		case R.id.tgbtn_filter_read_left_only: // �����л�
			isReadLeftOnly = !isReadLeftOnly;
			break;
		case R.id.tgbtn_filter_cod: // ��������
			isCOD = !isCOD;
			break;

		default:
			break;
		}
	}

	/**
	 * Ƭ����ӵ�ջ
	 * 
	 * @param
	 * @param
	 */
	private void addToStack(int position, String title) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		// ջ���뵯������ ����Ҫд��add()����֮ǰ
		ft.setCustomAnimations(R.anim.slide_in_right, 0, 0,
				R.anim.slide_out_right);
		FilterMenuFragment2 menuFragment = new FilterMenuFragment2();
		menuFragment.setMenu(menu.get(position), title, position);
		ft.add(R.id.menu_container, menuFragment);
		// ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
	}

	/**
	 * ���ù��˲˵�
	 */
	private void resetMenu() {
		mTgBtnSendJD.setChecked(false);
		mTgBtnCOD.setChecked(false);
		mTgBtnReadLeftOnly.setChecked(false);
		resultMap.clear();
		mAdapter.notifyDataSetChanged();
	}

	class FilterListAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflate = getActivity().getLayoutInflater().inflate(
					R.layout.item_fragment_filter_menu_list, null);
			TextView tvType = (TextView) inflate.findViewById(R.id.tv_type);
			TextView tvSelectedResult = (TextView) inflate
					.findViewById(R.id.tv_selected_result);
			String result = resultMap.get(position);
			if (result != null) {
				if (result.equals("ȫ��")) {
					tvSelectedResult.setTextColor(Color.BLACK);
				} else {
					tvSelectedResult.setTextColor(Color.RED);
				}
				tvSelectedResult.setText(result);
			} else {
				tvSelectedResult.setTextColor(Color.BLACK);
				tvSelectedResult.setText("ȫ��");
			}
			tvType.setText(menu.get(position).getMenuname());
			return inflate;
		}

		@Override
		public int getCount() {
			return menu.size();
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

	/**
	 * ����˵�
	 * 
	 * @param categoryItem
	 */
	public void setCategoryItem(CategoryItem categoryItem) {
		this.categoryItem = categoryItem;
	}

	public void setSelectedResult(String result) {
		resultMap.put(selectedPosition, result);
		mAdapter.notifyDataSetChanged();
	}

}
