package com.ivonafyp.planyourlift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class LoginEmail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button Login;
    EditText Email;
    EditText Password;
    TextView resetpassword,register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        mAuth = FirebaseAuth.getInstance();
        Login = (Button) findViewById(R.id.loginbutton);
        Email = (EditText) findViewById(R.id.loginemail);
        Password = (EditText) findViewById(R.id.loginpassword);
        resetpassword = (TextView) findViewById(R.id.resetpassword);
        register = (TextView) findViewById(R.id.register);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginEmail.this,MainActivity2.class);
            startActivity(intent);
            finish();
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginEmail.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginEmail.this, "User logged in",Toast.  LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                        Intent intent = new Intent(LoginEmail.this, MainActivity2.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginEmail.this, "Login failed" + task.getException().getMessage(),
                                                Toast.  LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
            }
        });
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginEmail.this);
                final EditText userinput = new EditText(LoginEmail.this);
                alertDialogBuilder.setTitle("Enter your email");
                alertDialogBuilder.setView(userinput);
                alertDialogBuilder.setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String emailAddress = userinput.getText().toString();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginEmail.this, "Email sent" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginEmail.this,Register.class);
                startActivity(intent);
            }
        });
    }

                private void reload () {}

                private void updateUI (FirebaseUser user){

                }
            }
//Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs
