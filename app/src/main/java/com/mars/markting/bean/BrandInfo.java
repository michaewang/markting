package com.mars.markting.bean;

public class BrandInfo {
	
	public String brand;
	public String py;
	public BrandInfo(String brand, String py) {
		super();
		this.brand = brand;
		this.py = py;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getPy() {
		return py;
	}
	public void setPy(String py) {
		this.py = py;
	}
	@Override
	public String toString() {
		return "BrandInfo [brand=" + brand + ", py=" + py + "]";
	}


}
