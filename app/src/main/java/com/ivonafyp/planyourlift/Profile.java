package com.ivonafyp.planyourlift;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView profilename, profileemail;
    Button profilelogout,changeemail,changepass,deleteaccount;
    String UserID;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilelogout = (Button) view.findViewById(R.id.profilelogout);
        UserID = mAuth.getCurrentUser().getUid();
        profilename = (TextView) view.findViewById(R.id.profilename);
        profileemail = (TextView) view.findViewById(R.id.profileemail);
        changepass = (Button) view.findViewById(R.id.changepass);
        changeemail = (Button) view.findViewById(R.id.changeemail);
        deleteaccount = (Button) view.findViewById(R.id.deleteaccount);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(user);
        DocumentReference reference;
        UserID = user.getUid();
        reference = db.collection("Users").document(UserID);
        reference.get().addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String username = task.getResult().getString("name");
                    String useremail = task.getResult().getString("email");
                    profilename.setText(username);
                    profileemail.setText(useremail);
                }
            }
        });



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText emailinput = new EditText(getActivity());
        emailinput.setHint("Email");
        final EditText passwordinput = new EditText(getActivity());
        passwordinput.setHint("Password");
        layout.addView(emailinput);
        layout.addView(passwordinput);
        alertDialogBuilder.setTitle("Please confirm your log in details");
        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String email = emailinput.getText().toString();
                String password = passwordinput.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(email, password);
                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                }
                                            });
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                    final EditText userinput = new EditText(getActivity());
                                    alertDialogBuilder.setTitle("Change your account email");
                                    alertDialogBuilder.setView(userinput);
                                    alertDialogBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                            });

                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Boolean error = false;
                                            String newemail = userinput.getText().toString();
                                            if (newemail.isEmpty()) {
                                                error = true;
                                                userinput.setError("Email cannot be empty");
                                            }
                                            if (error) {
                                                userinput.setError("Email cannot be empty");
                                            } else {
                                                alertDialog.dismiss();
                                                user.updateEmail(newemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Map<String, Object> User = new HashMap<>();
                                                            User.put("email", newemail);
                                                            db.collection("Users").document(UserID).set(User, SetOptions.merge());
                                                            updateUI(null);
                                                            profileemail.setText(newemail);
                                                            Toast.makeText(getActivity(), "Email updated", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                });
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(getActivity(), "Login failed" + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
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

        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });


        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(getActivity());
        LinearLayout layout1 = new LinearLayout(getActivity());
        layout1.setOrientation(LinearLayout.VERTICAL);
        final EditText emailinput1 = new EditText(getActivity());
        emailinput1.setHint("Email");
        final EditText passwordinput1 = new EditText(getActivity());
        passwordinput1.setHint("Password");
        layout1.addView(emailinput1);
        layout1.addView(passwordinput1);
        alertDialogBuilder1.setTitle("Please confirm your log in details");
        alertDialogBuilder1.setView(layout1);
        alertDialogBuilder1.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String email = emailinput1.getText().toString();
                String password = passwordinput1.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(email, password);
                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                }
                                            });
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                    final EditText userinput = new EditText(getActivity());
                                    alertDialogBuilder.setTitle("Change your account password");
                                    alertDialogBuilder.setView(userinput);
                                    alertDialogBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                            });

                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Boolean error = false;
                                            String newpass = userinput.getText().toString();
                                            if (newpass.isEmpty()) {
                                                error = true;
                                                userinput.setError("Passowrd cannot be empty");
                                            }
                                            if (newpass.length() < 8) {
                                                userinput.setError("Password must be at least 8 characters");
                                                error = true;
                                            }
                                            if (error) {
                                                userinput.setError("Check your password is at least 8 characters long");
                                            } else {
                                                alertDialog.dismiss();
                                                user.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getActivity(), "Password updated", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                });
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(getActivity(), "Login failed" + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });


            }


        })
             .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        final AlertDialog alertDialog1 = alertDialogBuilder1.create();

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.show();
            }
        });

        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(getActivity());
        LinearLayout layout2 = new LinearLayout(getActivity());
        layout2.setOrientation(LinearLayout.VERTICAL);
        final EditText emailinput2 = new EditText(getActivity());
        emailinput2.setHint("Email");
        final EditText passwordinput2 = new EditText(getActivity());
        passwordinput2.setHint("Password");
        layout2.addView(emailinput2);
        layout2.addView(passwordinput2);
        alertDialogBuilder2.setTitle("Please confirm your log in details");
        alertDialogBuilder2.setView(layout2);
        alertDialogBuilder2.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String email = emailinput2.getText().toString();
                String password = passwordinput2.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);
                                            AuthCredential credential = EmailAuthProvider
                                                    .getCredential(email, password);
                                            user.reauthenticate(credential)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                        }
                                                    });
                                            new AlertDialog.Builder(getActivity()).setTitle("Delete account?").setMessage("Are you sure?").setNegativeButton("No", null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                    db.collection("Users").document(mAuth.getCurrentUser().getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                        }
                                                    })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });
                                                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                                                        startActivity(intent);
                                                                       Toast.makeText(getActivity(), "Deleted account", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            })
                                                    .show();


                                        }
                                    }
                                });
                        }
            })
         .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        final AlertDialog alertDialog2 = alertDialogBuilder2.create();

        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.show();
            }
        });
        profilelogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error logging out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUI(FirebaseUser user) {
    }

}
//Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs
