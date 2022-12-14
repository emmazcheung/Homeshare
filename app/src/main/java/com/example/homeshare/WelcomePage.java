package com.example.homeshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomePage extends AppCompatActivity {
    FirebaseDatabase root;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        auth = FirebaseAuth.getInstance();

        TextView submitBtn = (TextView) findViewById(R.id.submitWelcomeButton);
        submitBtn.setOnClickListener(this::handleSubmitWelcome);
    }

    public static Boolean allFieldsFilledOut(String fName, String lName, String gender, String year, String bio) {
        return fName.length() > 0 && lName.length() > 0 && gender.length() > 0 && year.length() > 0 && bio.length() > 0;
    }

    public void handleSubmitWelcome(View view) {
        String fName = ((EditText) findViewById(R.id.firstName)).getText().toString();
        String lName = ((EditText) findViewById(R.id.lastName)).getText().toString();
        int selectedGenderId = ((RadioGroup) findViewById(R.id.welcomeGenderRadioGroup)).getCheckedRadioButtonId();
        String gender = "";
        if (selectedGenderId != -1) {
            gender = ((RadioButton) findViewById(selectedGenderId)).getText().toString();
        }
        String year = ((EditText) findViewById(R.id.yearInSchool)).getText().toString();
        String bio = ((TextView) findViewById(R.id.welcomeUserBio)).getText().toString();

        if (allFieldsFilledOut(fName, lName, gender, year, bio)) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            // add to user db
            root = FirebaseDatabase.getInstance();
            User user = new User(fName, lName, email, gender, year, bio);
            root.getReference().child("Users/"+uid).setValue(user);

            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            return;
        } else {
            TextView errMsg = findViewById(R.id.welcomeErrMsg);
            errMsg.setText("Please fill in all fields!");
        }
    }
}