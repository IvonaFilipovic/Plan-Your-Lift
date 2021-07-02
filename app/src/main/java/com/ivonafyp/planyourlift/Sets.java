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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Sets extends Fragment {
    View view;
    String UserID;
    ImageButton backbutton;
    RecyclerView Setsview;
    private Setsadapter adapter;
    ExtendedFloatingActionButton fab;
    TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sets, container, false);
        RecyclerView();
        return view;
    }

    private void RecyclerView() {
        Bundle bundle = getArguments();
        assert bundle != null;
        String exname = bundle.getString("exname");
        String s = bundle.getString("id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();
        updateUI(user);
        Query query = db.getInstance().collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname).collection(exname).orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Setslist> options = new FirestoreRecyclerOptions.Builder<Setslist>()
                .setQuery(query, Setslist.class)
                .build();
        adapter = new Setsadapter(options);
        Setsview = (RecyclerView) view.findViewById(R.id.setsrv);
        Setsview.setHasFixedSize(true);
        Setsview.setLayoutManager(new LinearLayoutManager(getActivity()));
        Setsview.setAdapter(adapter);
        adapter.setWorkoutname(s);
        adapter.setExname(exname);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab = (ExtendedFloatingActionButton) view.findViewById(R.id.addbutton);
        title = (TextView) view.findViewById(R.id.workoutname1);
        backbutton = (ImageButton) view.findViewById(R.id.backbutton);
        Bundle bundle = getArguments();
        assert bundle != null;
        String exname = bundle.getString("exname");
        String s = bundle.getString("id");
        String units = bundle.getString("units");
        title.setText(exname);

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       final Dialog alertdialog = new Dialog(getActivity());
                                       LayoutInflater inflater = requireActivity().getLayoutInflater();
                                       alertdialog.setContentView(R.layout.alertdialog3);
                                       final EditText repsinput = alertdialog.findViewById(R.id.userinput);
                                       final EditText valueinput = alertdialog.findViewById(R.id.userinput1);
                                       final Button create = alertdialog.findViewById(R.id.Create);
                                       final Button cancel = alertdialog.findViewById(R.id.Cancel);
                                       create.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               FirebaseFirestore db = FirebaseFirestore.getInstance();
                                               FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                               updateUI(user);
                                               String repsS = (repsinput.getText().toString().isEmpty()) ? "" : repsinput.getText().toString();
                                               int repsint = (repsS == "") ? 0 : Integer.parseInt(repsS);
                                               String valueS = (valueinput.getText().toString().isEmpty()) ? "" : valueinput.getText().toString();
                                               int valueint = (valueS == "") ? 0 : Integer.parseInt(valueS);
                                               Map<String, Object> map = new HashMap<>();
                                               if (!repsS.matches("")) {
                                                   map.put("reps", repsint);
                                               }
                                               if (!valueS.matches("")) {
                                                   map.put("value", valueint);
                                               }
                                               map.put("units", units);
                                               map.put("date", FieldValue.serverTimestamp());

                                               db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname).collection(exname).document().set(map)
                                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                           @Override
                                                           public void onSuccess(Void aVoid) {
                                                               DocumentReference ref = db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname);
                                                               ref.update("setcounter", FieldValue.increment(1));
                                                               repsinput.setText("");
                                                               valueinput.setText("");
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
                               });

        /*
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText repsinput = new EditText(getActivity());
        repsinput.setHint("Reps");
        final EditText valueinput = new EditText(getActivity());
        valueinput.setHint("Weight/Time");
        layout.addView(repsinput);
        layout.addView(valueinput);
        alertDialogBuilder.setTitle("Write number of reps and weight/time for this set");
        alertDialogBuilder.setView(layout);
        alertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateUI(user);
                String repsS = (repsinput.getText().toString().isEmpty()) ? "" : repsinput.getText().toString();
                int repsint = (repsS == "") ? 0 : Integer.parseInt(repsS);
                String valueS = (valueinput.getText().toString().isEmpty()) ? "" : valueinput.getText().toString();
                int valueint = (valueS == "") ? 0 : Integer.parseInt(valueS);
                Map<String, Object> map = new HashMap<>();
                if (!repsS.matches("")) {
                    map.put("reps", repsint);
                }
                if (!valueS.matches("")) {
                    map.put("value", valueint);
                }
                map.put("units",units);
                map.put("date", FieldValue.serverTimestamp());

                db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname).collection(exname).document().set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DocumentReference ref = db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname);
                                ref.update("setcounter", FieldValue.increment(1));
                                repsinput.setText("");
                                valueinput.setText("");
                            }


                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }


        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

*/

        adapter.readcallback(new Setsadapter.callback() {
            @Override
            public void oncallback(DocumentSnapshot documentSnapshot, int position) {
                String docid = documentSnapshot.getId();
                final Dialog alertdialog = new Dialog(getActivity());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                alertdialog.setContentView(R.layout.alertdialog3);
                final EditText repsinput = alertdialog.findViewById(R.id.userinput);
                final EditText valueinput = alertdialog.findViewById(R.id.userinput1);
                final Button create = alertdialog.findViewById(R.id.Create);
                final Button cancel = alertdialog.findViewById(R.id.Cancel);
                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        updateUI(user);
                        String repsS = (repsinput.getText().toString().isEmpty()) ? "" : repsinput.getText().toString();
                        int repsint = (repsS == "") ? 0 : Integer.parseInt(repsS);
                        String valueS = (valueinput.getText().toString().isEmpty()) ? "" : valueinput.getText().toString();
                        int valueint = (valueS == "") ? 0 : Integer.parseInt(valueS);
                        Map<String, Object> map = new HashMap<>();
                        if (!repsS.matches("")) {
                            map.put("reps", repsint);
                        }
                        if (!valueS.matches("")) {
                            map.put("value", valueint);
                        }
                        Integer counter = adapter.getItemCount();
                        db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname).collection(exname).document(docid).set(map, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        repsinput.setText("");
                                        valueinput.setText("");
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
          });
                /*
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText repsinput = new EditText(getActivity());
                repsinput.setHint("Reps");
                final EditText valueinput = new EditText(getActivity());
                valueinput.setHint("Weight");
                layout.addView(repsinput);
                layout.addView(valueinput);
                alertDialogBuilder.setTitle("Write number of reps and weight for this set");
                alertDialogBuilder.setView(layout);
                alertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        updateUI(user);
                        String repsS = (repsinput.getText().toString().isEmpty()) ? "" : repsinput.getText().toString();
                        int repsint = (repsS == "") ? 0 : Integer.parseInt(repsS);
                        String valueS = (valueinput.getText().toString().isEmpty()) ? "" : valueinput.getText().toString();
                        int valueint = (valueS == "") ? 0 : Integer.parseInt(valueS);
                        Map<String, Object> map = new HashMap<>();
                        if (!repsS.matches("")) {
                            map.put("reps", repsint);
                        }
                        if (!valueS.matches("")) {
                            map.put("value", valueint);
                        }
                        Integer counter = adapter.getItemCount();
                        db.collection("Users").document(UserID).collection("Workouts").document(s).collection("exercises").document(exname).collection(exname).document(docid).set(map,SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        repsinput.setText("");
                                        valueinput.setText("");
                                    }


                                })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }


                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

        });
        */

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
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














