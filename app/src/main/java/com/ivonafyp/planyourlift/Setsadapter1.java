package com.ivonafyp.planyourlift;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class Setsadapter1 extends FirestoreRecyclerAdapter<Setslist, Setsadapter1.ExerciseHolder> {
    private callback Callback;
    private int setnum;
    public Setsadapter1(@NonNull FirestoreRecyclerOptions<Setslist> options) {
        super(options);

    }

    @Override
    public void onBindViewHolder(ExerciseHolder holder, int position, Setslist list) {
        holder.reps.setText(String.valueOf(list.getReps()));
        holder.value.setText(String.valueOf(list.getValue()));
        holder.value2.setText(String.valueOf(list.getunits()));
        setnum = holder.getAdapterPosition()+1;
        holder.setnum.setText(String.valueOf(setnum));
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.activity_setlist1, group, false);
        return new ExerciseHolder(view);
    }



    class ExerciseHolder extends RecyclerView.ViewHolder {
        TextView setnum,reps,value,value2;
        CheckBox doneset;
        ImageButton deleteset;

        public ExerciseHolder(@NonNull View itemView) {
            super(itemView);
            reps = itemView.findViewById(R.id.repstv);
            value = itemView.findViewById(R.id.valuetv);
            value2 = itemView.findViewById(R.id.value2tv);
            setnum =  itemView.findViewById(R.id.setnum);
            doneset = itemView.findViewById(R.id.doneset);
            deleteset = itemView.findViewById(R.id.deleteset);
            doneset.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View view) {
                    if (Callback != null) {
                        if (doneset.isChecked()) {
                            int position = getAdapterPosition();
                            Callback.oncallback(getSnapshots().getSnapshot(position), position);
                        }
                    }
                }
            });

                deleteset.setOnClickListener(new View.OnClickListener()
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




