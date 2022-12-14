package com.example.wellnesswatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    public List<String> eList;
    public int sec =0;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkoutFragment newInstance(String param1, String param2) {
        WorkoutFragment fragment = new WorkoutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_workout, container, false);
        startTimer(view);

        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        TextView date  = (TextView) view.findViewById(R.id.date);
        date.setText(date_n);

        // Get the widgets reference from XML layout
        Spinner selectWorkout = view.findViewById(R.id.selectWorkout);
        Spinner selectType = view.findViewById(R.id.selectType);
        EditText setsText = view.findViewById(R.id.setAmount);
        EditText amountText = view.findViewById(R.id.inputAmount);
        EditText lbs = view.findViewById(R.id.lbsAmount);
        EditText selectWorkoutTxt = view.findViewById(R.id.inputWorkoutText);
        TextView ofText = view.findViewById(R.id.ofText);
        TextView timerText = view.findViewById(R.id.timerText);
        Button addWorkout = (Button) view.findViewById(R.id.addWorkout);
        Button endWorkout = (Button) view.findViewById(R.id.endWorkout);
        TextView displayWorkout = (TextView) view.findViewById(R.id.displayWorkout);
        StringBuilder sb = new StringBuilder();
        Queue<WorkoutObject> workout = new LinkedList<>();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String userId= mUser.getUid();
        eList = new ArrayList<>();
        setExerciseList();


        //Need to refactor how this works...

        String[] options = new String[]{
                "Select",
                "Minutes",
                "Seconds",
                "Reps",
                "Sets"
        };


        // Convert array to a list
            List<String> optionList = new ArrayList<>
                (Arrays.asList(options));


        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter
                = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                eList
        ){

            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }
            @Override
            public View getDropDownView(
                    int position, View convertView,
                    @NonNull ViewGroup parent) {

                // Get the item view
                View view = super.getDropDownView(
                        position, convertView, parent);
                TextView textView = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    textView.setTextColor(Color.GRAY);
                }
                else { textView.setTextColor(Color.BLACK); }
                return view;
            }
        };

        ArrayAdapter<String> spinnerArrayAdapter2
                = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                optionList
        ){

            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }
            @Override
            public View getDropDownView(
                    int position, View convertView,
                    @NonNull ViewGroup parent) {

                // Get the item view
                View view = super.getDropDownView(
                        position, convertView, parent);
                TextView textView = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    textView.setTextColor(Color.GRAY);
                }
                else { textView.setTextColor(Color.BLACK); }
                return view;
            }
        };

        // Set the drop down view resource
        spinnerArrayAdapter.setDropDownViewResource(
                android.R.layout.simple_dropdown_item_1line
        );
        spinnerArrayAdapter2.setDropDownViewResource(
                android.R.layout.simple_dropdown_item_1line
        );


        // Spinner on item selected listener
        selectWorkout.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view,
                            int position, long id) {

                        // Get the spinner selected item text
                        String selectedItemText = (String) parent
                                .getItemAtPosition(position);

                        // If user change the default selection
                        // First item is disable and
                        // it is used for hint
                        if(position > 0){
                            // Notify the selected item text
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Selected : "
                                            + selectedItemText,
                                    Toast.LENGTH_SHORT).show();
                        }

                        if(selectedItemText.equals("Add New")) {
                            selectWorkout.setVisibility(View.GONE);
                            selectWorkoutTxt.setVisibility(View.VISIBLE);

                        }
                    }


                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {
                    }
                });

        selectType.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent, View view,
                            int position, long id) {

                        // Get the spinner selected item text
                        String selectedItemText = (String) parent
                                .getItemAtPosition(position);

                        // If user change the default selection
                        // First item is disable and
                        // it is used for hint
                        if(position > 0){
                            // Notify the selected item text
                            Toast.makeText(
                                    getActivity().getApplicationContext(),
                                    "Selected : "
                                            + selectedItemText,
                                    Toast.LENGTH_SHORT).show();
                        }

                        if(selectedItemText.equals("Sets")) {
                            lbs.setX(lbs.getX()+180);
                            selectType.setX(selectType.getX()-50);
                            setsText.setVisibility(View.VISIBLE);
                            ofText.setVisibility(View.VISIBLE);
                        }else{
                            if(setsText.getVisibility()==View.VISIBLE) {
                                setsText.setVisibility(View.GONE);
                                ofText.setVisibility(View.GONE);
                                lbs.setX(lbs.getX()-180);
                                selectType.setX(selectType.getX()+50);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {
                    }
                });

        // Finally, data bind the spinner object with adapter
        selectWorkout.setAdapter(spinnerArrayAdapter);
        selectType.setAdapter(spinnerArrayAdapter2);

        //Handle when a user clicks Add+
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exercise = selectWorkout.getVisibility()==View.VISIBLE ? selectWorkout.getSelectedItem().toString() : selectWorkoutTxt.getText().toString();
                String amountInput = amountText.getText().toString();
                String typeInput = selectType.getSelectedItem().toString();
                String lbsInput = lbs.getText().toString();
                String setAmountInput = setsText.getText().toString();
                Log.wtf("reps",setAmountInput);
                Log.wtf("lbs",lbsInput);
                WorkoutObject workoutObject;
                //Need to also handle when sets is selected, but the additional field is not entered..
                if (!amountInput.isEmpty() && exercise!="Select" && typeInput!="Select") {
                    checkAndAddExercise(exercise);
                    //Decide which constructor to use..lbsInput
                    if(!setAmountInput.isEmpty() && !lbsInput.isEmpty()) {
                        workoutObject = new WorkoutObject(exercise ,amountInput, typeInput, Integer.parseInt(setAmountInput), Integer.parseInt(lbsInput));
                    }else if (!setAmountInput.isEmpty() && lbsInput.isEmpty()) {
                        workoutObject = new WorkoutObject(exercise ,amountInput, typeInput, Integer.parseInt(setAmountInput), "rep");
                    }else if (!lbsInput.isEmpty()) {
                        workoutObject = new WorkoutObject(exercise ,amountInput, typeInput, Integer.parseInt(lbsInput), "lbs");
                    }else{
                        workoutObject = new WorkoutObject(exercise ,amountInput, typeInput);
                    }
                    workout.add(workoutObject);
                    sb.append(workoutObject.toString()+"\n");
                    displayWorkout.setText(sb.toString());

                    //Clear fields --put into own method..
                    if(selectWorkout.getVisibility()!=View.VISIBLE) {
                        selectWorkoutTxt.getText().clear();
                        selectWorkoutTxt.setVisibility(View.GONE);
                        selectWorkout.setVisibility(View.VISIBLE);
                    }
                    selectWorkout.setAdapter(spinnerArrayAdapter);
                    selectType.setAdapter(spinnerArrayAdapter2);
                    spinnerArrayAdapter.notifyDataSetChanged();
                    amountText.getText().clear();
                    lbs.getText().clear();
                    setsText.getText().clear();
                    Log.wtf("alist",eList.toString());
                }else{
                    //amountText.setError("Please enter a valid input.");
                    Toast.makeText(getActivity(), "Make sure all required fields are not empty!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        endWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(workout.isEmpty()) {
                    goHome();
                }else{
                    uploadDatatoDB(date_n, timerText.getText().toString(), workout, userId);
                    goHome();
                    Toast.makeText(getActivity(), "Workout Logged!", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    public void uploadDatatoDB( String date, String duration, Queue<WorkoutObject> queue, String userId) {
        if(queue.isEmpty()) return;
        String key = mDatabase.push().getKey();
        Map<String, Object> userData = new HashMap <>();
        userData.put("duration", duration);
        userData.put("workout", queue.toString());
        mDatabase.child("Workouts").child(userId).child(date).child(key).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("FIREBASE", "ADDED");
            }
        });
    }



//This is how the data will be read then parsed from the db.
/*
    public void getDataFromDB() {
        mDatabase= FirebaseDatabase.getInstance().getReference("Workouts");
        mDatabase.child(mUser.getUid()).child("Nov 18, 2022").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    Log.d("DURATION", map.toString());
                    Set<String> keys = map.keySet();
                    Log.wtf("keyset", keys.toString());

                    for(String key : map.keySet()) {
                        String a = ((Map)map.get(key)).get("workout").toString();
                        Log.wtf("MAP",a);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
 */

    private void checkAndAddExercise(String exercise) {
        Log.wtf("STARR","STARTT");
        mDatabase.child("Exercises").child(mUser.getUid()).child("ExerciseList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    List<Object> list = (List<Object>) snapshot.getValue();
                    if(!list.contains(exercise)) {
                        eList.add(exercise);
                        mDatabase.child("Exercises").child(mUser.getUid()).child("ExerciseList").setValue(eList).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("FIREBASE", "ADDED");
                            }
                        });
                    }else{
                        return;
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Populate spinner with users past exercises..
    public void setExerciseList() {
        eList.add("Select");
        eList.add("Add New");
        mDatabase.child("Exercises").child(mUser.getUid()).child("ExerciseList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for(DataSnapshot item: snapshot.getChildren()) {
                        if(!eList.contains(item.getValue()))
                            eList.add(item.getValue().toString());
                    }
                }
            }
/*
            public void setExerciseList() {
                mDatabase.child("Exercises").child(mUser.getUid()).child("ExerciseList").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            eList = (ArrayList<String>) snapshot.getValue();
                            Log.wtf("theList",eList.toString());
                        }
                    }

 */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startTimer(View view) {
        final TextView timer = view.findViewById(R.id.timerText);
        final Handler handle = new Handler();
        handle.post(new Runnable() {
            @Override
            public void run() {
                int hrs = sec / 3600;
                int mins = (sec % 3600) / 60;
                int secs = sec % 60;
                String time_t = String.format(Locale.getDefault(), "    %d:%02d:%02d   ", hrs, mins, secs);
                timer.setText(time_t);
                sec++;
                handle.postDelayed(this, 1000);
            }
        });
    }


    private void goHome() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }
}