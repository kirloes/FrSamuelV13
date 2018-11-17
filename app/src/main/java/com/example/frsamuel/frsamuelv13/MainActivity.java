package com.example.frsamuel.frsamuelv13;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mainToolbar;

    private Button homeBtn;
    private Button inboxBtn;
    private Button userBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFire;

    private String admin_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseFirestore.getInstance();

        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("لنتواصل");
        homeBtn = findViewById(R.id.homeBTN);
        inboxBtn = findViewById(R.id.inboxBTN);
        userBtn = findViewById(R.id.userBtn);
        userBtn.setVisibility(View.INVISIBLE);

        if(mAuth.getCurrentUser() == null)
        {
            sendToLogin();
        }

        mFire.collection("Verify").document("Admin").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        admin_id = task.getResult().getString("code");
                        if(mAuth.getCurrentUser().getUid().equals(admin_id)){

                            userBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToHome();
            }
        });

        inboxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "not yet", Toast.LENGTH_LONG).show();
            }
        });

        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,UsersActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_setting:
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
                return true;
            case R.id.action_logout:
                logOut();
                return true;
            default:
                finish();
                System.exit(0);
                return false;

        }
    }

    private void logOut()
    {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin()
    {
        Intent LoginIntent = new Intent (MainActivity.this, LoginActivity.class);
        startActivity(LoginIntent);
        finish(); //user cannot go back until log in
    }

    private void sendToHome(){
        Intent LoginIntent = new Intent (MainActivity.this, HomeActivity.class);
        startActivity(LoginIntent);
    }
}
