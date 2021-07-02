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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;


import java.util.HashMap;
import java.util.Map;


public class CreatingWorkout extends Fragment {
    ExtendedFloatingActionButton fab;
    ImageButton backbutton ;
    TextView title ;
    RecyclerView Exerciselistview;
    View view;
    String UserID;
    private Exerciseadapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_creating_workout, container, false);
        RecyclerView();
        return view;

    }

    private void RecyclerView() {
        Bundle bundle = getArguments();
        assert bundle != null;
        String s = bundle.getString("id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();
        updateUI(user);
        Query query = db.getInstance().collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").orderBy("date", Query.Direction.ASCENDING);
        CollectionReference exreference = db.getInstance().collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises");
        FirestoreRecyclerOptions<Exerciselist> options = new FirestoreRecyclerOptions.Builder<Exerciselist>()
                .setQuery(query, Exerciselist.class)
                .build();
        adapter = new Exerciseadapter(options);
        Exerciselistview = (RecyclerView) view.findViewById(R.id.currentworkoutview);
        Exerciselistview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        Exerciselistview.setLayoutManager(linearLayoutManager);
        Exerciselistview.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                adapter.notifyItemMoved(fromPos, toPos);

                return true;// true if moved, false otherwise
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

        }).attachToRecyclerView(Exerciselistview);

    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = (ExtendedFloatingActionButton) view.findViewById(R.id.addbutton);
        backbutton = (ImageButton) view.findViewById(R.id.backbutton);
        title = (TextView) view.findViewById(R.id.workoutname1);
        Bundle bundle = getArguments();
        assert bundle != null;
        String s = bundle.getString("id");
        title.setText(s);


        //dialog to chose type of exercise first
        AlertDialog.Builder typebuilder = new AlertDialog.Builder(getActivity());
        String[] types = {"Strength", "Cardio"};
        typebuilder.setTitle("Chose the type of exercise")
                .setItems(types, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) { //if user selects strength
                            showstrengthdialog();
                        } else if (which == 1) {//if user selects cardio
                            showcardiodialog();
                        } else { // if there is an error
                        }
                    }
                });

        final AlertDialog typeDialog = typebuilder.create();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeDialog.show();
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                              fragmentManager.popBackStack();
                                          }
                                      });
        adapter.readcallback(new Exerciseadapter.callback() {
            @Override
            public void oncallback(DocumentSnapshot documentSnapshot, int position) {
                String id1 = documentSnapshot.getId();
                Exerciselist exerciselist = documentSnapshot.toObject(Exerciselist.class);
                String units = exerciselist.getunits();
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                Fragment myFragment = new Sets();
                Bundle bundle = new Bundle();
                bundle.putString("exname", id1);
                bundle.putString("id", s);
                bundle.putInt("setinc", 1);
                bundle.putString("units", units);
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), myFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }


        void showstrengthdialog () {
            final Dialog alertdialog = new Dialog(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            alertdialog.setContentView(R.layout.alertdialog);
            alertdialog.setTitle("Write number of reps and weight for this set");
            final EditText exnameinput = alertdialog.findViewById(R.id.exnameinput);
            final RadioButton kgbutton = alertdialog.findViewById(R.id.kgbutton);
            final RadioButton lbsbutton = alertdialog.findViewById(R.id.lbsbutton);
            final RadioGroup group =alertdialog.findViewById(R.id.radiogroup);
            RadioButton chosenunits = group.findViewById(group.getCheckedRadioButtonId());
            final Button create = alertdialog.findViewById(R.id.Create);
            final Button cancel = alertdialog.findViewById(R.id.Cancel);
            Bundle bundle = getArguments();
            assert bundle != null;
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    RadioButton checkedRadioButton =group.findViewById(checkedId);
                    boolean isChecked = checkedRadioButton.isChecked();
                    if (isChecked)
                    {
                        String units = checkedRadioButton.getText().toString();
                        bundle.putString("units",units);
                    }
                }
            });
            create.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              String exname = exnameinput.getText().toString();
                                              String units = bundle.getString("units");
                                              FirebaseFirestore db = FirebaseFirestore.getInstance();
                                              FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                              updateUI(user);
                                              Map<String, Object> exercise = new HashMap<>();
                                              exercise.put("exercisename", exname);
                                              exercise.put("units",units);
                                              exercise.put("setcounter", 0);
                                              exercise.put("date", FieldValue.serverTimestamp());
                                              String s = bundle.getString("id");
                                              db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname).set(exercise)
                                                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                          @Override
                                                          public void onSuccess(Void aVoid) {
                                                              exnameinput.setText("");
                                                          }
                                                      })
                                                      .addOnFailureListener(new OnFailureListener() {
                                                          @Override
                                                          public void onFailure(@NonNull Exception e) {
                                                          }
                                                      });

                                              Map<String, Object> sets = new HashMap<>();
                                              sets.put("date", FieldValue.serverTimestamp());
                                              sets.put("units",units);
                                              db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname).collection(exname).document().set(sets).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                  @Override
                                                  public void onSuccess(Void aVoid) {
                                                      DocumentReference ref = db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname);
                                                      ref.update("setcounter", FieldValue.increment(1));
                                                  }
                                              })
                                                      .addOnFailureListener(new OnFailureListener() {
                                                          @Override
                                                          public void onFailure(@NonNull Exception e) {
                                                          }
                                                      });
                                              AppCompatActivity activity = (AppCompatActivity) getActivity();
                                              Fragment myFragment = new Sets();
                                              Bundle bundle = new Bundle();
                                              bundle.putString("exname", exname);
                                              bundle.putString("id", s);
                                              bundle.putInt("setinc", 1);
                                              bundle.putString("units",units);
                                              myFragment.setArguments(bundle);

                                              activity.getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), myFragment)
                                                      .addToBackStack(null)
                                                      .commit();
                                              alertdialog.dismiss();
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
    void showcardiodialog () {
        final Dialog alertdialog = new Dialog(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        alertdialog.setContentView(R.layout.alertdialog1);
        alertdialog.setTitle("Write number of reps and time for this set");
        final EditText exnameinput = alertdialog.findViewById(R.id.exnameinput);
        final RadioButton minbutton = alertdialog.findViewById(R.id.minbutton);
        final RadioButton secbutton = alertdialog.findViewById(R.id.secbutton);
        final RadioGroup group =alertdialog.findViewById(R.id.radiogroup);
        RadioButton chosenunits = group.findViewById(group.getCheckedRadioButtonId());
        final Button create = alertdialog.findViewById(R.id.Create);
        final Button cancel = alertdialog.findViewById(R.id.Cancel);
        Bundle bundle = getArguments();
        assert bundle != null;
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton =group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    String units = checkedRadioButton.getText().toString();
                    bundle.putString("units",units);
                }
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exname = exnameinput.getText().toString();
                String units = bundle.getString("units");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateUI(user);
                Map<String, Object> exercise = new HashMap<>();
                exercise.put("exercisename", exname);
                exercise.put("units",units);
                exercise.put("setcounter", 0);
                exercise.put("date", FieldValue.serverTimestamp());
                String s = bundle.getString("id");
                db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname).set(exercise)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                exnameinput.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                Map<String, Object> sets = new HashMap<>();
                sets.put("date", FieldValue.serverTimestamp());
                sets.put("units",units);
                db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname).collection(exname).document().set(sets).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference ref = db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname);
                        ref.update("setcounter", FieldValue.increment(1));
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                Fragment myFragment = new Sets();
                Bundle bundle = new Bundle();
                bundle.putString("exname", exname);
                bundle.putString("id", s);
                bundle.putInt("setinc", 1);
                bundle.putString("units",units);
                myFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), myFragment)
                        .addToBackStack(null)
                        .commit();
                alertdialog.dismiss();
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
