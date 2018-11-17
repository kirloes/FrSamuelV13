package com.example.frsamuel.frsamuelv13;

import java.util.Date;

public class Comment {
    private String post;
    private String user_id;
    private Date time;
    public Comment(){

    }

    public Comment(String post, String user_id, Date commetDate)
    {
        this.post = post;
        this.user_id = user_id;
        this.time = time;
    }
    public String getpost() {
        return post;
    }

    public void setpost(String post) {
        this.post = post;
    }

    public String getuser_id() {
        return user_id;
    }

    public void setuser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date gettime() {
        return time;
    }

    public void settime(Date time) {
        this.time = time;
    }
}
