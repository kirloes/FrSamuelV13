package com.example.frsamuel.frsamuelv13;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText regEmail;
    private EditText regPSW;
    private EditText regConfPSW;
    private EditText regName;
    private EditText regPhone;
    private EditText regAddress;
    private EditText regCodeVerify;
    private Button regBtn;
    private ProgressBar regProg;
    private String fCode ;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFire;

    private void initialize(){

        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseFirestore.getInstance();

        regEmail = findViewById(R.id.RegEmailText);
        regPSW = findViewById(R.id.RegPswText);
        regConfPSW =  findViewById(R.id.RegConfPswText);
        regName =  findViewById(R.id.RegNameText);
        regPhone =  findViewById(R.id.RegPhoneText);
        regAddress =  findViewById(R.id.RegAddressText);
        regBtn =  findViewById(R.id.RegRegBtn);
        regProg =  findViewById(R.id.RegProgress);
        regCodeVerify = findViewById(R.id.codeVerification);
    }

    private void setfCode(){
        if(mFire != null) {
            DocumentReference dr = mFire.collection("Verify").document("CodeV");
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            fCode = task.getResult().getString("Code");
                            //Toast.makeText(RegisterActivity.this, fCode, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "حدث خطأ ما حاول مرة أخرى", Toast.LENGTH_LONG).show();
                        /*finish();
                        startActivity(getIntent());*/
                    }
                }
            });
        }else{
            Toast.makeText(RegisterActivity.this, "حدث خطأ فى الأتصال", Toast.LENGTH_LONG).show();
        }
    }

    private void sendToMain()
    {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void regMeth() {

        regProg.setVisibility(View.VISIBLE);
        final String Name = regName.getText().toString();
        final String Email = regEmail.getText().toString();
        final String PSW = regPSW.getText().toString();
        final String confirm_PSW = regConfPSW.getText().toString();
        final String Phone = regPhone.getText().toString();
        final String Address = regAddress.getText().toString();
        final String Code = regCodeVerify.getText().toString();

        if (!TextUtils.isEmpty(Name) && !TextUtils.isEmpty(Email) && !TextUtils.isEmpty(PSW)
                && !TextUtils.isEmpty(confirm_PSW) && !TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Address)) {
            if (PSW.equals(confirm_PSW)) {
                if (Phone.length() == 11) {
                    if (Code.equals(fCode)) {
                        if (mAuth != null) {
                            mAuth.createUserWithEmailAndPassword(Email, PSW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String userID = mAuth.getCurrentUser().getUid();
                                        Map<String, String> user = new HashMap<>();
                                        user.put("name", Name);
                                        user.put("phone", Phone);
                                        user.put("address", Address);
                                        mFire.collection("Users").document(userID).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    sendToMain();
                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        regProg.setVisibility(View.INVISIBLE);
                                        String e = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, e, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else {
                        regProg.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, "حدث خطأ فى الأتصال حاول مرة اخرى", Toast.LENGTH_LONG).show();
                    }
                } else {
                    regProg.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegisterActivity.this, "رقم التأكيد خطأ", Toast.LENGTH_LONG).show();
                }
            } else {
                regProg.setVisibility(View.INVISIBLE);
                Toast.makeText(RegisterActivity.this, "رقم الهاتف خطأ", Toast.LENGTH_LONG).show();
            }
        } else {
            regProg.setVisibility(View.INVISIBLE);
            Toast.makeText(RegisterActivity.this, "تأكد من كلمة المرور", Toast.LENGTH_LONG).show();
        }
        }else{
            regProg.setVisibility(View.INVISIBLE);
            Toast.makeText(RegisterActivity.this, "املأ الفراغات", Toast.LENGTH_LONG).show();
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
        setfCode();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regMeth();
            }
        });
    }

}
