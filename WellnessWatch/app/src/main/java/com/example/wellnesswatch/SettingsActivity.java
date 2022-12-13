package com.example.wellnesswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    FirebaseUser mUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        ImageView back = (ImageView) findViewById(R.id.backButtonSettings);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        ImageView toReportProblem = (ImageView) findViewById(R.id.forwardButtonOne);
        toReportProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReportProblemActivity.class));
            }
        });

        ImageView toManageCredentials = (ImageView) findViewById(R.id.forwardButtonTwo);
        toManageCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManageCredentialsActivity.class));
            }
        });

        Button deleteAccount = (Button) findViewById(R.id.deleteAccount);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUserDeletion();
            }
        });
    }

    private void confirmUserDeletion() {
        AlertDialog.Builder confirmDeletion = new AlertDialog.Builder(this);
        confirmDeletion.setTitle("Confirm deletion");
        confirmDeletion.setMessage("Are you sure you want to delete your account? This cannot be undone.");
        confirmDeletion.setCancelable(false);
        confirmDeletion.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser();
                Toast.makeText(getApplicationContext(), "Your account has been deleted.", Toast.LENGTH_LONG).show();
            }
        });
        confirmDeletion.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Your account has not been deleted.", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog confirmDeletionAlert = confirmDeletion.create();
        confirmDeletionAlert.show();
    }

    private void deleteUser() {
        mUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                LogOut();
                FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Exercises").child(mUser.getUid()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Workouts").child(mUser.getUid()).removeValue();
            }
        });
    }

    public void LogOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}