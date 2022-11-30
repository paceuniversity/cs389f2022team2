package com.example.wellnesswatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpGoalsActivity extends AppCompatActivity {

    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser mUser= mAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_goals);

        Spinner fitnessGoals = findViewById(R.id.selectFitnessGoal);
        Spinner wellnessGoals = findViewById(R.id.selectWellnessGoal);
        Button submit = findViewById(R.id.submitGoals);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.fitnessGoals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        fitnessGoals.setAdapter(adapter);

        ArrayAdapter<CharSequence>adapter2=ArrayAdapter.createFromResource(this, R.array.wellnessGoals, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        wellnessGoals.setAdapter(adapter2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fitness = fitnessGoals.getSelectedItem().toString();
                String wellness = wellnessGoals.getSelectedItem().toString();

                FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).child("wellnessgoal").setValue(wellness);
                FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).child("fitnessgoal").setValue(fitness);
                FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).child("setgoals").setValue("true");
                Toast.makeText(SignUpGoalsActivity.this, "Goals set successfully!", Toast.LENGTH_LONG).show();
                sendUserHome();
            }
        });
    }
    private void sendUserHome() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}