package com.example.banglageoname.utils;

import com.example.banglageoname.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

public class DialogController {

	private Activity activity;
	private Dialog dialogEnterEmail;
	
	public DialogController(Activity activity) {
		this.activity = activity;
	}
	
	
	public Dialog EnterEmailAddress() {
		dialogEnterEmail = new Dialog(this.activity);
		dialogEnterEmail.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogEnterEmail.setContentView(R.layout.popup_enter_email);
		dialogEnterEmail.setCanceledOnTouchOutside(false);
		dialogEnterEmail.setCancelable(true);
		dialogEnterEmail.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialogEnterEmail.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		return dialogEnterEmail;
	}

	
	
}
