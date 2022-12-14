package com.example.wellnesswatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutListFragment extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    public List<String> eList;
    private TextView displayExercises;


    public WorkoutListFragment() {
        // Required empty public constructor
    }

    public static WorkoutListFragment newInstance(String param1, String param2) {
        WorkoutListFragment fragment = new WorkoutListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_workout_list, container, false);


        displayExercises = view.findViewById(R.id.displayExercises);
        eList= new ArrayList<>();
        setExerciseList();
        return view;
    }

    public void setExerciseList() {
        StringBuilder sb = new StringBuilder();
        List<String> eList = new ArrayList<>();
        mDatabase.child("Exercises").child(mUser.getUid()).child("ExerciseList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot item : snapshot.getChildren()) {
                        String value = item.getValue().toString();
                        if(!value.equals("Select") && !value.equals("Add New")) {
                            eList.add(item.getValue().toString());
                        }
                    }
                    Collections.sort(eList, String.CASE_INSENSITIVE_ORDER);
                    for(String exercise: eList) {
                        sb.append(exercise+"\n");
                    }
                    displayExercises.setText(sb.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayExercises.setText("Error");

            }
        });
    }
}