package com.zxx.cookbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxx.cookbook.Constants;
import com.zxx.cookbook.R;
import com.zxx.cookbook.adapter.FoodManagerAdapter;
import com.zxx.cookbook.adapter.ListPopupWindowAdapter;
import com.zxx.cookbook.bean.CookBook;
import com.zxx.cookbook.bean.Food;
import com.zxx.cookbook.bean.User;
import com.zxx.cookbook.interfaces.IRecyclerViewItemClick;
import com.zxx.cookbook.widget.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by dell on 2018/5/9.
 */

public class CookbookActivity extends BaseActivity implements IRecyclerViewItemClick {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_right)
    ImageView right;
    @BindView(R.id.cookbook_rv)
    EmptyRecyclerView cookbookRv;
    @BindView(R.id.cookbook_ok)
    Button okButton;
    @BindView(R.id.id_empty_view)
    View emptyView;
    private int operateType;
    private CookBook cookBook;
    private List<Food> mList;
    private FoodManagerAdapter mAdapter;
    private ListPopupWindow listPopupWindow;
    @Override
    public int getLayoutId() {
        return R.layout.activity_cookbook;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        cookBook = (CookBook) getIntent().getSerializableExtra(Constants.COOKBOOK);
        if (cookBook == null) {
            return;
        }
        User bmobUser = BmobUser.getCurrentUser(User.class);
        if(bmobUser != null){
           int level=bmobUser.getUserLevel();
           if(level==1){
               right.setVisibility(View.VISIBLE);
               right.setImageResource(R.mipmap.more);
           }
        }
        right.setVisibility(View.VISIBLE);
        right.setImageResource(R.mipmap.more);

        titleText.setText(cookBook.getCookbookName());
        mList = new ArrayList<>();
        mAdapter = new FoodManagerAdapter(this, mList, this);
        cookbookRv.setEmptyView(emptyView);
        cookbookRv.setLayoutManager(new LinearLayoutManager(this));
        cookbookRv.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        queryData();
    }

    private void queryData() {
        BmobQuery<Food> query = new BmobQuery<>();
        query.addWhereRelatedTo("food", new BmobPointer(cookBook));
        query.findObjects(new FindListener<Food>() {
            @Override
            public void done(List<Food> object, BmobException e) {
                if (e == null) {
                    mList.clear();
                    mList.addAll(object);
                    mAdapter.notifyDataSetChanged();
                }
            }

        });
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (operateType){
            case Constants.DELETE:
                mList.get(position).setCheck(!mList.get(position).isCheck());
                mAdapter.notifyItemChanged(position);
                break;
            case Constants.EDIT:
                startActivity(new Intent(this,AddFoodActivity.class).putExtra(Constants.FOOD,mList.get(position)));
                operateType=0; //设为默认值
                break;
             default: //普通模式  跳转详情
                 startActivity(new Intent(this,FoodActivity.class).putExtra(Constants.FOOD,mList.get(position)));
              break;
        }
    }

    @OnClick(R.id.title_left)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.title_right)
    public void onRightClicked() {
        if(mAdapter.isManager()){ //再次点击关闭管理模式
            mAdapter.setManager(false);
            okButton.setVisibility(View.GONE);
            return;
        }
        showListPopupWindow(right);
    }

    public void showListPopupWindow(View view) {
        List<String> list=new ArrayList<>();
        list.add("删除");
        list.add("添加");
        list.add("修改");
        listPopupWindow = new ListPopupWindow(this);

        // ListView适配器
        listPopupWindow.setAdapter(
               new ListPopupWindowAdapter(this,list));

        // 选择item的监听事件
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                switch (pos){ //更改至删除页面
                    case 0:
                        operateType=Constants.DELETE;
                        break;
                    case 1:
                         //跳转添加页面
                        listPopupWindow.dismiss();
                        startActivity(new Intent(CookbookActivity.this,AddFdToCkActivity.class).putExtra(Constants.COOKBOOK,cookBook));
                        return;
                    case 2: //修改只改变点击item逻辑
                        operateType=Constants.EDIT;
                        listPopupWindow.dismiss();
                        return;
                }
                okButton.setVisibility(View.VISIBLE);
                mAdapter.setManager(true);
                listPopupWindow.dismiss();
            }
        });

        // 对话框的宽高
        listPopupWindow.setWidth(300);
//        listPopupWindow.setHeight(600);

        // ListPopupWindow的锚,弹出框的位置是相对当前View的位置
        listPopupWindow.setAnchorView(view);
        // ListPopupWindow 距锚view的距离
        listPopupWindow.setHorizontalOffset(-10);
//        listPopupWindow.setVerticalOffset(-100);

        listPopupWindow.setModal(false);

        listPopupWindow.show();
    }

}
