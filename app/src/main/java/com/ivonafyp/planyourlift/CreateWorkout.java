package com.ivonafyp.planyourlift;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;


public class CreateWorkout extends Fragment {
    ExtendedFloatingActionButton fab;
    TextView title;
    RecyclerView workoutlistview;
    View view;
    String UserID;
    private Workoutadapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_createworkout, container, false);
        RecyclerView();
        return view;

    }

    private void RecyclerView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();
        updateUI(user);
        Query query = db.getInstance().collection("Users").document(UserID).collection("Workouts");

        FirestoreRecyclerOptions<Workoutlist> options = new FirestoreRecyclerOptions.Builder<Workoutlist>()
                .setQuery(query, Workoutlist.class)
                .build();
        adapter = new Workoutadapter(options);
        workoutlistview = (RecyclerView) view.findViewById(R.id.chooseworkoutlist);
        workoutlistview.setHasFixedSize(true);
        workoutlistview.setLayoutManager(new LinearLayoutManager(getActivity()));
        workoutlistview.setAdapter(adapter);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = (ExtendedFloatingActionButton) view.findViewById(R.id.createbutton);
        title = (TextView) view.findViewById(R.id.title);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog alertdialog = new Dialog(getActivity());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                alertdialog.setContentView(R.layout.alertdialog2);
                final EditText userinput = alertdialog.findViewById(R.id.userinput);
                userinput.setHint("Workout name");
                final Button create = alertdialog.findViewById(R.id.Create);
                final Button cancel = alertdialog.findViewById(R.id.Cancel);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userinput1 = userinput.getText().toString();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        updateUI(user);
                        Map<String, Object> wrkout = new HashMap<>();
                        wrkout.put("workoutname", userinput1);
                        db.collection("Users").document(UserID).collection("Workouts").document(userinput1).set(wrkout)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        userinput.setText("");
                                        alertdialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertdialog.dismiss();
                    }
                });

                alertdialog.show();
            }
        });

        adapter.readcallback(new Workoutadapter.callback() {
            @Override
            public void oncallback(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new CreatingWorkout();
                Fragment myFragment1 = new CurrentWorkout();
                Bundle bundle = new Bundle();
                bundle.putString("id",id);
                myFragment.setArguments(bundle);
                myFragment1.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), myFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        }
        private void updateUI (FirebaseUser user){
        }

        @Override
        public void onStart() {
            super.onStart();
            adapter.startListening();
        }

        @Override
        public void onStop() {
            super.onStop();
            adapter.stopListening();
        }

    }

    //Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs
