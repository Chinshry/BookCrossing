package com.bcing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bcing.R;
import com.bcing.dialog.CustomDialog;
import com.bcing.entity.MyUser;
import com.bcing.main.MainActivity;
import com.bcing.utils.ShareUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.ui
 * File Name:LoginActivity
 * Describe：登录
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //登录框和按钮
    private EditText et_account;
    private EditText et_password;
    private Button btn_login;
    private CheckBox checkBox_password;


    //注册按钮

    private Button btn_registered;

    //忘记密码按钮
    private Button btn_forget;

    //登陆Dialog
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }


    private void initView() {
        //注册初始化
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);

        //登录初始化
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        checkBox_password = (CheckBox) findViewById(R.id.checkBox_password);

        //找回密码初始化
        btn_forget = (Button) findViewById(R.id.btn_forget);
        btn_forget.setOnClickListener(this);

        //登陆Dialog初始化
        dialog  = new CustomDialog(this,300,300, R.layout.dialog_loading,R.style.Theme_Dialog, Gravity.CENTER,R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);


        //设置选中的状态
        boolean isCheck = ShareUtils.getBoolean(this,"keeppass",false);
        checkBox_password.setChecked(isCheck);
        if (isCheck){
            //设置密码
            et_account.setText(ShareUtils.getString(this,"name",""));
            et_password.setText(ShareUtils.getString(this,"password",""));
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //忘记密码按钮
            case R.id.btn_forget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            //注册按钮
            case R.id.btn_registered:
                startActivity(new Intent(this, RegisteredActivity.class));
                break;
            //登录按钮
            case R.id.btn_login:
                //1、获取输入框的值
                String name = et_account.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(password)) {
                    dialog.show();
                    //登录
                    final MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            dialog.dismiss();
                            //判断结果
                            if (e == null) {
                                //判断邮箱是否验证
                                if (user.getEmailVerified()) {
                                    //跳转到主页面
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "请前往邮箱验证", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "登录失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //假设输入了用户名和密码 但未登录 直接退出了

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean(this, "keeppass", checkBox_password.isChecked());

        //是否记住密码
        if (checkBox_password.isChecked()) {
            //记住用户名密码
            ShareUtils.putString(this, "name", et_account.getText().toString().trim());
            ShareUtils.putString(this, "password", et_password.getText().toString().trim());
        } else {
            ShareUtils.deleShare(this, "name");
            ShareUtils.deleShare(this, "password");

        }

    }
}
