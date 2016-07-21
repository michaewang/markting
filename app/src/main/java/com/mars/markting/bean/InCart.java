package com.mars.markting.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "in_cart")
public class InCart extends Model implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3584698829991048916L;
	@Column
	String goodsId;	//id
	@Column
	String goodsName;	//
	@Column
	String goodsIcon;	//
	@Column
	String goodsType;	//
	@Column
	double goodsPrice;	//
	@Column
	String goodsPercent;	//
	@Column
	int goodsComment;	//
	@Column
	int isPhone;	//
	@Column
	int isFavor;	//
	@Column
	int num;	//
	
	public InCart(){}

	public InCart(String goodsId, String goodsName, String goodsIcon,
				  String goodsType, double goodsPrice, String goodsPercent,
				  int goodsComment, int isPhone, int isFavor, int num) {
		super();
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.goodsIcon = goodsIcon;
		this.goodsType = goodsType;
		this.goodsPrice = goodsPrice;
		this.goodsPercent = goodsPercent;
		this.goodsComment = goodsComment;
		this.isPhone = isPhone;
		this.isFavor = isFavor;
		this.num = num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + goodsComment;
		result = prime * result
				+ ((goodsIcon == null) ? 0 : goodsIcon.hashCode());
		result = prime * result + ((goodsId == null) ? 0 : goodsId.hashCode());
		result = prime * result
				+ ((goodsName == null) ? 0 : goodsName.hashCode());
		result = prime * result
				+ ((goodsPercent == null) ? 0 : goodsPercent.hashCode());
		long temp;
		temp = Double.doubleToLongBits(goodsPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((goodsType == null) ? 0 : goodsType.hashCode());
		result = prime * result + isFavor;
		result = prime * result + isPhone;
		result = prime * result + num;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		InCart other = (InCart) obj;
		if (goodsComment != other.goodsComment)
			return false;
		if (goodsIcon == null) {
			if (other.goodsIcon != null)
				return false;
		} else if (!goodsIcon.equals(other.goodsIcon))
			return false;
		if (goodsId == null) {
			if (other.goodsId != null)
				return false;
		} else if (!goodsId.equals(other.goodsId))
			return false;
		if (goodsName == null) {
			if (other.goodsName != null)
				return false;
		} else if (!goodsName.equals(other.goodsName))
			return false;
		if (goodsPercent == null) {
			if (other.goodsPercent != null)
				return false;
		} else if (!goodsPercent.equals(other.goodsPercent))
			return false;
		if (Double.doubleToLongBits(goodsPrice) != Double
				.doubleToLongBits(other.goodsPrice))
			return false;
		if (goodsType == null) {
			if (other.goodsType != null)
				return false;
		} else if (!goodsType.equals(other.goodsType))
			return false;
		if (isFavor != other.isFavor)
			return false;
		if (isPhone != other.isPhone)
			return false;
		if (num != other.num)
			return false;
		return true;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsIcon() {
		return goodsIcon;
	}

	public void setGoodsIcon(String goodsIcon) {
		this.goodsIcon = goodsIcon;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getGoodsPercent() {
		return goodsPercent;
	}

	public void setGoodsPercent(String goodsPercent) {
		this.goodsPercent = goodsPercent;
	}

	public int getGoodsComment() {
		return goodsComment;
	}

	public void setGoodsComment(int goodsComment) {
		this.goodsComment = goodsComment;
	}

	public int getIsPhone() {
		return isPhone;
	}

	public void setIsPhone(int isPhone) {
		this.isPhone = isPhone;
	}

	public int getIsFavor() {
		return isFavor;
	}

	public void setIsFavor(int isFavor) {
		this.isFavor = isFavor;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public InCart clone() {
		return new InCart(goodsId, goodsName, goodsIcon, goodsType, goodsPrice, goodsPercent, goodsComment, isPhone, isFavor, num);
	}
	
}
