package com.example.wellnesswatch;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartWorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartWorkoutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;

    // TODO: Rename and change types of parameters


    public StartWorkoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartWorkoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StartWorkoutFragment newInstance(String param1, String param2) {
        StartWorkoutFragment fragment = new StartWorkoutFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_workout, container, false);
        TextView welcome  = (TextView) view.findViewById(R.id.welcomeText);
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();




        //welcome.setText(firebaseUser.getUid());
        reference= FirebaseDatabase.getInstance().getReference();

      /*  reference.child("Users").child(fireBaseAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    welcome.setText("Hello, "+task.getResult().getValue(User.class).getFullName());
                }
            }
        });

       */

        // Inflate the layout for this fragment
        Button btn = (Button) view.findViewById(R.id.startWorkoutButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.frameLayout, new WorkoutFragment());
                fr.commit();
            }
        });
        return view;

    }

}