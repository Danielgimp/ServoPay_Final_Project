package com.example.servopay.Model;

public class Comment {

    private String comment,publisher,id;

    public Comment(String comment, String publisher , String id) {
        this.comment = comment;
        this.publisher = publisher;
        this.id = id;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


}
