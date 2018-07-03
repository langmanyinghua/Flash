package com.mmm.flash.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mmm.flash.R;
import com.mmm.flash.activity.WebActivity;
import com.mmm.flash.adapter.NewsAdapter;
import com.mmm.flash.bean.NewsEntity;
import com.mmm.flash.network.Helper;
import com.mmm.flash.network.HttpCallBack;
import com.mmm.flash.tool.Constants;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    Activity activity;
    ArrayList<NewsEntity> newsList = new ArrayList<NewsEntity>();
    ListView mListView;
    NewsAdapter mAdapter;
    String text;
    ImageView detail_loading;
    public final static int SET_NEWSLIST = 0;

    public int last_index;
    public int total_index;
    public boolean isLoading = false;//表示是否正处于加载状态
    private int page = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Bundle args = getArguments();
        text = args != null ? args.getString("text") : "";
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = activity;
        super.onAttach(activity);
    }

    /**
     * 此方法意思为fragment是否可见 ,可见时候加载数据
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            //fragment可见时加载数据
            handler.obtainMessage(SET_NEWSLIST).sendToTarget();
        } else {
            //fragment不可见时不执行操作
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.news_fragment, null);
        mListView = (ListView) view.findViewById(R.id.mListView);
        TextView item_textview = (TextView) view.findViewById(R.id.item_textview);
        detail_loading = (ImageView) view.findViewById(R.id.detail_loading);
        item_textview.setText(text);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initEvent();
        mAdapter = new NewsAdapter(activity, newsList);
        mListView.setAdapter(mAdapter);
    }

    public void initEvent() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (last_index == total_index && (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)) {
                    //表示此时需要显示刷新视图界面进行新数据的加载(要等滑动停止)
                    if (!isLoading) {
                        //不处于加载状态的话对其进行加载
                        isLoading = true;
                        //设置刷新界面可见
                        page++;
                        loadData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                last_index = firstVisibleItem + visibleItemCount;
                total_index = totalItemCount;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().startActivity(new Intent(getActivity(), WebActivity.class).putExtra("url", newsList.get(position).getUrl()));
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SET_NEWSLIST) {
                detail_loading.setVisibility(View.GONE);
                loadData();
            }
        }
    };


    public void loadData() {
        Helper.news(text, new HttpCallBack() {
            @Override
            public void callback(Object object) {
                newsList = (ArrayList<NewsEntity>) object;
                mAdapter.setNewsList(newsList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
