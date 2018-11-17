package com.example.frsamuel.frsamuelv13;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar setupToolbar;

    private EditText setupName;
    private EditText setupPhone;
    private EditText setupAddress;
    private Button setupSave;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        setupToolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("الإعدادات");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        fbs = FirebaseFirestore.getInstance();

        setupName = findViewById(R.id.setupName);
        setupPhone = findViewById(R.id.setupPhone);
        setupAddress = findViewById(R.id.setupAdress);
        setupSave = findViewById(R.id.setupsaveBtn);


    }

    @Override
    protected void onStart() {
        super.onStart();
        final String user_id = mAuth.getCurrentUser().getUid();
        if(user_id != null){
            fbs.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        if(task.getResult().exists())
                        {
                            setupName.setText(task.getResult().getString("name").toString());
                            setupAddress.setText(task.getResult().getString("address").toString());
                            setupPhone.setText(task.getResult().getString("phone").toString());
                        }
                    }
                }
            });
        }
        else{
            sendToLogin();
        }

        setupSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name =  setupName.getText().toString();
                final String Phone = setupPhone.getText().toString();
                final String Address = setupAddress.getText().toString();

                if(!TextUtils.isEmpty(Name)&&!TextUtils.isEmpty(Phone)&&!TextUtils.isEmpty(Address)) {
                        if (Phone.length() == 11) {
                            Map<String, String> user = new HashMap<>();
                            user.put("name", Name);
                            user.put("phone", Phone);
                            user.put("address", Address);
                            fbs.collection("Users").document(user_id).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SetupActivity.this, "تم تحديث البيانات", Toast.LENGTH_SHORT).show();
                                        finish();
                                        } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(SetupActivity.this, error, Toast.LENGTH_LONG).show();
                                        }
                                    }
                            });
                        }else{
                            Toast.makeText(SetupActivity.this, "رقم الهاتف خطأ", Toast.LENGTH_LONG).show();
                        }
                }
            }
        });
    }


    private void sendToLogin()
    {
        Intent LoginIntent = new Intent (SetupActivity.this, LoginActivity.class);
        startActivity(LoginIntent);
        finish(); //user cannot go back until log in
    }
}
