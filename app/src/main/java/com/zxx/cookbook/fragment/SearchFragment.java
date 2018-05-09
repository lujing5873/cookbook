package com.zxx.cookbook.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.zxx.cookbook.R;
import com.zxx.cookbook.adapter.FoodAdapter;
import com.zxx.cookbook.bean.Food;
import com.zxx.cookbook.interfaces.IRecyclerViewItemClick;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by MDM on 2018/5/8.
 */

public class SearchFragment extends BaseFragment implements IRecyclerViewItemClick {
    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;
    @BindView(R.id.search_et1)
    EditText mSearch;
    private FoodAdapter adpater;
    private List<Food> foods = new ArrayList<>();
    private PublishSubject<String> publishSubject;
    private List<Food> allFoods;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void initView() {
        searchRecycler.setLayoutManager(new LinearLayoutManager(activity));
        adpater = new FoodAdapter(activity, foods,this);
        searchRecycler.setAdapter(adpater);
    }

    @Override
    public void initData() {
        allFoods = new ArrayList<>();
        final BmobQuery<Food> query = new BmobQuery<>();
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<Food>() {
            @Override
            public void done(List<Food> object, BmobException e) {
                if (e == null) {
                    allFoods = object;
                    foods.clear();
                    foods.addAll(allFoods);
                    adpater.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void initLisenter() {
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = s.toString();
                publishSubject.onNext(search);
            }
        });
        publishSubject = PublishSubject.create();
        publishSubject.debounce(200, TimeUnit.MILLISECONDS).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return true;
            }
        }).switchMap(new Function<String, ObservableSource<List<Food>>>() {
            @Override
            public ObservableSource<List<Food>> apply(String s) throws Exception {
                return getSearchObservable(s);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Food>>() {
                    @Override
                    public void onNext(List<Food> foods) {
                        SearchFragment.this.foods.clear();
                        SearchFragment.this.foods.addAll(foods);
                        adpater.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<List<Food>> getSearchObservable(final String queryNmae) {
        return Observable.create(new ObservableOnSubscribe<List<Food>>() {

            @Override
            public void subscribe(final ObservableEmitter<List<Food>> observableEmitter) throws Exception {
                if (TextUtils.isEmpty(queryNmae) && allFoods != null) {
                    observableEmitter.onNext(allFoods);
                    observableEmitter.onComplete();
                    return;
                }
                final BmobQuery<Food> query = new BmobQuery<>();
                query.addWhereEqualTo("foodName", queryNmae);
                //返回50条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(500);
                //执行查询方法
                query.findObjects(new FindListener<Food>() {
                    @Override
                    public void done(List<Food> object, BmobException e) {
                        if (e == null) {
                            observableEmitter.onNext(object);
                            observableEmitter.onComplete();
                        } else {
                            observableEmitter.onComplete();
                        }
                    }
                });

            }
        }).subscribeOn(Schedulers.io());
    }



    @OnClick(R.id.search_text)
    public void onViewClicked() {
        publishSubject.onNext(mSearch.getText().toString());
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
