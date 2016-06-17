package com.example.blueweather.model;

public class City {
	private int id;
	private String name;
	private String code;
	private int provinceId;
	
	public City() {
		super();
	}

	public City(int id, String name, String code, int provinceId) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.provinceId = provinceId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", name=" + name + ", code=" + code
				+ ", provinceId=" + provinceId + "]";
	}
}
