package com.mars.markting.utils;

import java.text.DecimalFormat;

public class NumberUtils {
	/**
	 *
	 * @param price
	 * @return
	 */
	public static String formatPrice(double price) {
		DecimalFormat df=new DecimalFormat("0.00");
		String format = "￥ " + df.format(price);	//michael
		return format;
	}
}
