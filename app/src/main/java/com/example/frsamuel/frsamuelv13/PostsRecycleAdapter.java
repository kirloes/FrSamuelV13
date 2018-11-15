package com.example.frsamuel.frsamuelv13;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsRecycleAdapter extends RecyclerView.Adapter<PostsRecycleAdapter.ViewHolder>{
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFire;

    public Context context;
    public List<Posts> post_List_data;

    public PostsRecycleAdapter(List<Posts> post_List_data)
    {
        this.post_List_data = post_List_data;
        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list,viewGroup ,false);
        context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.setIsRecyclable(false);

        final String postView = post_List_data.get(i).getPost();
        final String postID = post_List_data.get(i).postID;
        long millisecond = post_List_data.get(i).getTime().getTime();
        final String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();

        final String nameView = post_List_data.get(i).getUser_id();
        mFire.collection("Users").document(nameView).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        String  name = task.getResult().getString("name").toString();
                        setHolder(viewHolder,postView,name, dateString);
                    }
                }
            }
        });
        final String user_id = mAuth.getCurrentUser().getUid();
        if(user_id != null) {
            mFire.collection("Posts/" + postID + "/Likes").document(user_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        viewHolder.postLikebtn.setImageDrawable(context.getResources().getDrawable(R.drawable.liked_action));
                    } else {
                        viewHolder.postLikebtn.setImageDrawable(context.getResources().getDrawable(R.drawable.like_action));
                    }
                }
            });
        }
        mFire.collection("Posts/" + postID + "/Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty())
                {
                    int count = documentSnapshots.size();
                    viewHolder.setlikecount(count);
                }else{
                    viewHolder.setlikecount(0);
                }
            }
        });

        if(user_id != null) {
        viewHolder.postLikebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mFire.collection("Posts/" + postID + "/Likes").document(user_id)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()){
                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("time", FieldValue.serverTimestamp());
                            mFire.collection("Posts/" + postID + "/Likes").document(mAuth.getCurrentUser().getUid()).set(likesMap);
                        }
                        else{
                            mFire.collection("Posts/" + postID + "/Likes").document(mAuth.getCurrentUser().getUid()).delete();

                        }
                    }
                });


            }
        });}

        if(user_id != null){
               viewHolder.postDeletebtn.setOnClickListener(new View.OnClickListener(){
                   @Override
                   public void onClick(View v){
                       if(nameView .equals(user_id)){
                           mFire.collection("Posts").document(postID).delete();
                           Toast.makeText(context, "تم مسحه", Toast.LENGTH_SHORT).show();
                           ((HomeActivity)context).finish();
                           context.startActivity(new Intent(context, HomeActivity.class));
                       }
                       else{
                           Toast.makeText(context, "ليس لديك الحق", Toast.LENGTH_SHORT).show();
                       }
                   }
               });


        }
    }

    public void setHolder(ViewHolder holder, String postView, String name, String date)
    {
        holder.setpostText(postView);
        holder.setUser_idText(name);
        holder.setDate(date);
    }

    @Override
    public int getItemCount() {
        return post_List_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;

        private TextView postView;
        private TextView user_id;
        private TextView postDate;
        private ImageView postLikebtn;
        private ImageView postDeletebtn;
        private TextView postLikecounter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            postLikebtn = mView.findViewById(R.id.Like_Btn);
            postDeletebtn = mView.findViewById(R.id.delete_post_btn);
        }

        public void setpostText(String text){
            postView = mView.findViewById(R.id.PostText);
            postView.setText(text);
        }

        public void setUser_idText(String id)
        {
            user_id = mView.findViewById(R.id.UserName_ListView);
            user_id.setText(id);
        }

        public void setDate(String Date){
            postDate = mView.findViewById(R.id.Date_ListView);
            postDate.setText(Date);
        }

        public void setlikecount(int count){
            postLikecounter = mView.findViewById(R.id.like_counter);
            postLikecounter.setText(count + "Likes");
        }
    }
}
