package com.bcing.entity;

import java.lang.reflect.Array;

import cn.bmob.v3.BmobObject;

/**
 * Created by 57010 on 2017/5/17.
 */

public class WantCrossInfo extends BmobObject {

    private Number crosscode;
    private String isbn;
    private String ownuser;
    private Array wantuser;
    private Array wantusercity;

    public Number getCrosscode() {
        return crosscode;
    }

    public void setCrosscode(Number crosscode) {
        this.crosscode = crosscode;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getOwnuser() {
        return ownuser;
    }

    public void setOwnuser(String ownuser) {
        this.ownuser = ownuser;
    }

    public Array getWantuser() {
        return wantuser;
    }

    public void setWantuser(Array wantuser) {
        this.wantuser = wantuser;
    }

    public Array getWantusercity() {
        return wantusercity;
    }

    public void setWantusercity(Array wantusercity) {
        this.wantusercity = wantusercity;
    }
}
