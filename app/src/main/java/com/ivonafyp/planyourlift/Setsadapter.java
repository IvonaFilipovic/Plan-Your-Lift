package com.ivonafyp.planyourlift;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class Setsadapter extends FirestoreRecyclerAdapter<Setslist, Setsadapter.ExerciseHolder> {
    private callback Callback;
    private int setnum;
    private String workoutname;
    private String exname;

    public Setsadapter(@NonNull FirestoreRecyclerOptions<Setslist> options) {
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
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.activity_setslist, group, false);
        return new ExerciseHolder(view);
    }



    class ExerciseHolder extends RecyclerView.ViewHolder {
        TextView exercisename,setnum,reps,value,value2;
        Button editset;
        ImageButton deleteset;

        public ExerciseHolder(@NonNull View itemView) {
            super(itemView);
            reps = itemView.findViewById(R.id.repstv);
            value = itemView.findViewById(R.id.valuetv);
            value2 = itemView.findViewById(R.id.value2tv);
            setnum =  itemView.findViewById(R.id.setnum);
            editset = itemView.findViewById(R.id.editset);
            deleteset = itemView.findViewById(R.id.deleteset);
            Log.d("tag","exname" + exercisename);
            Log.d("tag","workoutname" + workoutname);
            editset.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View view){
                    int position = getAdapterPosition();
                    Callback.oncallback(getSnapshots().getSnapshot(position),position);
                }
            });

            deleteset.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View view){
                    deletemethod(getAdapterPosition());
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String UserID = user.getUid();
                    DocumentReference ref = db.collection("Users").document(UserID).collection("Workouts").document(workoutname).collection("exercises").document(exname);
                    ref.update("setcounter", FieldValue.increment(-1));
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
    public void setWorkoutname(String workoutname) {
        this.workoutname = workoutname;
    }
    public void setExname(String exname) {
        this.exname = exname;
    }

}
//Code may include some snippets from https://developer.android.com/ and https://firebase.google.com/docs




