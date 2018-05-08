package com.zxx.cookbook.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxx.cookbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MDM on 2018/5/8.
 */

public class ClassifyFragment extends BaseFragment {
    @BindView(R.id.classify_tv)
    TextView classifyTv;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initLisenter() {

    }
}
