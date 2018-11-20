package com.example.frsamuel.frsamuelv13;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private EditText postText;
    private Button AddPostBtn;
    private RecyclerView postList;

    private List<Posts> post_List_data;
    private PostsRecycleAdapter postAdap;
    private Boolean firstPage = true;
    private DocumentSnapshot lastVisible;
    private int counter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebasefirestor;

    private void initialize(){
        mAuth = FirebaseAuth.getInstance();
        firebasefirestor = FirebaseFirestore.getInstance();

        counter = 0;
        post_List_data = new ArrayList<>();
        firstPage = true;

        postText =  findViewById(R.id.textPost);
        AddPostBtn = findViewById(R.id.addPost);
        postList = findViewById(R.id.Post_view);

        postAdap = new PostsRecycleAdapter(post_List_data);
        postList.setLayoutManager(new LinearLayoutManager(this));
        postList.setAdapter(postAdap);
    }

    private void addPost(){
        String post = postText.getText().toString();
        if(!TextUtils.isEmpty(post))
        {
            Map<String,Object> postMap = new HashMap<>();
            postMap.put("post", post);
            postMap.put("user_id",mAuth.getCurrentUser().getUid());
            postMap.put("time", FieldValue.serverTimestamp());

            firebasefirestor.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(HomeActivity.this, "تم", Toast.LENGTH_SHORT).show();
                        Intent thisIntent = getIntent();
                        finish();
                        startActivity(thisIntent);
                    }else{
                        Toast.makeText(HomeActivity.this, "حدث خطأ ما حاول مرة أخرى", Toast.LENGTH_SHORT).show();
                    } }
            });
        }else{
            Toast.makeText(HomeActivity.this, "اكتب شىء", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData(){
        post_List_data.clear();

        Query newQuery = firebasefirestor.collection("Posts")
                .orderBy("time", Query.Direction.ASCENDING);
        newQuery.addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(firstPage) {
                    if(documentSnapshots.size() > 0 ){
                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);}
                }
                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String postID = doc.getDocument().getId();
                        if(postID.isEmpty()){
                            continue;
                        }
                        Posts post = doc.getDocument().toObject(Posts.class).withID(postID);
                        if(post.getTime() == null){
                            continue;
                        }
                        if(firstPage){
                            post_List_data.add(post);}
                        else{
                            post_List_data.add(0,post);
                        }
                        postAdap.notifyDataSetChanged();
                }
                    firstPage = false;
            }
        }});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize();
        loadData();
        AddPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        postList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Boolean reachedButton = !recyclerView.canScrollVertically(1);

                if(reachedButton){

                }
            }
        });



    }
}
