package com.ygxy.xqm.huli.bean;

/**
 * Created by XQM on 2017/4/10.
 */

public class MyNotification{
    private int type;
    private String role;
    private String pkStudent1;
    private String pkStudent2;
    private String referee1;
    private String referee2;
    private String competitor;
    private int score;
    private int result;

    public String getCompetitor() {
        return competitor;
    }

    public void setCompetitor(String competitor) {
        this.competitor = competitor;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPkStudent1() {
        return pkStudent1;
    }

    public void setPkStudent1(String pkStudent1) {
        this.pkStudent1 = pkStudent1;
    }

    public String getPkStudent2() {
        return pkStudent2;
    }

    public void setPkStudent2(String pkStudent2) {
        this.pkStudent2 = pkStudent2;
    }

    public String getReferee1() {
        return referee1;
    }

    public void setReferee1(String referee1) {
        this.referee1 = referee1;
    }

    public String getReferee2() {
        return referee2;
    }

    public void setReferee2(String referee2) {
        this.referee2 = referee2;
    }
}
