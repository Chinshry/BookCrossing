package com.bcing.entity;

import com.bcing.R;

/**
 * Created by 57010 on 2017/5/17.
 */

public class ItemBookDetail {
    private String username;
    private int usernameImage;
    private String bookName;
    private String author;
    private String publish;
    private String city;
    private String bookImage;

    public ItemBookDetail(String username, String bookName, String author, String publish,
                          String city, String bookImage) {
        this.username = username;
        this.bookName = bookName;
        this.author = author;
        this.publish = publish;
        this.city = city;
        this.usernameImage = R.drawable.user_image;
        this.bookImage = bookImage;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUsernameImage() {
        return usernameImage;
    }

    public void setUsernameImage(int usernameImage) {
        this.usernameImage = usernameImage;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getpublish() {
        return publish;
    }

    public void setpublish(String publish) {
        this.publish = publish;
    }

    public String getcity() {
        return city;
    }

    public void setcity(String city) {
        this.city = city;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }
}
