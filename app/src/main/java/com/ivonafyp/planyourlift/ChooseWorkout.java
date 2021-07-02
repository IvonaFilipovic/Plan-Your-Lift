package com.ivonafyp.planyourlift;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ChooseWorkout extends Fragment {
    TextView title;
    RecyclerView chooseworkoutlist;
    View view;
    String UserID;
    private ChooseWorkoutadapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chooseworkout, container, false);
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
        adapter = new ChooseWorkoutadapter(options);
        chooseworkoutlist = (RecyclerView) view.findViewById(R.id.chooseworkoutlist);
        chooseworkoutlist.setHasFixedSize(true);
        chooseworkoutlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        chooseworkoutlist.setAdapter(adapter);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView) view.findViewById(R.id.title);



        adapter.readcallback(new ChooseWorkoutadapter.callback() {
            @Override
            public void oncallback(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new CurrentWorkout();
                Bundle bundle = new Bundle();
                bundle.putString("id",id);
                myFragment.setArguments(bundle);
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
