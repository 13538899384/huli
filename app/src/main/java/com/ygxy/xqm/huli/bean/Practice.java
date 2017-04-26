package com.ygxy.xqm.huli.bean;

/**
 * Created by XQM on 2017/3/17.
 */

public class Practice {
    private String name;
    private String level;
    private int imageId;

    public Practice(String name, String level, int imageId) {
        this.name = name;
        this.level = level;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
