package com.example.blueweather.model;

public class BlueInfo {

	public WeatherInfo weatherinfo;

	public class WeatherInfo {
		public String city;
		public String cityid;
		public String temp1;
		public String temp2;
		public String ptime;
		public String weather;

		@Override
		public String toString() {
			return "WeatherInfo [city=" + city + ", cityid=" + cityid
					+ ", temp1=" + temp1 + ", temp2=" + temp2 + ", ptime="
					+ ptime + ", weather=" + weather + "]";
		}
	}

	@Override
	public String toString() {
		return "BlueInfo [weatherinfo=" + weatherinfo + "]";
	}

}
