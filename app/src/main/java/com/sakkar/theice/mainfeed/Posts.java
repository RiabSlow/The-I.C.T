package com.sakkar.theice.mainfeed;

import android.net.Uri;

import com.google.firebase.database.ServerValue;

import java.security.Timestamp;
import java.util.ArrayList;

/**
 * Created by Sakkar on 3/30/2017.
 */

public class Posts {
    private String post,authors,date;
    private int likes,unlikes;
    private String imageDownloadUri;
    private long postNo;
    ArrayList<String> likers=new ArrayList<>();
    ArrayList<String> unlikers=new ArrayList<>();
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

    public String getImageDownloadUri() {
        return imageDownloadUri;
    }

    public void setImageDownloadUri(String imageDownloadUri) {
        this.imageDownloadUri = imageDownloadUri;
    }

    public ArrayList<String> getLikers() {
        return likers;
    }

    public void setLikers(ArrayList<String> likers) {
        this.likers = likers;
    }

    public ArrayList<String> getUnlikers() {
        return unlikers;
    }

    public void setUnlikers(ArrayList<String> unlikers) {
        this.unlikers = unlikers;
    }

    @Override
    public String toString() {
        return post+" "+authors;
    }

    public void updateLikes(int i) {
        likes+=i;
    }

    public void addLikers(String name) {
        likers.add(name);
    }

    public void removeLikers(String name) {
        likers.remove(name);
    }

    public void updateUnlikes(int i) {
        unlikes+=i;
    }

    public void addUnlikers(String name) {
        unlikers.add(name);
    }

    public void removeUnikers(String name) {
        unlikers.remove(name);
    }
}
