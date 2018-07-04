package com.mmm.flash.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mmm.flash.R;
import com.mmm.flash.base.CommonAdapter;
import com.mmm.flash.base.ViewHolder;
import com.mmm.flash.bean.CommentBean;
import com.mmm.flash.util.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 浪漫樱花 on 2018/6/4.
 */
public class WebActivity extends FragmentActivity {
    private WebView webView;
    private String url = null;
    private List<CommentBean> commentList;
    private ListView comment_list;
    private CommonAdapter commonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webview);

        url = getIntent().getStringExtra("url");
        if (!url.startsWith("http:")) url = "http:" + url;
        Log.i(" weburl  ", url);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);//设置支持缩放
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮

        webView.loadUrl(url);

//        comment_list = findViewById(R.id.comment_list);
//        String commentresult = getIntent().getStringExtra("comment");
//        if (!TextUtils.isEmpty(commentresult)) {
//            commentList = JSON.parseArray(commentresult, CommentBean.class);
//            comment_list.setAdapter(commonAdapter = new CommonAdapter<CommentBean>(this, commentList, R.layout.activity_web_item) {
//                @Override
//                public void convert(ViewHolder helper, CommentBean item) {
//                    helper.setText(R.id.nickname, item.getNickname());
//                    helper.setText(R.id.comment, item.getComment());
//                }
//            });
//
//            setListViewHeightBasedOnChildren(comment_list);
//        }
    }

    public void onClickBack(View view) {
        finish();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}