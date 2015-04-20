package com.example.banglageoname;

import com.example.banglageoname.service.ServiceLocationChangeNotify;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;
import android.os.Build;

public class SplashScreenActivity extends Activity implements AnimationListener {
	private CountDownTimer countDownTimer;
	TextView txtViewTitle;
	private Animation animBounce;
	private Intent intent;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		initViews();
		creatingSplashScreen();

	}
	
	private void initViews() {
		intent = new Intent(SplashScreenActivity.this, ServiceLocationChangeNotify.class);
		txtViewTitle = (TextView) findViewById(R.id.txt_view_title);
		animBounce = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
		animBounce.setAnimationListener(this);
		
		
	}

	private void creatingSplashScreen() {
		countDownTimer = new CountDownTimer(3500, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				txtViewTitle.setVisibility(View.VISIBLE);
				txtViewTitle.startAnimation(animBounce);
			}
			
			@Override
			public void onFinish() {
				startService(intent);
				startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
				finish();				
			}
		};
		
		countDownTimer.start();
	}
	
	@Override
	public void onBackPressed() {
		if (countDownTimer != null) {
			countDownTimer.cancel();
		}
		super.onBackPressed();
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
		

}
