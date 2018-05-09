package com.zxx.cookbook.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zxx.cookbook.R;
import com.zxx.cookbook.adapter.ViewPagerAdapter;
import com.zxx.cookbook.fragment.ClassifyFragment;
import com.zxx.cookbook.fragment.CollectFragment;
import com.zxx.cookbook.fragment.MyFragment;
import com.zxx.cookbook.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tab_1)
    RadioButton tab1;
    @BindView(R.id.tab_2)
    RadioButton tab2;
    @BindView(R.id.tab_3)
    RadioButton tab3;
    @BindView(R.id.tab_4)
    RadioButton tab4;
    @BindView(R.id.main_vp)
    ViewPager mainVp;
    @BindView(R.id.title_left)
    ImageView titleLeft;
    @BindView(R.id.title_text)
    TextView titleText;
    private List<Fragment> mList;
    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        titleText.setText(getResources().getString(R.string.app_name));
        titleLeft.setVisibility(View.GONE);
        Drawable drawableFirst = getResources().getDrawable(R.drawable.bottom1_img);
        drawableFirst.setBounds(0, 0, 69, 69);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        tab1.setCompoundDrawables(null, drawableFirst, null, null);//只放上面

        Drawable drawableFirst2 = getResources().getDrawable(R.drawable.bottom2_img);
        drawableFirst2.setBounds(0, 0, 69, 69);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        tab2.setCompoundDrawables(null, drawableFirst2, null, null);//只放上面


        Drawable drawableFirst3 = getResources().getDrawable(R.drawable.bottom3_img);
        drawableFirst3.setBounds(0, 0, 69, 69);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        tab3.setCompoundDrawables(null, drawableFirst3, null, null);//只放上面


        Drawable drawableFirst4 = getResources().getDrawable(R.drawable.bottom4_img);
        drawableFirst4.setBounds(0, 0, 69, 69);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        tab4.setCompoundDrawables(null, drawableFirst4, null, null);//只放上面

        mList = new ArrayList<>();
        Fragment fragment = new ClassifyFragment();
        Fragment fragment2 = new SearchFragment();
        Fragment fragment3 = new CollectFragment();
        Fragment fragment4 = new MyFragment();
        mList.add(fragment);
        mList.add(fragment2);
        mList.add(fragment3);
        mList.add(fragment4);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mList);
        mainVp.setOffscreenPageLimit(3);
        mainVp.setAdapter(mViewPagerAdapter);
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onPageChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {


    }

    public void onPageChange(int position) {
        switch (position) {
            case 0:
                tab1.setChecked(true);
                break;
            case 1:
                tab2.setChecked(true);
                break;
            case 2:
                tab3.setChecked(true);
                break;
            case 3:
                tab4.setChecked(true);
                break;
        }
    }


    @OnClick({R.id.tab_1, R.id.tab_2, R.id.tab_3, R.id.tab_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab_1:
                mainVp.setCurrentItem(0);
                break;
            case R.id.tab_2:
                mainVp.setCurrentItem(1);
                break;
            case R.id.tab_3:
                mainVp.setCurrentItem(2);
                break;
            case R.id.tab_4:
                mainVp.setCurrentItem(3);
                break;
        }
    }

}
