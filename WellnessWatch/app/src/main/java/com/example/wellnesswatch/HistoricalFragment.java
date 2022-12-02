package com.example.wellnesswatch;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoricalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoricalFragment extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private TextView displayWorkout;


    public HistoricalFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HistoricalFragment newInstance(String param1, String param2) {
        HistoricalFragment fragment = new HistoricalFragment();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_historical, container, false);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        displayWorkout = view.findViewById(R.id.textView5);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser() ;


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // textView.setText(month+1 + "/" + dayOfMonth + "/" + year);
                getDataFromDB(month, dayOfMonth, year);
            }
        });
        Button btn = (Button) view.findViewById(R.id.allWorkoutsButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.frameLayout, new RecallFragment());
                fr.commit();
            }
        });
        Button btn2 = (Button) view.findViewById(R.id.recallButton);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.frameLayout, new WorkoutListFragment());
                fr.commit();
            }
        });

        return view;
    }

    public void getDataFromDB(int month, int day, int year) {
        //Format the date so it matches the format of the DB.
        String[] shortMonths = new DateFormatSymbols().getShortMonths();
        String date = shortMonths[month]+" "+String.format("%02d",day)+", "+year;
        Log.wtf("date",date);
        mDatabase= FirebaseDatabase.getInstance().getReference("Workouts");
        mDatabase.child(mUser.getUid()).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Log.wtf("COUNT", String.valueOf(snapshot.getChildrenCount()));
                    String fetchedFromDB="";
                    String duration="";
                    StringBuilder sb = new StringBuilder();
                    sb.append(date+"\n");

                    //The db will return a nested hashmap, which needs to be parsed.
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    Log.wtf("1", map.toString());

                    //Handle if the user did myltiple workouts that day..
                    //Works... probably need to refactor..
                    if(snapshot.getChildrenCount()>1) {
                        int counter=1;
                            for(String key : map.keySet()) {
                                sb.append("Workout "+counter+++"\n");
                                fetchedFromDB = ((Map)map.get(key)).get("workout").toString();
                                duration = ((Map)map.get(key)).get("duration").toString();
                                sb.append(duration+"\n");
                                Log.wtf("3", fetchedFromDB);
                                fetchedFromDB = fetchedFromDB.replaceAll("[\\[\\](){}]","");
                                String[] parseData = fetchedFromDB.split(",");
                                for(String data : parseData) {
                                    Log.wtf("4", data);
                                    sb.append(data+"\n");
                                }
                            }
                        displayWorkout.setText(sb.toString());
                    }else{
                        for(String key : map.keySet()) {
                            fetchedFromDB = ((Map)map.get(key)).get("workout").toString();
                            duration = ((Map)map.get(key)).get("duration").toString();
                            fetchedFromDB = fetchedFromDB.replaceAll("[\\[\\](){}]","");
                        }
                        sb.append(duration+"\n");
                        String[] parseData = fetchedFromDB.split(",");
                        for(String data : parseData) {
                            sb.append(data+"\n");
                        }
                        displayWorkout.setText(sb.toString());
                    }
                }else{
                    displayWorkout.setText(date+"\n No Workout");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayWorkout.setText("Error reading from DB");
            }
        });
    }
}