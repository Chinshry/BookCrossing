package com.bcing.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bcing.R;
import com.bcing.entity.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.ui
 * File Name:ForgetActivity
 * Describe：忘记密码
 */

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_fg_pw;
    private EditText et_fg_mail;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        initView();
    }

    //初始化View
    private void initView() {
        et_fg_mail = (EditText) findViewById(R.id.et_fg_mail);

        btn_fg_pw = (Button) findViewById(R.id.btn_fg_pw);
        btn_fg_pw.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_fg_pw:
                //1、获取输入框的邮箱
                final String email = et_fg_mail.getText().toString().trim();
                //2、判断是否为空
                if (!TextUtils.isEmpty(email)){
                    //3、发送邮件
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(ForgetPasswordActivity.this,"重置密码请求成功，请到" + email + "邮箱进行密码重置操作",Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(ForgetPasswordActivity.this,"邮件发送失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
