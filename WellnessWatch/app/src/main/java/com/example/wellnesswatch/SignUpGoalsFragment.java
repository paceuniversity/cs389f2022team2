package com.example.wellnesswatch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpGoalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpGoalsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpGoalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpGoalsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpGoalsFragment newInstance(String param1, String param2) {
        SignUpGoalsFragment fragment = new SignUpGoalsFragment();
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
        View view = inflater.inflate(R.layout.activity_sign_up_goals, container, false);

        Spinner fitnessGoals = view.findViewById(R.id.selectFitnessGoal);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(getActivity(), R.array.fitnessGoals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        fitnessGoals.setAdapter(adapter);

        Spinner wellnessGoals = view.findViewById(R.id.selectWellnessGoal);
        ArrayAdapter<CharSequence>adapter2=ArrayAdapter.createFromResource(getActivity(), R.array.wellnessGoals, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        wellnessGoals.setAdapter(adapter2);

        return view;
    }

}