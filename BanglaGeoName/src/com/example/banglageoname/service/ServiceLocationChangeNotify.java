package com.example.banglageoname.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.banglageoname.utils.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.model.LatLng;


public class ServiceLocationChangeNotify  extends Service implements LocationListener{
	int counter = 0;
	int count = 0;
	private Intent myIntent;
	private PendingIntent pendingIntent;
	private static final String TAG = "BroadcastService";
	public static final String BROADCAST_ACTION = "com.netcabs.driver.SplashScreenActivity";
	private final Handler handler = new Handler();
	private Intent intent;
	private LatLng current_latLng;
	private Geocoder gcd;
	private String geoAddress = "";
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000*30; // 1 min
	
	@Override
	public void onCreate() {
		super.onCreate();
    	intent = new Intent(BROADCAST_ACTION);
    	myIntent = new Intent(ServiceLocationChangeNotify.this, com.example.banglageoname.Notification.NotificationReceiver.class);
    	gcd = new Geocoder(ServiceLocationChangeNotify.this, Locale.getDefault());
    	initLocationManager();
	}
	
	private void initLocationManager() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
			
		} else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		
		} else {
			Toast.makeText(this, "Please enable location service", Toast.LENGTH_SHORT).show();
		}
		
	} 
	
    @Override
    public void onStart(Intent intent, int startId) {
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
   
    }

    private Runnable sendUpdatesToUI = new Runnable() {
    	public void run() {

    		if(InternetConnectivity.isConnectedToInternet(ServiceLocationChangeNotify.this)) {
    			current_latLng = new LatLng(new GPSTracker(ServiceLocationChangeNotify.this).getLatitude(), new GPSTracker(ServiceLocationChangeNotify.this).getLongitude());
    			Log.i("SERVICE_UPDATE_TAXI", "***** " + current_latLng.latitude + ",,," + current_latLng.longitude);
    		//	new GeoAddressAsyncTask(current_latLng.latitude,current_latLng.longitude).execute();
    			
    			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	        		new GeoAddressAsyncTask(current_latLng.latitude,current_latLng.longitude).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	        	} else {
	        		new GeoAddressAsyncTask(current_latLng.latitude, current_latLng.longitude).execute();
	        	}
		
    		} else {
    			//new CustomToast(ServiceTaxiLocationUpdate.this, ConstantValues.internetConnectionMsg).showToast();
    		}
    	    handler.postDelayed(this, 10000); // 3 seconds
    	}
    };    

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {		
        handler.removeCallbacks(sendUpdatesToUI);		
		super.onDestroy();
	}
	
	
	private class GeoAddressAsyncTask extends AsyncTask<Void, Void, Void> {
		double Lat, Lon;
		List<Address> addresses = null;
		public GeoAddressAsyncTask(double Lat, double Lon) {
			this.Lat = Lat;
			this.Lon = Lon;
		}
	
		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				addresses = gcd.getFromLocation(Lat, Lon, 1);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i("Value found", "________________");
			if (addresses != null) {
				if (addresses.size() > 0) {
					Log.i("Value found", "________________");
					String address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
					String addressWithoutNullValue = address.replace("null", "");
					Log.e("Address is 1", "" + addressWithoutNullValue);
					geoAddress = addressWithoutNullValue.replace(", ,", ",");
					if (geoAddress.length() > 0 && geoAddress.charAt(geoAddress.length()-1)==',') {
						geoAddress = geoAddress.substring(0, geoAddress.length()-2);
					}
					Log.e("Address is 2", "" + geoAddress);
					
					//info.setLocationName(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getSubLocality() + ", " + addresses.get(0).getAdminArea());
				} else {
					Log.i("Value is null", "______empty__________");
				}
			} else {
				Log.i("Value is null", "________________");
				
			}
			
		}
		
	}


	@Override
	public void onLocationChanged(Location location) {
		double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        myIntent.putExtra("NOTIFICATION_TITLE", latitude+","+longitude);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(ServiceLocationChangeNotify.this, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
	    AlarmManager alarmManager = (AlarmManager)getSystemService(ServiceLocationChangeNotify.ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis(), pendingIntent);
	   

        // Zoom in the Google Map
//      /  animateMarker(currentPositionMarker, curenLatLng, false);
    
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	
}
