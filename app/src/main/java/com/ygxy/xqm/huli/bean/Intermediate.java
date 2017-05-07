package com.ygxy.xqm.huli.bean;

/**
 * Created by XQM on 2017/4/28.
 */

public class Intermediate {
    private String number;
    private int url1;
    private int url2;

    public Intermediate(String number,int url1,int url2){
        this.number = number;
        this.url1 = url1;
        this.url2 =  url2;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getUrl1() {
        return url1;
    }

    public void setUrl1(int url1) {
        this.url1 = url1;
    }

    public int getUrl2() {
        return url2;
    }

    public void setUrl2(int url2) {
        this.url2 = url2;
    }
}
