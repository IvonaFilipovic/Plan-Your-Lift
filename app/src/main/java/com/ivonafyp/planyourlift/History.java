package com.ivonafyp.planyourlift;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class History extends Fragment {
    TextView title;
    RecyclerView savedworkoutlist;
    View view;
    String UserID;
    private HistoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        RecyclerView();
        return view;

    }

    private void RecyclerView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();
        updateUI(user);
        Query query = db.getInstance().collection("Users").document(UserID).collection("Saved Workouts");

        FirestoreRecyclerOptions<Historylist> options = new FirestoreRecyclerOptions.Builder<Historylist>()
                .setQuery(query, Historylist.class)
                .build();
        adapter = new HistoryAdapter(options);
        savedworkoutlist = (RecyclerView) view.findViewById(R.id.savedworkoutlist);
        savedworkoutlist.setHasFixedSize(true);
        savedworkoutlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        savedworkoutlist.setAdapter(adapter);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView) view.findViewById(R.id.title);


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
