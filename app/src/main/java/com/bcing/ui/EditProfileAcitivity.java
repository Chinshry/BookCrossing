package com.bcing.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bcing.R;
import com.bcing.dialog.CustomDialog;
import com.bcing.entity.MyUser;
import com.bcing.utils.L;
import com.bcing.utils.UtilTools;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.ui
 * File Name:EditProfile
 * Describe：Application
 */

public class EditProfileAcitivity extends BaseActivity implements View.OnClickListener {
    //圆形头像
    private CircleImageView img_dp;
    private CustomDialog dialog;
    private TextView et_nick;
    private TextView et_mail;
    private TextView et_sex;
    private TextView et_city;

    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        initView();
    }

    @Override
    protected void initEvents() {
        setTitle(getString(R.string.edit));

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //初始化view
    private void initView() {
//        btn_edit = (Button) findViewById(R.id.btn_edit);
//        btn_edit.setOnClickListener(this);
//
//
//        btn_update = (Button) findViewById(R.id.btn_update);
//        btn_update.setOnClickListener(this);


        //圆形头像
        img_dp = (CircleImageView) findViewById(R.id.img_dp);
        img_dp.setOnClickListener(this);

        //1、拿到String
        UtilTools.getImageToShare(this,img_dp);


        //登陆Dialog初始化
        dialog = new CustomDialog(this, 640, 600, R.layout.dialog_photo, R.style.Theme_Dialog, Gravity.BOTTOM, R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);

        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancle = (Button) dialog.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(this);


        et_nick = (TextView) findViewById(R.id.et_nick);
        et_mail = (TextView) findViewById(R.id.et_mail);
        et_sex = (TextView) findViewById(R.id.et_sex);
        et_city = (TextView) findViewById(R.id.et_city);


        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_nick.setText(userInfo.getUsername());
        et_mail.setText(userInfo.getEmail());

        if (userInfo.getSex()) {
            et_sex.setText("男");
        } else {
            et_sex.setText("女");
        }
        et_city.setText(userInfo.getCity());

    }

    public void onOptionPicker(View view) {
        OptionPicker picker = new OptionPicker(this, new String[]{"男", "女"});
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setShadowColor(Color.RED, 40);
        picker.setSelectedIndex(1);
        picker.setCycleDisable(true);
        picker.setTextSize(20);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {

                final MyUser user = new MyUser();
                if (item.equals("男")) {
                    user.setSex(true);
                } else {
                    user.setSex(false);
                }

                BmobUser bmobUser = BmobUser.getCurrentUser();
                user.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(EditProfileAcitivity.this, "修改性别成功", Toast.LENGTH_SHORT).show();
                            if (user.getSex()) {
                                et_sex.setText("男");
                            } else {
                                et_sex.setText("女");
                            }
                        } else {
                            Toast.makeText(EditProfileAcitivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        picker.show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            //圆形头像
            case R.id.img_dp:
                dialog.show();
                break;

            case R.id.btn_camera:
                toCamere();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.btn_cancle:
                dialog.dismiss();
                break;
        }
    }

    public static final String PHOTO_IMAGE_FILENAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private File tempFile = null;

    //跳转相机
    private void toCamere() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用 可用高就进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageState(), PHOTO_IMAGE_FILENAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 0) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILENAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        L.e("setted");
                        //既然已经设置图片 原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                            L.e("delect");
                        }
                    }
                    break;
            }
        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("uri == null");
            return;
        }
        L.e("uri != null");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片质量 分辨率
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            img_dp.setImageBitmap(bitmap);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存图片
        UtilTools.putImageToShare(this,img_dp);

    }

    public void onAddress3Picker(View view) {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideCounty(true);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                showToast("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                final MyUser user = new MyUser();
                user.setCity(city.getAreaName());

                BmobUser bmobUser = BmobUser.getCurrentUser();
                user.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(EditProfileAcitivity.this, "修改城市成功", Toast.LENGTH_SHORT).show();
                            et_city.setText(user.getCity());
                        } else {
                            Toast.makeText(EditProfileAcitivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        task.execute("重庆", "重庆");
    }

    public void noEdit(View view) {
        showToast("无法修改");
    }


}
