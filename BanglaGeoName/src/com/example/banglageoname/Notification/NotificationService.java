package com.example.banglageoname.Notification;

import com.example.banglageoname.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class NotificationService extends Service {
	private NotificationManager mManager;
	private boolean isIDFround = false;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		String notificationMessage = "";
		Intent intent1 = new Intent(this.getApplicationContext(), com.example.banglageoname.HomeActivity.class);
		
		if(intent!=null){
			if(intent.getExtras()!= null){
				if( null == intent.getStringExtra("NOTIFICATION_TITLE")){
					notificationMessage = "New Co-ordinates Found!";
				} else{
					//notificationMessage = "New Co-ordinates "+intent.getStringExtra("EVENT_TITLE")+" Found!";
					notificationMessage = "New Co-ordinates Found!";
				}
			}else{
				notificationMessage = "New Co-ordinates Found!";
			}
		} else {
			notificationMessage = "New Co-ordinates Found!";
		}
		
	    mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
	      
	     
	       Notification notification = new Notification(R.drawable.map, "New Geo-Coordinates Found!", System.currentTimeMillis());
	       intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 
	       PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
	       notification.flags |= Notification.FLAG_AUTO_CANCEL;
	       notification.setLatestEventInfo(this.getApplicationContext(), "Bangla GeoNames ", notificationMessage, pendingNotificationIntent);
	 
	       mManager.notify(0, notification);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}