package com.bcing.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 57010 on 2017/5/17.
 */

public class WantCrossInfo extends BmobObject {

    private Number crosscode;
    private String isbn;
    private String ownuser;
    private List<String> wantuser;
    private List<String> wantusercity;

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

    public List getWantuser() {
        return wantuser;
    }

    public void setWantuser(List wantuser) {
        this.wantuser = wantuser;
    }

    public List getWantusercity() {
        return wantusercity;
    }

    public void setWantusercity(List wantusercity) {
        this.wantusercity = wantusercity;
    }
}
