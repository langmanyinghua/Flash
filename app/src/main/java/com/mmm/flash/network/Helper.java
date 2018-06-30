package com.mmm.flash.network;

import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mmm.flash.AppApplication;
import com.mmm.flash.R;
import com.mmm.flash.bean.GameOption;
import com.mmm.flash.bean.NewsEntity;
import com.mmm.flash.tool.Constants;
import com.mmm.flash.util.APPType;
import com.mmm.flash.util.Constant;
import com.mmm.flash.util.HttpUtil;
import com.mmm.flash.util.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by 浪漫樱花 on 2018/6/7.
 */
public class Helper {
    public static String uuid = null;
    public static String session = null;

    /**
     * 启动接口
     */
    public static void start() {
        RequestParams params = new RequestParams();
        params.put("uuid", uuid);
        params.put("appname", AppApplication.application.getString(R.string.app_name));
        params.put("appsrc", Constant.APPSRC);
        HttpUtil.post(Constant.URI.START, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("response", response.toString() + " ");
                    if (Constant.HttpStatus.SUCCESS == response.getInt("status")) {
                        session = response.getJSONObject("result").getString("session");
                        JSONArray pmArray = response.getJSONObject("result").getJSONArray("pm");
                        Constant.PM.clear();
                        for (int i = 0; i < pmArray.length(); i++) {
                            Constant.PM.add(pmArray.getString(i));
                        }
                        Log.i("PM", Constant.PM + " ");
                        Log.i("session", session + " ");
                        if (!TextUtils.isEmpty(session)) order_game();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取选项
     */
    public static void order_game() {
        RequestParams params = new RequestParams();
        params.put("session", session);
        params.put("appname", AppApplication.application.getString(R.string.app_name));
        params.put("appsrc", Constant.APPSRC);

        if (Constant.apptype == APPType.game) {
            Constant.URI.ORDER_CREATE = Constant.URI.ORDER_CREATE_GAME;
        } else if (Constant.apptype == APPType.comic) {
            Constant.URI.ORDER_CREATE = Constant.URI.ORDER_CREATE_COMIC;
        } else {
            Constant.URI.ORDER_CREATE = Constant.URI.ORDER_CREATE_GAME;
        }

        HttpUtil.get(Constant.URI.ORDER_CREATE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("response", response.toString() + " ");
                    if (Constant.HttpStatus.SUCCESS == response.getInt("status")) {
                        JSONArray resultArray = response.getJSONObject("result").getJSONArray("list");

                        Constant.GameOptionList.clear();
                        for (int i = 0; i < resultArray.length(); i++) {
                            JSONObject result = resultArray.getJSONObject(i);

                            GameOption gameOption = new GameOption();
                            gameOption.setId(result.getInt("id"));
                            gameOption.setName(result.getString("name"));
//                            double amount = 9.9;
//                            try {
//                                amount = Double.parseDouble(result.getString("amount")) / 100;
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            int amount = 9;
                            try {
                                amount = Integer.parseInt(result.getString("amount")) / 100;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            gameOption.setAmount(amount + "");
                            gameOption.setMemo(result.getString("memo"));
                            Constant.GameOptionList.add(gameOption);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 下单
     *
     * @param id
     */
    public static void order(int id, String pm, final HttpCallBack httpCallBack) {
        RequestParams params = new RequestParams();
        params.put("session", session);
        params.put("id", id);
        params.put("pm", pm);
        if (Constant.apptype == APPType.game) {
            params.put("action", "game");
        } else if (Constant.apptype == APPType.comic) {
            params.put("action", "comic");
        } else {
            params.put("action", "game");
        }

        params.put("appname", AppApplication.application.getString(R.string.app_name));
        params.put("appsrc", Constant.APPSRC);
        Log.i(" session ", session + "    " + id);
        HttpUtil.post(Constant.URI.ORDER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("onSuccess response", response.toString() + " ");
                    if (Constant.HttpStatus.SUCCESS == response.getInt("status")) {
                        Constant.ORDER_ID = response.getJSONObject("result").getString("order_id");
                        httpCallBack.callback(response.getJSONObject("result").getString("url"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("onFailure response", responseString.toString() + " ");
            }
        });
    }

    /**
     * 查询订单状态
     *
     * @param
     */
    public static void order_status(final HttpCallBack httpCallBack) {
        if (TextUtils.isEmpty(Constant.ORDER_ID)) return;

        RequestParams params = new RequestParams();
        params.put("session", session);
        params.put("order_id", Constant.ORDER_ID);
        params.put("appname", AppApplication.application.getString(R.string.app_name));
        params.put("appsrc", Constant.APPSRC);
        HttpUtil.get(Constant.URI.ORDER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("response", response.toString() + " ");
                    if (Constant.HttpStatus.SUCCESS == response.getInt("status")) {
                        if ("paid".equals(response.getJSONObject("result").getString("status"))) {
                            PreferenceUtils.setPrefBoolean(AppApplication.application, Constant.IS_PAY, true);
                            Constant.ORDER_ID = "";
                            httpCallBack.callback(null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 反馈
     *
     * @param contact 电话
     * @param content 内容
     */
    public static void feedback(String contact, String content, final HttpCallBack httpCallBack) {
        RequestParams params = new RequestParams();
        params.put("session", session);
        params.put("contact", contact);
        params.put("content", content);
        params.put("appname", AppApplication.application.getString(R.string.app_name));
        params.put("appsrc", Constant.APPSRC);
        HttpUtil.post(Constant.URI.FEEDBACK, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("response", response.toString() + " ");
                    if (Constant.HttpStatus.SUCCESS == response.getInt("status")) {
                    }
                    httpCallBack.callback(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void news() {
        HttpUtil.get("http://is.snssdk.com/api/news/feed/v46/?category=news_entertainment&count=100", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("response", response.toString() + " ");
                    if ("success".equals(response.getString("message"))) {
                        JSONArray jsonArray = response.getJSONArray("data");

                        Constants.newsList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String json = jsonObject.getString("content");
                            Log.i("json", json);

                            JSONObject jsonObjectContent = new JSONObject(json);
                            if (!jsonObjectContent.has("title")) continue;

                            NewsEntity newsEntity = new NewsEntity();
                            newsEntity.setId(i);
                            newsEntity.setNewsId(i);
                            newsEntity.setTitle(jsonObjectContent.getString("title"));
                            if (jsonObjectContent.has("comment_count")) {
                                newsEntity.setCommentNum(jsonObjectContent.getInt("comment_count"));
                            }
                            if (jsonObjectContent.has("source")) {
                                newsEntity.setSource(jsonObjectContent.getString("source"));
                            }

                            if (jsonObjectContent.has("image_list")) {
                                List<String> urls = new ArrayList<String>();
                                JSONArray image_list = jsonObjectContent.getJSONArray("image_list");
                                for (int k = 0; k < image_list.length(); k++) {
                                    JSONObject imagejson = image_list.getJSONObject(k);
                                    urls.add(imagejson.getString("url"));
                                }
                                newsEntity.setPicList(urls);
                            }

                            newsEntity.setNewsCategoryId(1);
                            Constants.newsList.add(newsEntity);

                            if (json.length() > 3000) {
                                for (int j = 0; j < json.length(); j += 3000) {
                                    if (j + 3000 < json.length())
                                        Log.i("rescounter" + j, json.substring(j, j + 3000));
                                    else
                                        Log.i("rescounter" + j, json.substring(j, json.length()));
                                }
                            } else {
                                Log.i("resinfo", json);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                {
                    Log.i("onFailure", responseString.toString() + " ");
                }
            }
        });
    }
}
