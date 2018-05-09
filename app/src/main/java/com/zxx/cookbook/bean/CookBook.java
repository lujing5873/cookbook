package com.zxx.cookbook.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by dell on 2018/5/8.
 */

public class CookBook extends BmobObject {
    private String cookbookName;
    private BmobRelation food;
    private BmobFile cookbookImage;
    public String getCookbookName() {
        return cookbookName;
    }

    public void setCookbookName(String cookbookName) {
        this.cookbookName = cookbookName;
    }

    public BmobRelation getFood() {
        return food;
    }

    public void setFood(BmobRelation food) {
        this.food = food;
    }

    public BmobFile getCookbookImage() {
        return cookbookImage;
    }

    public void setCookbookImage(BmobFile cookbookImage) {
        this.cookbookImage = cookbookImage;
    }
}
