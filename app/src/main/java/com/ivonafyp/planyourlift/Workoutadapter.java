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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class Workoutadapter extends FirestoreRecyclerAdapter<Workoutlist, Workoutadapter.WorkoutHolder> {
      private callback Callback;


    public Workoutadapter(@NonNull FirestoreRecyclerOptions<Workoutlist> options) {
        super(options);
    }
@Override
public void onBindViewHolder(WorkoutHolder holder, int position, Workoutlist list) {
        holder.workoutname.setText(list.getWorkoutname());

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
public WorkoutHolder onCreateViewHolder(ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.activity_workoutlist, group, false);
        return new WorkoutHolder(view);
        }


class WorkoutHolder extends RecyclerView.ViewHolder {
    TextView workoutname;
    Button editworkout, deleteworkout;

    public WorkoutHolder(@NonNull View itemView) {
        super(itemView);
        workoutname = itemView.findViewById(R.id.workoutname);
        editworkout = itemView.findViewById(R.id.editworkout);
        deleteworkout = itemView.findViewById(R.id.deleteworkout);
        editworkout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                int position = getAdapterPosition();
               DocumentReference ref =  getSnapshots().getSnapshot(position).getReference();
               Callback.oncallback(getSnapshots().getSnapshot(position),position);
        }
    });


        deleteworkout.setOnClickListener(new View.OnClickListener()
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
