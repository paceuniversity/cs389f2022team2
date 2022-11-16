package com.example.wellnesswatch;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_workout, container, false);


        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        TextView date  = (TextView) view.findViewById(R.id.date);
        date.setText(date_n);

        // Get the widgets reference from XML layout
        Spinner selectWorkout = view.findViewById(R.id.selectWorkout);
        Spinner selectType = view.findViewById(R.id.selectType);
        EditText setsText = view.findViewById(R.id.setAmount);
        EditText amountText = view.findViewById(R.id.inputAmount);
        EditText lbs = view.findViewById(R.id.lbsAmount);
        TextView ofText = view.findViewById(R.id.ofText);
        Button addWorkout = (Button) view.findViewById(R.id.addWorkout);
        Button endWorkout = (Button) view.findViewById(R.id.endWorkout);
        TextView displayWorkout = (TextView) view.findViewById(R.id.displayWorkout);
        StringBuilder sb = new StringBuilder();
        Queue<WorkoutObject> workout = new LinkedList<>();

        //Need to refactor how this works...

        // Initializing a String Array
        String[] exercise = new String[]{
                "Select",
                "Push"
        };
        String[] options = new String[]{
                "Select",
                "Minutes",
                "Seconds",
                "Reps",
                "Sets"
        };


        // Convert array to a list
        List<String> exerciseList = new ArrayList<>
                (Arrays.asList(exercise));

        List<String> optionList = new ArrayList<>
                (Arrays.asList(options));


        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter
                = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                exerciseList
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



        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                WorkoutObject ob = new WorkoutObject(selectWorkout.getSelectedItem().toString(),amountText.getText().toString(), selectType.getSelectedItem().toString());
                workout.add(ob);
                sb.append(ob.toString()+"\n");
                //sb.append(selectWorkout.getSelectedItem()+ ", "+amountText.getText()+ " "+selectType.getSelectedItem() + ", "+lbs.getText()+"lbs \n");
                displayWorkout.setText(sb.toString());
                Log.d("QUEUE",workout.toString());

            }
        });

        endWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayWorkout.setText(workout.toString());
            }
        });

        return view;
    }
}