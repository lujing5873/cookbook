package com.zxx.cookbook.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    private boolean isUploadSuccess;
    private String imgTmpPath;
    @Override
    public int getLayoutId() {
        return R.layout.activity_add_food;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
         imgTmpPath=getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/tmp";
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
                Food food=new Food();
                food.setFoodName(foodName);
                food.setPractice(foodPractice);
                food.setMaterial(foodMaterial);
                food.setEffect(foodEffect);

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
                BmobFile bmobFile=new BmobFile(file);
                bmobFile.upload(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            //bmobFile.getFileUrl()--返回的上传文件的完整地址
                            showShortToast("上传文件成功" );
                            isUploadSuccess=true;
                        }else{
                            showShortToast("上传文件失败");
                            isUploadSuccess=false;
                        }
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                isUploadSuccess=false;
                showShortToast("压缩文件失败");
            }
            @Override
            public void onComplete() {
            }
        });

    }
}
