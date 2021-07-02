package com.ivonafyp.planyourlift;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class Exerciseadapter extends FirestoreRecyclerAdapter<Exerciselist, Exerciseadapter.ExerciseHolder> {
    private callback Callback;


    public Exerciseadapter(@NonNull FirestoreRecyclerOptions<Exerciselist> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(ExerciseHolder holder, int position, Exerciselist list) {
        holder.exercisename.setText(list.getExercisename());
         holder.sets.setText(String.valueOf(list.getSetcounter()));

    }


    public void deletemethod(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.activity_exerciselist, group, false);
        return new ExerciseHolder(view);
    }


    class ExerciseHolder extends RecyclerView.ViewHolder {
        TextView exercisename,sets;
        Button editexercise, deleteexercise;

        public ExerciseHolder(@NonNull View itemView) {
            super(itemView);
            exercisename = itemView.findViewById(R.id.exercisename);
            sets = itemView.findViewById(R.id.setstv);
            editexercise = itemView.findViewById(R.id.editexercise);
            deleteexercise = itemView.findViewById(R.id.deleteexercise);
            editexercise.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View view){
                    int position = getAdapterPosition();
                    Callback.oncallback(getSnapshots().getSnapshot(position),position);
                }
            });
             deleteexercise.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View view){
                    deletemethod(getAdapterPosition());
                }
            });

        }

    }

    public interface callback {
        void oncallback(DocumentSnapshot documentSnapshot, int position);
    }
    public void readcallback(callback Callback) {
        this.Callback = Callback;
    }


}
//Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs



