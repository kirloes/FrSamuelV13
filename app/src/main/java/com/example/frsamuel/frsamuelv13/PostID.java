package com.example.frsamuel.frsamuelv13;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class PostID {
    @Exclude
    public String postID;

    public <T extends PostID>T withID(@NonNull final String id)
    {
        this.postID = id;
        return (T) this;
    }

}
