package com.zxx.cookbook.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zxx.cookbook.Constants;
import com.zxx.cookbook.R;
import com.zxx.cookbook.bean.CookBook;
import com.zxx.cookbook.bean.Food;

import net.bither.util.NativeUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2018/5/8.
 */

public class AddFoodActivity extends BaseActivity {
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.add_img)
    ImageView addImg;
    @BindView(R.id.add_et1)
    EditText addEt1;
    @BindView(R.id.add_et3)
    EditText addEt3;
    @BindView(R.id.add_et4)
    EditText addEt4;
    @BindView(R.id.add_et5)
    EditText addEt5;
    private final int REQUEST_CODE=10001;
    private String imgTmpPath;
    private BmobFile bmobFile;
    private Food food;
    @Override
    public int getLayoutId() {
        return R.layout.activity_add_food;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        titleText.setText("添加食物");
         imgTmpPath=getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/tmp";
        food= (Food) getIntent().getSerializableExtra(Constants.FOOD);
        if(food!=null){ //如果是修改food 设置服务器数据
            bmobFile=food.getImage();
            Glide.with(this).load(food.getImage().getUrl()).into(addImg);
            addEt1.setText(food.getFoodName());
            addEt3.setText(food.getMaterial());
            addEt4.setText(food.getPractice());
            addEt5.setText(food.getEffect());
        }
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.title_left, R.id.add_ok,R.id.add_chooseImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.add_ok:
                String foodName = addEt1.getText().toString();
                String foodMaterial=addEt3.getText().toString();
                String foodPractice=addEt4.getText().toString();
                String foodEffect=addEt5.getText().toString();
                if(bmobFile==null){
                    showShortToast("请选择图片");
                    return;
                }
                if(TextUtils.isEmpty(foodName)){
                    showShortToast("请输入菜名");
                    return;
                }
                if(TextUtils.isEmpty(foodMaterial)){
                    showShortToast("请输入材料");
                    return;
                }
                if(TextUtils.isEmpty(foodPractice)){
                    showShortToast("请输入做法");
                    return;
                }
                if(TextUtils.isEmpty(foodEffect)){
                showShortToast("请输入功效");
                return;
                }
                if(food==null){
                    food=new Food();
                }
                food.setFoodName(foodName);
                food.setPractice(foodPractice);
                food.setMaterial(foodMaterial);
                food.setEffect(foodEffect);
                food.setImage(bmobFile);
                if(TextUtils.isEmpty(food.getObjectId())){
                    food.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                showShortToast("添加食物成功");
                                finish();
                            }else{
                                showShortToast("添加食物失败");
                            }
                        }
                    });
                }else{
                    food.update(new UpdateListener() {
                        @Override
                        public void done( BmobException e) {
                            if(e==null){
                                showShortToast("修改食物成功");
                                finish();
                            }else{
                                showShortToast("修改食物失败");
                            }
                        }
                    });
                }


                break;
            case R.id.add_chooseImg:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK&&requestCode==REQUEST_CODE){
            uploadImg(data.getData());
        }
    }

    private void uploadImg(final Uri res){
        Observable<File> fileObservable=Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(res, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                c.close();
                NativeUtil.compressBitmap(NativeUtil.getBitmapFromFile(imagePath),imgTmpPath);
                emitter.onNext(new File(imgTmpPath));
            }
        });
        fileObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        fileObservable.subscribe(new Observer<File>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(File file) {
                Glide.with(AddFoodActivity.this).load(file).apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy( DiskCacheStrategy.NONE)).into(addImg);
                bmobFile=new BmobFile(file);
                bmobFile.upload(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            //bmobFile.getFileUrl()--返回的上传文件的完整地址
                            showShortToast("上传文件成功" );
                        }else{
                            showShortToast("上传文件失败");
                            bmobFile=null;
                        }
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                bmobFile=null;
                showShortToast("压缩文件失败");
            }
            @Override
            public void onComplete() {
            }
        });

    }
}
