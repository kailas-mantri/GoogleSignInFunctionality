package com.scrimatec.scrimainsertdata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText text,text1;
    Button button;

    DatabaseReference reference;
    String log,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text=findViewById(R.id.login);
        text1=findViewById(R.id.pass);

        reference = FirebaseDatabase.getInstance().getReference("User");
        button=findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log = text.getText().toString();
                pass = text1.getText().toString();

                if (TextUtils.isEmpty(log)){
                    text.setError("Enter the user details");
                    text.requestFocus();
                }
                else if (TextUtils.isEmpty(pass)){
                    text1.setError("Please Enter your password");
                    text1.requestFocus();
                }
                else{

                    String uid = reference.push().getKey();

                    User user= new User(uid,log,pass);
                    reference.child(uid).setValue(user);
                    text.setText("");
                    text1.setText("");
                    Toast.makeText(MainActivity.this, "Data Insertion Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class User {

        String uid, username, passcode;

        public User(String uid, String username, String passcode) {

            this.uid = uid;
            this.username = username;
            this.passcode = passcode;
        }

        public User() { }

        public String getUid() { return uid; }

        public void setUid(String uid) { this.uid = uid; }

        public String getUsername() { return username; }

        public void setUsername(String username) { this.username = username; }

        public String getPasscode() { return passcode; }

        public void setPasscode(String passcode) { this.passcode = passcode; }
    }
}