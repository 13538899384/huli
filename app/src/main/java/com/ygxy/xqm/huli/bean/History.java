package com.ygxy.xqm.huli.bean;

/**
 * PK记录
 * Created by XQM on 2017/3/30.
 */

public class History {
    private String history_name;
    private String history_referee1;
    private String history_referee2;
    private String history_grade1;
    private String history_grade2;
    private String history_result;

    public History(String history_name, String history_referee1, String history_referee2, String history_grade1, String history_grade2, String history_result) {
        this.history_name = history_name;
        this.history_referee1 = history_referee1;
        this.history_referee2 = history_referee2;
        this.history_grade1 = history_grade1;
        this.history_grade2 = history_grade2;
        this.history_result = history_result;
    }

    public String getHistory_name() {
        return history_name;
    }

    public void setHistory_name(String history_name) {
        this.history_name = history_name;
    }

    public String getHistory_referee1() {
        return history_referee1;
    }

    public void setHistory_referee1(String history_referee1) {
        this.history_referee1 = history_referee1;
    }

    public String getHistory_referee2() {
        return history_referee2;
    }

    public void setHistory_referee2(String history_referee2) {
        this.history_referee2 = history_referee2;
    }

    public String getHistory_grade1() {
        return history_grade1;
    }

    public void setHistory_grade1(String history_grade1) {
        this.history_grade1 = history_grade1;
    }

    public String getHistory_grade2() {
        return history_grade2;
    }

    public void setHistory_grade2(String history_grade2) {
        this.history_grade2 = history_grade2;
    }

    public String getHistory_result() {
        return history_result;
    }

    public void setHistory_result(String history_result) {
        this.history_result = history_result;
    }
}
