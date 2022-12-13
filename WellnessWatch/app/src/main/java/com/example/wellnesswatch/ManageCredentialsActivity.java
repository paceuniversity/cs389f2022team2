package com.example.wellnesswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ManageCredentialsActivity extends AppCompatActivity {

    Button btnChangeCredentials;
    EditText inputNewEmail, inputNewPassword, confirmNewPassword;
    FirebaseUser mUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_credentials);

        inputNewEmail = findViewById(R.id.editEmailAddress);
        inputNewPassword = findViewById(R.id.editPassword);
        confirmNewPassword = findViewById(R.id.confirmPassword);
        btnChangeCredentials = findViewById(R.id.changeCredentials);
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        ImageView back = (ImageView) findViewById(R.id.backButtonCredentials);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });

        btnChangeCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCredentials();
                Toast.makeText(getApplicationContext(), "Updated Credentials.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void changeCredentials() {
        String newEmail = inputNewEmail.getText().toString();
        String newPassword = inputNewPassword.getText().toString();
        String confirmPassword = confirmNewPassword.getText().toString();

        Log.wtf("pas", newEmail);
        if(!newEmail.isEmpty() && isValidEmail(newEmail)) {
            mUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Log.wtf("EMAIL","ChangedEmail");
                        //Need to also update the value in the db...
                        Toast.makeText(getApplicationContext(), "Updated Info", Toast.LENGTH_LONG);
                        FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid()).child("email").setValue(newEmail);
                    }else{
                        Log.wtf("F",task.getException().toString());
                    }
                }
            });
        }


        Log.wtf("pas", newPassword);
        if(!newPassword.equals(confirmPassword) && !newEmail.isEmpty()) {
            confirmNewPassword.setError("Your password does not match.");
        }else if(newPassword.length()<6){
            inputNewPassword.setError("Please enter a valid password.");
        }else{
            mUser.updatePassword(confirmPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Log.wtf("EMAIL","ChangedPass");
                        LogOut();
                    }
                }
            });
        }
    }


    private void LogOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private Boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}