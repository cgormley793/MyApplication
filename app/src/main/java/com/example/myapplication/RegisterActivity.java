package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar_UsrReg);
        progressBar.setVisibility(View.INVISIBLE);

        mDatabase = FirebaseDatabase.getInstance().getReference("user");
    }




    public void registerUser(View view) {
        //getting fullname  email and password from edit text
        String email = ((EditText) findViewById(R.id.editText_UserEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.editText_Password)).getText().toString();
        String Reenterpassword = ((EditText) findViewById(R.id.editText_rePassword)).getText().toString();
        String fullname = ((EditText) findViewById(R.id.editText_fullname)).getText().toString();

        mDatabase.child("email").setValue(email);
        mDatabase.child("password").setValue(password);
        mDatabase.child("Reenterpassword").setValue(Reenterpassword);
        mDatabase.child("fullname").setValue(fullname);



        //checking if fullname, email amd password are empty
        if (TextUtils.isEmpty(fullname)){
            Toast.makeText(this, "Please enter Fullname", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(Reenterpassword)){
            Toast.makeText(this, "Please Re enter Password", Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(Reenterpassword)){
            Toast.makeText(this, "Password do not match", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length() < 5){
            Toast.makeText(getApplicationContext(), "Password is not strong enough", Toast.LENGTH_SHORT).show();
            return;
        }




        //if the email and password are not empty, display a progress bar
        progressBar.setVisibility(view.VISIBLE);
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            //display some message here
                            Toast.makeText(RegisterActivity.this, "Successfully Registered",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            //display some message here
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(RegisterActivity.this, "Failed to Register:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void goLogin(View view) {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

}