package com.example.wellnesswatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WellnessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WellnessFragment extends Fragment {

    ListView LvRss;
    TextView userName, wellnessGoal,fitnessGoal,greetingText;
    ArrayList<String> titles;
    ArrayList<String> links;
    FirebaseAuth mAuth;
    String IS_WORKING;
    boolean isEditing = false;
    String rssFeed;




    public WellnessFragment() {
        // Required empty public constructor
    }

    public static WellnessFragment newInstance(String param1, String param2) {
        WellnessFragment fragment = new WellnessFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wellness, container, false);
        //userName = (TextView) view.findViewById(R.id.userName);
        fitnessGoal = (TextView) view.findViewById(R.id.fitnessGoal);
        wellnessGoal= (TextView) view.findViewById(R.id.wellnessGoal);
        greetingText= (TextView) view.findViewById(R.id.greetingText);
        isEditing=true;

        mAuth = FirebaseAuth.getInstance();
        setUserData();


        LvRss = (ListView) view.findViewById(R.id.LvRss);
        titles = new ArrayList<String>();
        links = new ArrayList<String>();
        LvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(links.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        return view;
    }

    public InputStream getInputStream(URL url) {
        try
        {
            return url.openConnection().getInputStream();
        }

        catch (IOException e)
        {
            return null;
        }
    }

    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception>
    {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());

        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Loading articles... please wait");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {

            try
            {
                Log.wtf("feed",rssFeed);
                URL url = new URL(rssFeed);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(getInputStream(url), "UTF_8");

                boolean insideItem = false;

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    if (eventType == XmlPullParser.START_TAG)
                    {
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            insideItem = true;
                        }
                        else if (xpp.getName().equalsIgnoreCase("title"))
                        {
                            if (insideItem)
                            {
                                titles.add(xpp.nextText());
                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("link"))
                        {
                            if (insideItem)
                            {
                                links.add(xpp.nextText());
                            }
                        }
                    }
                    else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
                    {
                        insideItem = false;
                    }

                    eventType = xpp.next();
                }


            }
            catch (MalformedURLException e)
            {
                exception = e;
            }
            catch (XmlPullParserException e)
            {
                exception = e;
            }
            catch (IOException e)
            {
                exception = e;
            }


            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titles);

            LvRss.setAdapter(adapter);


            progressDialog.dismiss();
        }
    }

    public void setUserData() {
        StringBuilder sb = new StringBuilder();
        Date dt = new Date();
        int hours = dt.getHours();
        if(hours>=0 && hours<=12){
            sb.append("Good Morning,");
        }else if(hours>=12 && hours<=16){
            sb.append("Good Afternoon,");
        }else if(hours>=16 && hours<=21){
           sb.append("Good Evening,");
        }else if(hours>=21 && hours<=24){
            sb.append("Good Night,");
        }

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                    String name[]  = userData.get("fullName").toString().split(" ");
                    sb.append(" "+name[0]);
                    fitnessGoal.setText(userData.get("fitnessgoal").toString());
                    wellnessGoal.setText(userData.get("wellnessgoal").toString());
                    getRssFeed(userData.get("wellnessgoal").toString());
                }
                new ProcessInBackground().execute();
                greetingText.setText(sb.toString());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getRssFeed(String goal) {
        if(goal.contains("healthier")) {
            rssFeed = "https://www.skinnytaste.com/feed/";
        }
        else if (goal.contains("stress")) {
            rssFeed = "https://www.stress.org/feed";
        }
        else if (goal.contains("anxiety")) {
            rssFeed = "https://anxiouslass.com/feed/";
        }
        else if (goal.contains("better sleep")) {
            rssFeed = "https://anxiouslass.com/feed/";
        }
        else if (goal.contains("positivity")) {
            rssFeed = "https://anxiouslass.com/feed/";
        }
        else if (goal.contains("meditating")) {
            rssFeed = "https://anxiouslass.com/feed/";
        }
    }

}