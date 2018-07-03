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

    public static void news(String kw, final HttpCallBack httpCallBack){
        news(2,kw,httpCallBack);
    }

    public static void news(int page, String kw, final HttpCallBack httpCallBack) {
        String apikey = "eDcffFw9AFkosMsNtGIXMyQUuAS5ZKbxt0QW0oriugdakrErCm07w0q55nuXua4H";
        String url = "http://api01.bitspaceman.com:8000/news/qihoo";
        RequestParams params = new RequestParams();
        params.put("apikey", apikey);
        params.put("pageToken", page);
        params.put("kw", kw);
        params.put("site", "qq.com");
        HttpUtil.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("response", response.toString() + " ");
                    if ("000000".equals(response.getString("retcode"))) {
                        JSONArray jsonArray = response.getJSONArray("data");

                        ArrayList<NewsEntity> list = new ArrayList<NewsEntity>();

                        Constants.newsList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            NewsEntity newsEntity = new NewsEntity();
                            newsEntity.setId(i);
                            newsEntity.setNewsId(i);
                            newsEntity.setUrl(jsonObject.getString("url"));
                            newsEntity.setTitle(jsonObject.getString("title"));
                            newsEntity.setCommentNum(jsonObject.optInt("commentCount"));
                            newsEntity.setSource(jsonObject.optString("posterScreenName"));

                            if (jsonObject.has("imageUrls")) {
                                List<String> urls = new ArrayList<String>();
                                JSONArray imageUrls = jsonObject.optJSONArray("imageUrls");
                                if (imageUrls != null && imageUrls.length() > 0) {
                                    for (int k = 0; k < imageUrls.length(); k++) {
                                        urls.add(imageUrls.getString(k));
                                    }
                                }

                                newsEntity.setPicList(urls);
                                list.add(newsEntity);
                            }

                            httpCallBack.callback(list);
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
