package com.example.blueweather.model;

public class Country {
	private int id;
	private String name;
	private String code;
	private int cityId;
	
	public Country() {
		super();
	}
	public Country(int id, String name, String code, int cityId) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.cityId = cityId;
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
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code
				+ ", cityId=" + cityId + "]";
	}
}
