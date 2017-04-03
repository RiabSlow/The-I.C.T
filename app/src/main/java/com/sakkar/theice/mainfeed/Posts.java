package com.sakkar.theice.mainfeed;

import com.google.firebase.database.ServerValue;

import java.security.Timestamp;

/**
 * Created by Sakkar on 3/30/2017.
 */

public class Posts {
    private String post,authors,date;
    private int likes,unlikes;
    long postNo;

    public Posts() {
    }

    public Posts(String post, String authors, int likes, int unlikes) {

        this.post = post;
        this.authors = authors;
        this.likes = likes;
        this.unlikes = unlikes;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getUnlikes() {
        return unlikes;
    }

    public void setUnlikes(int unlikes) {
        this.unlikes = unlikes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getPostNo() {
        return postNo;
    }

    public void setPostNo(long postNo) {
        this.postNo = postNo;
    }

    @Override
    public String toString() {
        return post+" "+authors;
    }
}
