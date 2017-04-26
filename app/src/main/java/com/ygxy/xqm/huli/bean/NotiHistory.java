package com.ygxy.xqm.huli.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by XQM on 2017/4/20.
 */

public class NotiHistory extends DataSupport{
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
