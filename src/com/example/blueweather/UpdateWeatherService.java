package com.example.blueweather;

import com.example.blueweather.util.Utility;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class UpdateWeatherService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("SUN", "Service onStartCommand()");
		updatWeather();
		
		updateOnTime();
		
		return super.onStartCommand(intent, flags, startId);
	}

	private void updatWeather() {
		SharedPreferences sp = getSharedPreferences("BlueWeather",MODE_PRIVATE);
		String weatherCode = sp.getString("weatherCode", "");
		String url = "http://www.weather.com.cn/adat/cityinfo/" + weatherCode + ".html";
		
		new HttpUtils().send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				Utility.handleWeatherResult(UpdateWeatherService.this, result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				
			}
		});
	}

	private void updateOnTime() {
		AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		int delayedTime = 8 * 60 * 60 * 1000;
		
		long triggerAtMillis = SystemClock.elapsedRealtime() + delayedTime;
		
		Intent i = new Intent(this, UpdateWeatherReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
