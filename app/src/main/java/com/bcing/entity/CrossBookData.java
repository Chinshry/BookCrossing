package com.bcing.entity;

import java.io.Serializable;

/**
 * Created by 57010 on 2017/5/17.
 */

public class CrossBookData implements Serializable {

    public static final String serialVersionName = "bookInfo";

    private String isbn;
    private String username;
    private int usernameImage;
    private String bookName;
    private String author;
    private String publish;
    private String city;
    private String bookImage;
//    protected List<BookInfoResponse> books;
    private String pages;
    private String pubdate;
    private String summary;

    private int crosscode;



    private String createdAt;

//    public BookListInfo(String username, String bookName, String author, String publish,
//                          String city, String bookImage) {
//        this.username = username;
//        this.bookName = bookName;
//        this.author = author;
//        this.publish = publish;
//        this.city = city;
//        this.usernameImage = R.drawable.user_image;
//        this.bookImage = bookImage;
//    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

//    public List<BookInfoResponse> getBooks() {
//        return books;
//    }
//
//    public void setBooks(List<BookInfoResponse> books) {
//        this.books = books;
//    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getCrosscode() {
        return crosscode;
    }

    public void setCrosscode(int crosscode) {
        this.crosscode = crosscode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
