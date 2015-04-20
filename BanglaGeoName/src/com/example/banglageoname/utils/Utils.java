package com.example.banglageoname.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Utils {
	public static String saveBitmap(Bitmap bitmap) {
		String filePath = Environment.getExternalStorageDirectory()
				+ File.separator + "BanglaGeoNames";
		File FileImagePath = new File(filePath);
		if (FileImagePath.isDirectory()) {
		} else {
			FileImagePath.mkdirs();
		}

		String fullPath = Environment.getExternalStorageDirectory()
				+ File.separator + "BanglaGeoNames/screenshot"
				+ System.currentTimeMillis() + ".png";
		File FileFullImagePath = new File(fullPath);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(FileFullImagePath);
			bitmap.compress(CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			return fullPath;
		} catch (FileNotFoundException e) {
			Log.e("GREC", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("GREC", e.getMessage(), e);
		}
		return "";
	}

}
