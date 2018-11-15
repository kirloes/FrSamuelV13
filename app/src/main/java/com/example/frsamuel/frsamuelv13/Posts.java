package com.example.frsamuel.frsamuelv13;

import java.util.Date;

public class Posts extends PostID{
    private String post;
    private String user_id;
    private Date time;

    public Posts()
    {

    }

    public Posts(String user_id, String post, Date time)
    {
        this.post = post;
        this.user_id = user_id;
        this.time = time;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
