package com.example.frsamuel.frsamuelv13;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class CommentRecycleAdapter extends RecyclerView.Adapter<CommentRecycleAdapter.ViewHolder>{

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFire;

    public Context context;
    public List<Comment> comment_List_data;

    public CommentRecycleAdapter(List<Comment> comment_List_data)
    {
        this.comment_List_data = comment_List_data;
        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public CommentRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_list,viewGroup ,false);
        context = viewGroup.getContext();
        return new CommentRecycleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecycleAdapter.ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);

        final String postView = comment_List_data.get(i).getpost();
        final String postID = comment_List_data.get(i).getuser_id();
        long millisecond = comment_List_data.get(i).gettime().getTime();
        final String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();

        setHolder(viewHolder,postView,postID,dateString);
    }

    @Override
    public int getItemCount() {
        return comment_List_data.size();
    }

    public void setHolder(CommentRecycleAdapter.ViewHolder holder, String postView, String name, String date)
    {
        holder.setpostText(postView);
        holder.setUser_idText(name);
        holder.setDate(date);
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        private View mView;

        private TextView postView;
        private TextView user_id;
        private TextView postDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setpostText(String text){
            postView = mView.findViewById(R.id.CommentText);
            postView.setText(text);
        }

        public void setUser_idText(String id)
        {
            user_id = mView.findViewById(R.id.UserName_CommentList);
            user_id.setText(id);
        }

        public void setDate(String Date){
            postDate = mView.findViewById(R.id.Date_CommentList);
            postDate.setText(Date);
        }


    }
}
