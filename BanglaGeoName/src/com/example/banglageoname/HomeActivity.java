package com.example.banglageoname;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.banglageoname.utils.DialogController;
import com.example.banglageoname.utils.Mail;
import com.example.banglageoname.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class HomeActivity extends Activity implements OnClickListener {
	private double mcurrent_lat;
	private double mcurrent_lon;
	private MarkerOptions markerOptions;
	public  GoogleMap googleMap;
	private MapView mapView;
	private Geocoder gcd;
	private EditText edtTxtLat;
	private EditText edtTxtLong;
	private EditText edtTxtGeoName;
	private Button btnSendEmail;
	private String emailAddress = "";
	private Dialog dialogEnterEmail;
	private static final String GMAIL_EMAIL_ID = "ughoraranda@gmail.com";
	private static final String GMAIL_ACCOUNT_PASSWORD = "@u@ghoraranda";
	
	
	private String savelat ="";
	private String savelon = "";
	private String saveadd="";
	private View rootView;
	private String fullPath = "";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initViews();
		initMap();
		setListener();

		
	}
	
	private void setListener() {
		btnSendEmail.setOnClickListener(this);
		
	}

	private void initViews() {
		rootView = findViewById(android.R.id.content).getRootView();
		btnSendEmail =(Button) findViewById(R.id.btn_sendEmail);
		edtTxtLat =(EditText) findViewById(R.id.edt_txt_lat);
		edtTxtLong =(EditText) findViewById(R.id.edt_txt_long);
		edtTxtGeoName =(EditText) findViewById(R.id.edt_txt_name);
	}

	private void initMap() {
		gcd = new Geocoder(this, Locale.getDefault());
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		com.example.banglageoname.utils.GPSTracker gps = new com.example.banglageoname.utils.GPSTracker(this);
		mcurrent_lat = gps.getLatitude();
		mcurrent_lon = gps.getLongitude();
		
		final LatLng cur_Latlng = new LatLng(mcurrent_lat, mcurrent_lon);
		edtTxtLat.setText(""+mcurrent_lat);
		edtTxtLong.setText(""+mcurrent_lon);
		
	
		googleMap.setMyLocationEnabled(true);
		//googleMap.setTrafficEnabled(true);
		//googleMap.set
		
		googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			
			@Override
			public void onCameraChange(CameraPosition arg0) {
				edtTxtLat.setText(""+arg0.target.latitude);
				edtTxtLong.setText(""+arg0.target.longitude);
				new GeoAddressAsyncTask(arg0.target.latitude, arg0.target.longitude).execute();
				Log.i("Value is on MAP", "________" + arg0.target);

			}
			
		});
		
		if(googleMap != null) {
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
			
		} else {
			Log.i("Map is null", "_________________");
		}
		
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
					//String address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();
					String address =  addresses.get(0).getAddressLine(0);

					
//					String address = "";
//					for(int i = 0; i<addresses.get(0).getMaxAddressLineIndex(); i++){
//						if(address.equals("")){
//							address = addresses.get(0).getAddressLine(i);
//						} else {
//							address = address+", "+addresses.get(0).getAddressLine(i);
//						}
//						
//					}
					String addressWithoutNullValue = address.replace("null", "");
					Log.e("Address is 1", "" + addressWithoutNullValue);
					String formatedAddress = "";
					formatedAddress = addressWithoutNullValue.replace(", ,", ",");
					Log.e("Address is 2", "" + formatedAddress);
					if (formatedAddress.length() > 0 && formatedAddress.charAt(formatedAddress.length()-1)==',') {
						formatedAddress = formatedAddress.substring(0, formatedAddress.length()-2);
					}
					

					edtTxtGeoName.setText(formatedAddress);
					//txtView.setText(addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getSubLocality() + ", " + addresses.get(0).getAdminArea());
					
				} else {
					Log.i("Value is null", "______empty__________");
				}
				   
			} else {
				Log.i("Value is null", "________________");
			}
			
		}
	}
	
	
