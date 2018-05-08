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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    @Override
    public int getLayoutId() {
        return R.layout.activity_add_food;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

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

    private void uploadImg(Uri res){
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(res, filePathColumns, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePathColumns[0]);
        String imagePath = c.getString(columnIndex);
        c.close();
        String imgPath=getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/tmp";
        NativeUtil.compressBitmap(NativeUtil.getBitmapFromFile(imagePath),imgPath);
    }
}
