package com.example.frsamuel.frsamuelv13;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView userList;
    private List<Users> user_List_data;
    private UsersRecycleAdapter userAdap;
    private String admin_id;
    private android.support.v7.widget.Toolbar userToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebasefirestor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        userToolbar = findViewById(R.id.userToolbar);
        setSupportActionBar(userToolbar);
        getSupportActionBar().setTitle("الأعضاء");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firebasefirestor = FirebaseFirestore.getInstance();

        firebasefirestor.collection("Verify").document("Admin").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        admin_id = task.getResult().getString("code");
                        setUsrs();
                    }
                }
            }
        });
        user_List_data = new ArrayList<>();
        userList = findViewById(R.id.users_view);
        userAdap = new UsersRecycleAdapter(user_List_data);
        userList.setLayoutManager(new LinearLayoutManager(this));
        userList.setAdapter(userAdap);

    }

   private void setUsrs(){
        super.onStart();
        user_List_data.clear();
        Query newQuery = firebasefirestor.collection("Users")
                .orderBy("name", Query.Direction.ASCENDING);
        newQuery.addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        if(doc.getDocument().getId().equals(admin_id)){continue;}
                        Users user = doc.getDocument().toObject(Users.class);
                        user.setUser_id(doc.getDocument().getId());
                        user_List_data.add(user);
                        userAdap.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
