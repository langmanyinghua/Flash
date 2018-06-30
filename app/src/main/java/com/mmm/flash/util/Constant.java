package com.mmm.flash.util;

import com.mmm.flash.bean.GameOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 浪漫樱花 on 2018/6/7.
 */
public class Constant {
    public static List<GameOption> GameOptionList = new ArrayList<>();
    private static final String IP = "http://api.mailuoli.com/";
    public static String ORDER_ID = "";
    public static final String IS_PAY = "isPay";
    public static final String IS_FIST = "isFirst";
    public static List<String> PM = new ArrayList<>();
    public static final String APPSRC = "5b1925e9dc5c793aaa383250";

    // TODO: 2018/6/10 修改类型 
    public static final APPType apptype = APPType.comic;

    public static class URI {
        public static final String START = IP + "start";                                           // 启动
        public static final String ORDER = IP + "order";                                           // 下单
        public static String ORDER_CREATE = IP + "order/game";                                     // 选项
        public static final String ORDER_CREATE_COMIC = IP + "order/comic";                        // 漫画
        public static final String ORDER_CREATE_GAME = IP + "order/game";                          // 游戏
        public static final String FEEDBACK = IP + "feedback";                                     // 反馈
        public static final String PLAY = IP + "play";                                             // 游戏
    }

    public static class HttpStatus {
        public static final int SUCCESS = 0;
    }
}
