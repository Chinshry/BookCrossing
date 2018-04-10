package com.bcing.entity;

/**
 * Created by chinshry on 2018/3/14.
 * Project Name:BookCrossing
 * Package Name:com.bcing.entity
 * File Name:BookData
 * Describe：图书数据
 */

public class BookData {
    //标题 作者 出版社 封面
    //标题 出处 图片 url
    private String title;
    private String author;
    private String publisher;
    private String image;


    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
