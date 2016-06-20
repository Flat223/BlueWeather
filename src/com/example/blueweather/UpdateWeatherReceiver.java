package com.example.blueweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UpdateWeatherReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("SUN", "Receiver onReceive()");
		Intent mIntent = new Intent(context, UpdateWeatherService.class);
		context.startService(mIntent);
	}
}
