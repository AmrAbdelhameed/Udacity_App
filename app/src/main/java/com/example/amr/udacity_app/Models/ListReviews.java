package com.example.amr.udacity_app.Models;

public class ListReviews {
    private String author;
    private String content;

    public ListReviews() {}

    public ListReviews(String author, String content) {
        super();
        this.author = author;
        this.content = content;
    }

    public String getauthor() {
        return author;
    }

    public void setauthor(String author) {
        this.author = author;
    }

    public String getcontent() {
        return content;
    }

    public void setcontent(String content) {
        this.content = content;
    }

}