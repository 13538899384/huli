package com.ygxy.xqm.huli.bean;

import org.litepal.crud.DataSupport;

/**
 * 用户实体类
 * Created by XQM on 2017/3/17.
 */

public class User extends DataSupport{
    private String id;
    private String studentNumber;//学号
    private String phone;//手机号
    private String nickname;//账号
    private String password;//密码
    private String repeatPassword;
//    private String head_portrtait;//头像
    private int gold;//金币数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
