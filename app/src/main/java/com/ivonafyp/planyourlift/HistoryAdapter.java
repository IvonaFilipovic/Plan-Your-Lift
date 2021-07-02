package com.ivonafyp.planyourlift;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class HistoryAdapter extends FirestoreRecyclerAdapter<Historylist, HistoryAdapter.WorkoutHolder> {
    private callback Callback;


    public HistoryAdapter(@NonNull FirestoreRecyclerOptions<Historylist> options) {
        super(options);
    }
    @Override
    public void onBindViewHolder(WorkoutHolder holder, int position, Historylist list) {
        holder.workoutname.setText(list.getWorkout());
        holder.time.setText(list.getTime());

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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.activity_savedworkoutlist, group, false);
        return new WorkoutHolder(view);
    }


    class WorkoutHolder extends RecyclerView.ViewHolder {
        TextView workoutname,time;
        ImageButton deletesaved;

        public WorkoutHolder(@NonNull View itemView) {
            super(itemView);
            workoutname = itemView.findViewById(R.id.workoutname);
            time = itemView.findViewById(R.id.time);
            deletesaved = itemView.findViewById(R.id.deletesaved);
            deletesaved.setOnClickListener(new View.OnClickListener()
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
