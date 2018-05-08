package com.zxx.cookbook.bean;

import cn.bmob.v3.BmobObject;

/**
 * 收藏表
 */

public class Star extends BmobObject {
    private User user;
    private Food food;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
