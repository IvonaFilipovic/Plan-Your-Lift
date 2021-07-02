package com.ivonafyp.planyourlift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CurrentWorkoutadapter extends FirestoreRecyclerAdapter<Exerciselist, CurrentWorkoutadapter.ExerciseHolder> {
    private final CurrentWorkout fragment;
    public Context context;
    String workoutname;

    private callback Callback;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();


    public CurrentWorkoutadapter(@NonNull FirestoreRecyclerOptions<Exerciselist> options, Context context, CurrentWorkout fragment){
        super(options);
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public void onBindViewHolder(CurrentWorkoutadapter.ExerciseHolder holder, int position, Exerciselist list) {
        holder.exercisename.setText(list.getExercisename());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.childrv.setLayoutManager(layoutManager);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UserID = user.getUid();
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();
        holder.childrv.findViewById(R.id.childrv);

        Query query = db.getInstance().collection("Users").document(UserID).collection("Workouts").document(workoutname).collection("exercises").document(id).collection(id).orderBy("date", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Setslist> options = new FirestoreRecyclerOptions.Builder<Setslist>()
                .setQuery(query, Setslist.class)
                .build();
        Setsadapter1 secondadapter = new Setsadapter1(options);
        secondadapter.startListening();
        secondadapter.notifyDataSetChanged();
        holder.childrv.setAdapter(secondadapter);
        secondadapter.readcallback(new Setsadapter1.callback() {
            @Override
            public void oncallback(DocumentSnapshot documentSnapshot, int position) {
                fragment.method();
            }
        });
    }
    public void deletemethod(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    @NonNull
    @Override
    public CurrentWorkoutadapter.ExerciseHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.activity_currentworkoutlist, group, false);
        return new CurrentWorkoutadapter.ExerciseHolder(view);
    }


    class ExerciseHolder extends RecyclerView.ViewHolder {
        TextView exercisename;
        public RecyclerView childrv;

        public ExerciseHolder(@NonNull View itemView) {
            super(itemView);
            exercisename = itemView.findViewById(R.id.exercisename);
            childrv = itemView.findViewById(R.id.childrv);

        }


    }
    public void setWorkoutname(String workoutname) {
        this.workoutname = workoutname;
    }

    public interface callback {
        void oncallback(DocumentSnapshot documentSnapshot, int position);
    }

    public void readcallback(callback Callback) {
        this.Callback = Callback;
    }




}
//Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs
