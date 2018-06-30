package com.mmm.flash.util;

import android.app.Dialog;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mmm.flash.R;

public class LoadingDialog extends Dialog {
	private TextView content;
	public LoadingDialog(Context context) {
		super(context, R.style.loadingDialogStyle);
		setContentView(R.layout.dialog_loading);
		this.setCanceledOnTouchOutside(false);
		content = (TextView) findViewById(R.id.dialog_loading_tv);
		LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.dialog_loading_linearlayout);
		linearLayout.getBackground().setAlpha(300);
	}
	public void ShowDialog(){
		this.show();
	}
	
	public void ShowDialog(String content){
		this.content.setText(content+"");
		this.show();
	}
}
