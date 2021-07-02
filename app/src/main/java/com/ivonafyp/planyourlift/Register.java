package com.ivonafyp.planyourlift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button Register;
    EditText Email,Password,Name;
    TextView haveaccount,policy;
    String UserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        Register = (Button) findViewById(R.id.launchregbutton);
        Name = (EditText) findViewById(R.id.regname);
        Email = (EditText) findViewById(R.id.regemail);
        Password = (EditText) findViewById(R.id.regpassword);
        haveaccount = (TextView) findViewById(R.id.haveaccount);
        policy = (TextView) findViewById(R.id.policy);
        String policylink = " By clicking register, you agree to our <a href='https://sites.google.com/view/planyourlift/home'>Terms & Privacy Policy.</a>";
        policy.setText(Html.fromHtml(policylink));
        policy.setMovementMethod(LinkMovementMethod.getInstance());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Register.this,MainActivity2.class);
            startActivity(intent);
            finish();
         }

        Register.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String name = Name.getText().toString();
                                        String email = Email.getText().toString();
                                        if (TextUtils.isEmpty(email)) {
                                            Email.setError("Please enter an email");
                                            return;
                                        }

                                        String password = Password.getText().toString();
                                        if (TextUtils.isEmpty(password)) {
                                            Password.setError("Please enter a password");
                                            return;
                                        }
                                        if (password.length() < 8) {
                                            Password.setError("Password must be at least 8 characters");
                                            return;
                                        }

                                        mAuth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Register.this, "User registered successfully",Toast.  LENGTH_SHORT).show();
                                                            FirebaseUser user = mAuth.getCurrentUser();
                                                            updateUI(user);
                                                            UserID = mAuth.getCurrentUser().getUid();
                                                            Map<String, Object> User = new HashMap<>();
                                                            User.put("name", name);
                                                            User.put("email", email);
                                                            db.collection("Users").document(UserID).set(User).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {                                                                 }
                                                                    });
                                                            Intent intent = new Intent(Register.this, MainActivity2.class);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(Register.this, "Registration failed" + task.getException().getMessage(),
                                                                    Toast.  LENGTH_SHORT).show();
                                                            updateUI(null);
                                                        }
                                                    }
                                                });
                                    }
        });

        haveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LoginEmail.class);
                startActivity(intent);
            }
        });



    }

            private void reload() { }

    private void updateUI(FirebaseUser user) {

    }

    public void logout(View v) {

        mAuth.signOut();
    }

        }

//Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs
