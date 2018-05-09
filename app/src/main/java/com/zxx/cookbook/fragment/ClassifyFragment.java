package com.zxx.cookbook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxx.cookbook.Constants;
import com.zxx.cookbook.R;
import com.zxx.cookbook.activity.CookbookActivity;
import com.zxx.cookbook.adapter.CookbookAdapter;
import com.zxx.cookbook.bean.CookBook;
import com.zxx.cookbook.bean.Food;
import com.zxx.cookbook.interfaces.IRecyclerViewItemClick;
import com.zxx.cookbook.widget.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MDM on 2018/5/8.
 */

public class ClassifyFragment extends BaseFragment implements IRecyclerViewItemClick{

    @BindView(R.id.classify_rv)
    EmptyRecyclerView classifyRv;
    @BindView(R.id.id_empty_view)
    View emptyView;
    private CookbookAdapter mAdapter;
    private List<CookBook> mList;
    private int size;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_classify;
    }

    @Override
    public void initView() {
        mList=new ArrayList<>();
        mAdapter=new CookbookAdapter(activity,mList,this);
        classifyRv.setEmptyView(emptyView);
        classifyRv.setLayoutManager(new GridLayoutManager(activity,2));
        classifyRv.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        queryData();
    }

    @Override
    public void initLisenter() {

    }

    private void queryData(){
        io.reactivex.Observable.create(new ObservableOnSubscribe<List<CookBook>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<CookBook>> emitter) throws Exception {
                BmobQuery<CookBook> bookBmobQuery=new BmobQuery<>();
                bookBmobQuery.setLimit(50);
                bookBmobQuery.findObjects(new FindListener<CookBook>() {
                    @Override
                    public void done(final List<CookBook> list, BmobException e) {
                        //查询到Cookbook集合
                        size=list.size(); //获取条目数
                        for(final CookBook cookBook:list){
                            BmobQuery<Food> query = new BmobQuery<>();
                            query.addWhereRelatedTo("food", new BmobPointer(cookBook));
                            query.findObjects(new FindListener<Food>() {
                                @Override
                                public void done(List<Food> object,BmobException e) {
                                    //查询关联的条目数
                                    size--;
                                    if(e==null){
                                        cookBook.setNumber(object.size());
                                    }
                                    if(size==0){ //如果查完了  发射
                                        emitter.onNext(list);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CookBook>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<CookBook> cookBooks) {
                        mList.clear();
                        mList.addAll(cookBooks);
                        mAdapter.notifyDataSetChanged();
                        System.out.println(cookBooks.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    @Override
    public void onItemClick(View view, int position) {
        CookBook cookBook=mList.get(position);
        startActivity(new Intent(activity, CookbookActivity.class).putExtra(Constants.COOKBOOK,cookBook));
    }
}
