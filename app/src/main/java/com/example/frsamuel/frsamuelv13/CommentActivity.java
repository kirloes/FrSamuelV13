package com.example.frsamuel.frsamuelv13;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CommentActivity extends AppCompatActivity {

    private RecyclerView commentList;
    private List<Comment> comment_List_data;
    private CommentRecycleAdapter commentAdap;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebasefirestor;

    private Button addComment;
    private EditText textComment;

    String postID;
    String postName;
    String currName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        mAuth = FirebaseAuth.getInstance();
        firebasefirestor = FirebaseFirestore.getInstance();

        comment_List_data = new ArrayList<>();
        commentList = findViewById(R.id.Comment_view);
        commentAdap = new CommentRecycleAdapter(comment_List_data);
        commentList.setLayoutManager(new LinearLayoutManager(this));
        commentList.setAdapter(commentAdap);

        addComment = findViewById(R.id.addComment);
        textComment = findViewById(R.id.textComment);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentMeth();
            }
        });

        Intent comment = getIntent();
        postID = comment.getStringExtra("postID");
        postName = comment.getStringExtra("userName");
        Toast.makeText(this, postID, Toast.LENGTH_SHORT).show();

        firebasefirestor.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        currName = task.getResult().getString("name");
                    }
                }
            }
        });
        firebasefirestor.collection("Posts").document(postID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Comment com = new Comment();
                    com.setpost(task.getResult().getString("post"));
                    com.settime(task.getResult().getDate("time"));
                    com.setuser_id(postName);
                    comment_List_data.add(com);
                    commentAdap.notifyDataSetChanged();
                    loadComment(postID);
                }
            }
        });

    }

    private void loadComment(String postID) {
        Query newQuery = firebasefirestor.collection("Posts/" + postID + "/Comments")
                .orderBy("time", Query.Direction.ASCENDING);


        newQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String postID = doc.getDocument().getId();
                        if (postID.isEmpty()) {
                            continue;
                        }
                        Comment com = doc.getDocument().toObject(Comment.class);
                        if (com.gettime() == null) {
                            continue;
                        }
                        comment_List_data.add(com);
                        commentAdap.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void addCommentMeth() {
        String comment = textComment.getText().toString();
        if (!TextUtils.isEmpty(comment)) {
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("post", comment);
            postMap.put("user_id", currName);
            postMap.put("time", FieldValue.serverTimestamp());

            firebasefirestor.collection("Posts/" + postID + "/Comments").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CommentActivity.this, "تم", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CommentActivity.this, "حدث خطأ ما حاول مرة أخرى", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(CommentActivity.this, "اكتب شىء", Toast.LENGTH_SHORT).show();
        }
    }
}
