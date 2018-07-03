package com.mmm.flash.bean;

/**
 * Created by 浪漫樱花 on 2018/7/3.
 */
public class CommentBean {
    private String uid;
    private String nickname;
    private String avatar;
    private String comment;

    public CommentBean() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CommentBean(String uid, String nickname, String avatar, String comment) {
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
