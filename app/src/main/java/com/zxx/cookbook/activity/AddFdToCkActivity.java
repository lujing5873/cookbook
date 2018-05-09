package com.zxx.cookbook.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxx.cookbook.Constants;
import com.zxx.cookbook.R;
import com.zxx.cookbook.adapter.FoodManagerAdapter;
import com.zxx.cookbook.bean.CookBook;
import com.zxx.cookbook.bean.Food;
import com.zxx.cookbook.interfaces.IRecyclerViewItemClick;
import com.zxx.cookbook.widget.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddFdToCkActivity extends BaseActivity implements IRecyclerViewItemClick {
    @BindView(R.id.add_fdtock_recycler)
    EmptyRecyclerView addFdtockRecycler;
    @BindView(R.id.id_empty_view)
    RelativeLayout idEmptyView;
    @BindView(R.id.title_text)
    TextView titleText;
    private CookBook cookBook;
    private List<Food> mList;
    private FoodManagerAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_fdtock;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        cookBook = (CookBook) getIntent().getSerializableExtra(Constants.COOKBOOK);
        if (cookBook == null) {
            return;
        }
        titleText.setText(cookBook.getCookbookName());
        mList = new ArrayList<>();
        mAdapter = new FoodManagerAdapter(this, mList, this);
        mAdapter.setManager(true);
        addFdtockRecycler.setEmptyView(idEmptyView);
        addFdtockRecycler.setLayoutManager(new LinearLayoutManager(this));
        addFdtockRecycler.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        queryData();
    }

    @Override
    public void onItemClick(View view, int position) {
        mList.get(position).setCheck(!mList.get(position).isCheck());
        mAdapter.notifyItemChanged(position);
    }


    @OnClick(R.id.add_fdtock_ok)
    public void onViewClicked() {
        BmobRelation relation = new BmobRelation();
        for (Food food : mList) {
            if (food.isCheck()) {
                relation.add(food);
            }
        }
        if(relation.getObjects().size()==0){
            showShortToast("请选择至少一项");
            return;
        }
        cookBook.setFood(relation);
        cookBook.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    showShortToast("添加成功");
                    finish();
                } else {
                    showShortToast("添加失败,请重试");
                }
            }
        });
    }

    private void queryData() {
        Observable.create(new ObservableOnSubscribe<List<Food>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Food>> emitter) throws Exception {
                BmobQuery<Food> query = new BmobQuery<>();
                //返回50条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(500);
                //执行查询方法
                query.findObjects(new FindListener<Food>() {
                    @Override
                    public void done(final List<Food> foodList, BmobException e) {
                        if (e == null) {
                            BmobQuery<Food> query2 = new BmobQuery<>();
                            query2.addWhereRelatedTo("food", new BmobPointer(cookBook));
                            query2.findObjects(new FindListener<Food>() {
                                @Override
                                public void done(List<Food> object, BmobException e) {
                                    //查询关联的条目数
                                    if (e == null) {  //删除已关联food  需要双重for循环对比 效率太低放弃
                                        foodList.removeAll(object);
                                        emitter.onNext(foodList);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Food>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Food> cookBooks) {
                        mList.clear();
                        mList.addAll(cookBooks);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }


    @OnClick(R.id.title_left)
    public void onLeftClicked() {
        finish();
    }
}
