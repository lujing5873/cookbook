package com.zxx.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zxx.cookbook.Constants;
import com.zxx.cookbook.R;
import com.zxx.cookbook.bean.Food;
import com.zxx.cookbook.bean.Star;
import com.zxx.cookbook.bean.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dell on 2018/5/9.
 */

public class FoodActivity extends BaseActivity {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.food_img)
    ImageView foodImg;
    @BindView(R.id.food_material)
    TextView foodMaterial;
    @BindView(R.id.food_practice)
    TextView foodPractice;
    @BindView(R.id.food_effect)
    TextView foodEffect;
    @BindView(R.id.title_right)
    ImageView right;
    private Food food;
    private boolean isLike;
    private Star mStar;
    private User user;

    @Override
    public int getLayoutId() {
        return R.layout.activity_food;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        food = (Food) getIntent().getSerializableExtra(Constants.FOOD);
        if (food == null) {
            return;
        }
        user = BmobUser.getCurrentUser(User.class);
        right.setVisibility(View.VISIBLE);
        titleText.setText(food.getFoodName());
        Glide.with(this).load(food.getImage().getUrl()).into(foodImg);
        foodPractice.setText(food.getPractice());
        foodMaterial.setText(food.getMaterial());
        foodEffect.setText(food.getEffect());
    }

    @Override
    public void initData() {
        requestData();
    }

    /**
     * 查询是否收藏
     */
    private void requestData() {
        BmobQuery<Star> query = new BmobQuery<>();
//查询playerName叫“比目”的数据
        query.addWhereEqualTo("food", food);
        query.addWhereEqualTo("user", user);
//返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(1);
//执行查询方法
        query.findObjects(new FindListener<Star>() {
            @Override
            public void done(List<Star> object, BmobException e) {
                if (e == null) {
                    mStar = object.get(0);
                    right.setImageResource(R.mipmap.like);
                    isLike = true;
                } else {
                    right.setImageResource(R.mipmap.unlike);
                    isLike = false;
                }
            }
        });
    }

    @OnClick(R.id.title_left)
    public void onViewClicked() {
        finish();
    }


    @OnClick(R.id.title_right)
    public void onRightClicked() {
        if (isLike) {
            mStar.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        showShortToast("取消收藏");
                        right.setImageResource(R.mipmap.unlike);
                        isLike = false;
                    }
                }
            });
        } else {
            mStar = new Star();
            mStar.setFood(food);
            mStar.setUser(user);
            mStar.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        showShortToast("添加收藏");
                        right.setImageResource(R.mipmap.like);
                        isLike=true;
                    }
                }
            });

        }
    }
}
