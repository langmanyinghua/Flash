package com.mmm.flash.bean;

/**
 * Created by 浪漫樱花 on 2018/6/7.
 */
public class GameOption {
    private int id; // id
    private String name; // 标题
    private String amount;// 金额 分
    private String memo; // 副标题

    public GameOption() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public GameOption(int id, String name, String amount, String memo) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "GameOption{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", memo='" + memo + '\'' +
                '}';
    }
}
