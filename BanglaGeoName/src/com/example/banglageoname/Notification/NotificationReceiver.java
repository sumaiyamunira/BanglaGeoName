package com.example.banglageoname.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
         
		Intent service1 = new Intent(context, NotificationService.class);
		Log.e("title from broadcast receiver", "--"+intent.getStringExtra("NOTIFICATION_TITLE"));
		service1.putExtra("NOTIFICATION_TITLE", intent.getStringExtra("NOTIFICATION_TITLE"));
		context.startService(service1);
	}
}