package com.mars.markting.utils;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.mars.markting.bean.GoodsInfo;
import com.mars.markting.bean.InCart;

import java.util.List;

public final class DBUtils {
	public static void delete(GoodsInfo info) {
		new Delete()
				.from(GoodsInfo.class)
				.where("goodsId=? And isFavor=?", info.getGoodsId(),
						info.getIsFavor()).executeSingle();
	}

	/**
	 * �����ʷ
	 */
	public static void deleteHistory() {
		new Delete().from(GoodsInfo.class).where("isFavor=0").execute();
	}

	public static void deleteCart(InCart cart) {
		new Delete().from(InCart.class).where("goodsId=?", cart.getGoodsId())
				.executeSingle();
	}

	/**
	 * �ж��Ƿ����ղ�
	 * 
	 * @param info mInfo
	 * @return
	 */
	public static boolean hasFavor(GoodsInfo info) {
		GoodsInfo info2 = new Select().from(GoodsInfo.class)
				.where("goodsId=? And isFavor=?", info.getGoodsId(), 1)
				.executeSingle();

		// if (info2 == null){
		// return false;
		// }else{
		// return true;
		// }
		return info2 != null;
	}

	/**
	 * �ж��Ƿ��Ѽ��빺�ﳵ
	 * 
	 * @param cart
	 * @return
	 */
	public static boolean hasInCart(InCart cart) {
		InCart info2 = new Select().from(InCart.class)
				.where("goodsId=?", cart.getGoodsId()).executeSingle();

		// if (info2 == null){
		// return false;
		// }else{
		// return true;
		// }
		return info2 != null;
	}

	public static void save(GoodsInfo info) {
		info.save();
	}

	public static void saveInCart(InCart cart) {
		cart.save();
	}

	public static List<GoodsInfo> getFavor() {
		return new Select().from(GoodsInfo.class).where("isFavor=?", 1)
				.execute();
	}

	public static List<GoodsInfo> getHistory() {
		return new Select().from(GoodsInfo.class).where("isFavor=?", 0)
				.execute();
	}

	public static List<InCart> getInCart() {
		return new Select().from(InCart.class).execute();
	}

	public static int getInCartNum() {
		int num = 0;
		List<InCart> execute = new Select().from(InCart.class).execute();
		for (int i = 0; i < execute.size(); i++) {
			num = num + execute.get(i).getNum();
		}
		return num;
	}

	private DBUtils() {
	}
}
