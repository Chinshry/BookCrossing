package com.bcing.entity;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.entity
 * File Name:MyUser
 * Describe：扩展用户属性 年龄 性别 城市 简介
 */

public class MyUser extends BmobUser{
    private Integer age;
    private Boolean sex;
    private String nick;
    private String city;
    private String desc;
    private List<String> wish;
    private BmobFile userimg;

    public BmobFile getUserimg() {
        return userimg;
    }

    public void setUserimg(BmobFile userimg) {
        this.userimg = userimg;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setWish(List wish) {
        this.wish = wish;
    }

    public List getWish() {
        return wish;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
