package com.mmm.flash.util;

import android.widget.Toast;

import com.mmm.flash.AppApplication;

public class ToastUtil {
	private static Toast toast;
	public static void showToast(String str) {
		if (toast == null) {
			toast = Toast.makeText(AppApplication.application, str, Toast.LENGTH_SHORT);
		} else {
			toast.setText(str);
		}
		toast.show();
	}

}
