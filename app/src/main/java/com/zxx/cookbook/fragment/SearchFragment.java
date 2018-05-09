package com.zxx.cookbook.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxx.cookbook.R;
import com.zxx.cookbook.adapter.SearchAdpater;
import com.zxx.cookbook.bean.Food;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MDM on 2018/5/8.
 */

public class SearchFragment extends BaseFragment {
    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;
    private SearchAdpater adpater;
    private List<Food> foods = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void initView() {
        searchRecycler.setLayoutManager(new LinearLayoutManager(activity));
        adpater=new SearchAdpater(activity,foods);
        searchRecycler.setAdapter(adpater);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initLisenter() {
        adpater.setOnItemClickLinsentener(new SearchAdpater.OnItemClickLisentener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }


}
