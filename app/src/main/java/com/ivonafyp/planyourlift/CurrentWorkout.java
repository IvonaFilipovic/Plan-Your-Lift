package com.ivonafyp.planyourlift;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class CurrentWorkout extends Fragment {
    Button finishbutton, pausebutton, startbutton;
    TextView title, resttimertv;
    View view;
    String UserID;
    private Chronometer chronometer;
    private long stoppedAt = 0;
    private long elapsed;
    private boolean timerOn;
    RecyclerView currentworkoutview;
    private CurrentWorkoutadapter adapter;
    private int timerinms = 60000;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_currentworkout, container, false);
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
        Query query = db.getInstance().collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Exerciselist> options = new FirestoreRecyclerOptions.Builder<Exerciselist>()
                .setQuery(query, Exerciselist.class)
                .build();
        adapter = new CurrentWorkoutadapter(options, getContext(),CurrentWorkout.this);
        currentworkoutview = (RecyclerView) view.findViewById(R.id.currentworkoutview);
        currentworkoutview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        currentworkoutview.setLayoutManager(layoutManager);
        adapter.setWorkoutname(s);
        currentworkoutview.setNestedScrollingEnabled(false);
        currentworkoutview.setAdapter(adapter);

    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        finishbutton = (Button) view.findViewById(R.id.finishbutton);
        pausebutton = (Button) view.findViewById(R.id.pausebutton);
        startbutton = (Button) view.findViewById(R.id.startbutton);
        title = (TextView) view.findViewById(R.id.workoutname1);
        resttimertv = (TextView) view.findViewById(R.id.resttimertv);
        Spinner spinner = view.findViewById(R.id.spinner);
        finishbutton.setOnClickListener(this::buttonsclicked);
        startbutton.setOnClickListener(this::buttonsclicked);
        pausebutton.setOnClickListener(this::buttonsclicked);
        Bundle bundle = getArguments();
        assert bundle != null;
        String s = bundle.getString("id");
        title.setText(s);
        chronometer = view.findViewById(R.id.chronometer);
        chronometer.setFormat("Elapsed: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.timer_array, R.layout.spinneritem);
        arrayAdapter.setDropDownViewResource(R.layout.spinnerdropdown);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String spinnerselection = parent.getItemAtPosition(pos).toString();
                int spinnertime = Integer.valueOf(spinnerselection);
                timerinms = spinnertime * 1000;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }
    public void method() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    LinearLayout layout = new LinearLayout(getActivity());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    TextView timer = new TextView(getActivity());
                    timer.setTextSize(60);
                    timer.setPadding(60, 40, 40, 40);
                    layout.addView(timer);
                    alertDialogBuilder.setView(layout);
                    alertDialogBuilder.setTitle("REST TIMER")
                            .setPositiveButton("Finish rest", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    timer.setText("Rest Finished");
                                    dialog.dismiss();
                                }
                            });
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    CountDownTimer resttimer = new CountDownTimer(timerinms, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            timer.setText((millisUntilFinished / 60000) + ":" + (millisUntilFinished % 60000 / 1000));
                        }

                        @Override
                        public void onFinish() {
                            timer.setText("Rest Finished");
                            alertDialog.dismiss();
                        }
                    };
                    resttimer.start();

                }

    public void buttonsclicked(final View v) {
        switch (v.getId()) {
            case R.id.startbutton:
                if (!timerOn) {
                    chronometer.setBase(SystemClock.elapsedRealtime() + stoppedAt);
                    chronometer.start();
                    timerOn = true;
                }
                break;
            case R.id.pausebutton:
                if (timerOn) {
                    chronometer.stop();
                    stoppedAt = chronometer.getBase() - SystemClock.elapsedRealtime();
                    timerOn = false;
                }
                break;
            case R.id.finishbutton:
                if (timerOn) {
                    chronometer.stop();
                    elapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
                    stoppedAt = 0;
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    timerOn = false;
                } else {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    stoppedAt = 0;
                }
                saveworkout();
                break;
        }
    }

    public void saveworkout() {
        DateFormat dateformat = new SimpleDateFormat("EEE,MMM d,yyyy");
        String date = dateformat.format(Calendar.getInstance().getTime());
        int seconds = (int) (elapsed / 1000) % 60 ;
        int minutes = (int) ((elapsed / (1000*60)) % 60);
        int hours   = (int) ((elapsed / (1000*60*60)) % 24);
        String time = String.format("%d hrs,%d mins,%d sec",hours,minutes,seconds);
        Log.d("date","" + date );
        Log.d("time","" + time);
        final Dialog alertdialog = new Dialog(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        alertdialog.setContentView(R.layout.alertdialog4);
        final TextView titletv = alertdialog.findViewById(R.id.titletv);
        titletv.setText("Save Workout?");
        final Button create = alertdialog.findViewById(R.id.Create);
        final Button cancel = alertdialog.findViewById(R.id.Cancel);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                assert bundle != null;
                String s = bundle.getString("id");
                title.setText(s);
                String name = String.format(s + " - " + date);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateUI(user);
                Map<String, Object> save = new HashMap<>();
                save.put("workoutname", s);
                save.put("workout", name);
                save.put("time", time);
                save.put("date", FieldValue.serverTimestamp());
                db.collection("Users").document(UserID).collection("Saved Workouts").document(name).set(save)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                Fragment myFragment = new History();
                                Bundle bundle = new Bundle();
                                bundle.putString("name",name);
                                myFragment.setArguments(bundle);
                                activity.getSupportFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), myFragment)
                                        .addToBackStack(null)
                                        .commit();
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
