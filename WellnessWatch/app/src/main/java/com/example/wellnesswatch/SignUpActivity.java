package com.example.wellnesswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    EditText inputEmail, inputName, inputPassword, inputConformPassword, inputPhone;
    Button btnRegister;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getSupportActionBar().hide();
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConformPassword = findViewById(R.id.inputConfirmPassword);
        inputPhone = findViewById(R.id.inputPhone);
        btnRegister = findViewById(R.id.signupButton);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        ImageView back = (ImageView) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserBack();
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateDetails();

            }
        });

    }

    private void ValidateDetails() {
        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConformPassword.getText().toString();
        String phone = inputPhone.getText().toString();


        if (name.isEmpty()) {
            inputName.setError("Please enter your name");

        } else if (!isValidEmail(email)) {
            inputEmail.setError("Please enter a valid email");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Please enter a valid password. Password must be longer then 6 characters.");
        } else if (!password.equals(confirmPassword)) {
            inputConformPassword.setError("Your password does not match.");
        } else if (phone.isEmpty()) {
            inputPhone.setError("Please enter a phone number.");
        } else {
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        User user = new User(name, email, phone);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            initExerciseList();
                                            setGoalsState();
                                            Toast.makeText(SignUpActivity.this, "User has been registered", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                            sendUserToNextActivity();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }else{
                        Toast.makeText(SignUpActivity.this, "Failed to register", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }


    }

    private void sendUserToNextActivity() {
        Intent i = new Intent(SignUpActivity.this, SignUpGoalsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void sendUserBack() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }

    private void initExerciseList() {
        Log.wtf("init", "init");
        List<String> defaultList = Arrays.asList("Select", "Add New");
        FirebaseDatabase.getInstance().getReference().child("Exercises").child(FirebaseAuth.getInstance().getUid()).child("ExerciseList").setValue(defaultList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.wtf("init", "init2");
                Log.d("FIREBASE", "ADDED");
            }
        });

    }

    private void setGoalsState() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("setgoals").setValue("false").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.wtf("init", "init2");
                Log.d("FIREBASE", "ADDED");
            }
        });

    }


    private Boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}