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
import com.zxx.cookbook.R;
import com.zxx.cookbook.bean.CookBook;

import net.bither.util.NativeUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2018/5/9.
 */

public class AddCookbookActivity extends BaseActivity {
    private final int REQUEST_CODE = 10001;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.add_cookbook_img)
    ImageView addCookbookImg;
    @BindView(R.id.add_cookbook_et1)
    EditText addCookbookEt1;
    private boolean isUploadSuccess;
    private String imgTmpPath;
    private BmobFile bmobFile;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_cookbook;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        titleText.setText("添加菜谱");
        imgTmpPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/tmp";
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.title_left, R.id.add_cookbook_chooseImg, R.id.add_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.add_cookbook_chooseImg:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.add_ok:
                String cookbookName=addCookbookEt1.getText().toString();
                if(!isUploadSuccess){
                    showShortToast("请先添加图片");
                    return;
                }
                if(TextUtils.isEmpty(cookbookName)){
                    showShortToast("请输入菜谱名称");
                    return;
                }
                CookBook cookBook=new CookBook();
                cookBook.setCookbookName(cookbookName);
                cookBook.setCookbookImage(bmobFile);
                cookBook.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            showShortToast("添加菜谱成功");
                            finish();
                        }else{
                            showShortToast("添加菜谱失败");
                        }
                    }
                });
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(File file) {
                Glide.with(AddCookbookActivity.this).load(file).apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy( DiskCacheStrategy.NONE)).into(addCookbookImg);
                bmobFile=new BmobFile(file);
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
                            bmobFile=null;
                        }
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e.getMessage());
                bmobFile=null;
                isUploadSuccess=false;
                showShortToast("压缩文件失败");
            }
            @Override
            public void onComplete() {
            }
        });

    }
}
