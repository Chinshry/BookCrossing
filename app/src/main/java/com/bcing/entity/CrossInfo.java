package com.bcing.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.entity
 * File Name:CrossBook
 * Describe：Application
 */

public class CrossInfo extends BmobObject {

    private String isbn;
    private String crossuser;
    private String crosscity;



    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCrossuser() {
        return crossuser;
    }

    public void setCrossuser(String crossuser) {
        this.crossuser = crossuser;
    }

    public String getCrosscity() {
        return crosscity;
    }

    public void setCrosscity(String crosscity) {
        this.crosscity = crosscity;
    }


}