/*	public void sendMail(String path) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
		new String[] { emailAddress });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bangla GeoNames Mail");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
		"This is an autogenerated mail from Bangla GeoNames app");
		emailIntent.setType("image/png");
		Uri myUri = Uri.parse("file://" + path);
		emailIntent.putExtra(Intent.EXTRA_STREAM, myUri);
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	//	dialogEnterEmail.dismiss();
	}*/

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_sendEmail:
			saveadd = edtTxtGeoName.getText().toString();
			savelat = edtTxtLat.getText().toString();
			savelon = edtTxtLong.getText().toString();
			dialogEnterEmail();
			break;
			
			default:
				break;
		}
		
	}

	private void dialogEnterEmail() {
		dialogEnterEmail = new DialogController(HomeActivity.this).EnterEmailAddress();
		
		final EditText edtTxtEmail;
		Button btnSendEmail;
		edtTxtEmail = (EditText) dialogEnterEmail.findViewById(R.id.edt_txt_email);
		btnSendEmail = (Button) dialogEnterEmail.findViewById(R.id.btn_sendEmail);
		btnSendEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(edtTxtEmail.getText().toString().equalsIgnoreCase("")){
					Toast.makeText(HomeActivity.this, "Please enter an email address", Toast.LENGTH_SHORT).show();
				} else {
					dialogEnterEmail.dismiss();
					googleMap.addMarker(new MarkerOptions()
					.position(new LatLng(Double.parseDouble(savelat), Double.parseDouble(savelon)))
					.icon(BitmapDescriptorFactory.defaultMarker())
					.title(saveadd));
					emailAddress = edtTxtEmail.getText().toString();
					
					 googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
				            public void onSnapshotReady(Bitmap bitmap) {
				                // Write image to disk
				                FileOutputStream out = null;
								try {
									out = new FileOutputStream("/mnt/sdcard/map.png");
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								 
				                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				                fullPath = Utils.saveBitmap(bitmap);
				                new GeneratePdf().execute();
				            }
				        });
						
				}
				
			}
				
		});
		
		dialogEnterEmail.show();
		
	}
	
	public final void snapshot (GoogleMap.SnapshotReadyCallback callback){
		
	}
	
	private ProgressDialog progressDialog;
	private class GeneratePdf extends AsyncTask<String, Void, String> {
		View v1;
		Bitmap bm;
		
		@Override
		protected void onPreExecute() {
			
			progressDialog = ProgressDialog.show(HomeActivity.this, "Creating Image", "Please wait...", true, false);
			
		}

		@Override
		protected String doInBackground(String... urls) {
			try {
				
				Mail m = new Mail(GMAIL_EMAIL_ID, GMAIL_ACCOUNT_PASSWORD);
				String toAddresses = emailAddress;
				m.setToAddresses(toAddresses);
				m.setFromAddress(GMAIL_EMAIL_ID);
				m.setMailSubject("Bangla GeoNames");
				m.setMailBody("Co-ordinates: "+ savelat+","+savelon+"\nGeoName:"+saveadd);
				
/*			//	View v1 = findViewById(android.R.id.content).getRootView();
				   
				   View v1 = findViewById(android.R.id.content); //this works too
				// but gives only content
				   v1.setDrawingCacheEnabled(true);
				Bitmap bitmap = v1.getDrawingCache();
				String path = Utils.saveBitmap(bitmap);*/

			
				
				try {
					m.addAttachment(fullPath, "Map_"+saveadd+"_"+savelat+"_"+savelon+".png");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					if (m.send()) {
						new File(fullPath).delete();
						return "1";
					} else {
						new File(fullPath).delete();
						return "2";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				
			return "3";

		}

		@Override
		protected void onPostExecute(String result) {
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			
			if ("1".equals(result)) {
				Toast.makeText(HomeActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
			} else if ("2".equals(result)) {
				Toast.makeText(HomeActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
			} else if ("3".equals(result)) {
				Toast.makeText(HomeActivity.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
			}
			
		}
	}
	
/*	public void captureMapScreen() {
        SnapshotReadyCallback callback = new SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                try {
                	rootView.setDrawingCacheEnabled(true);
                    Bitmap backBitmap = rootView.getDrawingCache();
                    Bitmap bmOverlay = Bitmap.createBitmap(
                            backBitmap.getWidth(), backBitmap.getHeight(),
                            backBitmap.getConfig());
                    Canvas canvas = new Canvas(bmOverlay);
                    canvas.drawBitmap(snapshot, new Matrix(), null);
                    canvas.drawBitmap(backBitmap, 0, 0, null);
                    String filePath = Environment.getExternalStorageDirectory()
            				+ File.separator + "BanglaGeoNames";
            		File FileImagePath = new File(filePath);
            		if (FileImagePath.isDirectory()) {
            		} else {
            			FileImagePath.mkdirs();
            		}

            		fullPath = Environment.getExternalStorageDirectory()
            				+ File.separator + "BanglaGeoNames/screenshot"
            				+ System.currentTimeMillis() + ".png";
                    FileOutputStream out = new FileOutputStream(fullPath);

                    bmOverlay.compress(Bitmap.CompressFormat.PNG, 90, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        googleMap.snapshot(callback);

    }*/
	

}
