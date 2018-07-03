package com.mmm.flash.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mmm.flash.bean.CommentBean;
import com.mmm.flash.bean.NewsEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 浪漫樱花 on 2018/6/23.
 */
public class CommUtil {

    /**
     * 截取后面十位
     *
     * @param content
     * @return
     */
    public static String SubContent(String content) {
        if (TextUtils.isEmpty(content) || content.length() <= 10) {
            return content;
        } else {
            return content.substring(content.length() - 10, content.length());
        }
    }

    public static ArrayList<NewsEntity> getNewsData(Context context, String name) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<NewsEntity> list = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(name)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (stringBuilder.length() > 0) {
            try {
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    NewsEntity newsEntity = new NewsEntity();
                    newsEntity.setId(i);
                    newsEntity.setNewsId(i);
                    newsEntity.setUrl(jsonObject.optString("url"));
                    newsEntity.setTitle(jsonObject.optString("title"));
                    newsEntity.setSource(jsonObject.optString("author"));
                    newsEntity.setCommentNum(jsonObject.optInt("commentCount"));
                    newsEntity.setSource(jsonObject.optString("posterScreenName"));

                    if (jsonObject.has("images")) {
                        List<String> urls = new ArrayList<String>();
                        JSONArray imageUrls = jsonObject.optJSONArray("images");
                        if (imageUrls != null && imageUrls.length() > 0) {
                            for (int k = 0; k < imageUrls.length(); k++) {
                                urls.add(imageUrls.getString(k));
                            }
                        }
                        newsEntity.setPicList(urls);
                    }

                    if (jsonObject.has("comments")) {
                        List<CommentBean> commentBeanList = new ArrayList<>();
                        JSONArray commentArray = jsonObject.optJSONArray("comments");
                        if (commentArray != null && commentArray.length() > 0) {
                            for (int k = 0; k < commentArray.length(); k++) {
                                JSONObject json = commentArray.getJSONObject(k);

                                CommentBean comment = new CommentBean();
                                comment.setUid(json.optString("uid"));
                                comment.setNickname(json.optString("nickname"));
                                comment.setComment(json.optString("comment"));
                                comment.setAvatar(json.optString("avatar"));
                                commentBeanList.add(comment);
                            }
                            newsEntity.setCommentList(commentBeanList);
                        }
                    }
                    list.add(newsEntity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
