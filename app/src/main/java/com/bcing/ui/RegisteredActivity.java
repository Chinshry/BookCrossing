package com.bcing.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bcing.R;
import com.bcing.entity.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.ui
 * File Name:RegisteredActivity
 * Describe：注册
 */
public class RegisteredActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_reg_account;
    private EditText et_reg_mail;
    private EditText et_reg_pw;
    private Button btn_reg_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        initView();
    }

    private void initView() {
        et_reg_account = (EditText) findViewById(R.id.et_reg_account);
        et_reg_mail = (EditText) findViewById(R.id.et_reg_mail);
        et_reg_pw = (EditText) findViewById(R.id.et_reg_pw);
        btn_reg_register = (Button) findViewById(R.id.btn_reg_register);
        btn_reg_register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
                case R.id.btn_reg_register :
                    //获取输入框的值
                    String name = et_reg_account.getText().toString().trim();
                    String mail = et_reg_mail.getText().toString().trim();
                    String password = et_reg_pw.getText().toString().trim();
                    //判断是否为空
                    if (! TextUtils.isEmpty(name) &
                            ! TextUtils.isEmpty(mail) &
                            ! TextUtils.isEmpty(password)) {
                        //注册
                        MyUser user = new MyUser();
                        user.setUsername(name);
                        user.setPassword(password);
                        user.setEmail(mail);
                        //注意：不能用save方法进行注册
                        user.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser s, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(RegisteredActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(RegisteredActivity.this, "注册失败" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                    }
                    break;
        }
    }

    @Override
    protected void initEvents() {

    }
}

