package com.zxx.cookbook.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zxx.cookbook.Constants;
import com.zxx.cookbook.R;
import com.zxx.cookbook.bean.Food;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private Food food;

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
        titleText.setText(food.getFoodName());
        Glide.with(this).load(food.getImage().getUrl()).into(foodImg);
        foodPractice.setText(food.getPractice());
        foodMaterial.setText(food.getMaterial());
        foodEffect.setText(food.getEffect());
    }

    @Override
    public void initData() {

    }


    @OnClick(R.id.title_left)
    public void onViewClicked() {
        finish();
    }
}
