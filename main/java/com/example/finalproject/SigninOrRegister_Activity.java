package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SigninOrRegister_Activity extends menuActivity implements View.OnClickListener{
    EditText et_username;
    EditText et_pass_word;
    Button btn_log_in ;
    Button btn_register ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    static public friend friendUsingThisPhone = null;
    Intent myNotificationService ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_or_register);
        et_username = (EditText) findViewById(R.id.et_username);
        et_pass_word = (EditText) findViewById(R.id.et_pass_word);
        btn_log_in = (Button) findViewById(R.id.btn_log_in);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_log_in.setOnClickListener(this);
        btn_register.setOnClickListener(this);

    }





    public void onClick(View view){
        if((et_username.getText().toString().trim().length() == 0) || et_pass_word.getText().toString().trim().length() == 0){
            Toast.makeText(SigninOrRegister_Activity.this,"enter details", Toast.LENGTH_LONG).show();
        }
        else if(view == btn_register) {
            Query q = myRef.child("App Users").orderByValue();
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean userTaken = false;
                    for (DataSnapshot dst : snapshot.getChildren()) {
                        friend sub = dst.getValue(friend.class);
                        if (sub.getUsername().equals(et_username.getText().toString())){
                            Toast.makeText(SigninOrRegister_Activity.this, "Username already taken, choose a different one",Toast.LENGTH_LONG).show();
                            userTaken = true;
                        }
                    }

                    if (!userTaken)
                    {

                        friendUsingThisPhone = new friend(et_username.getText().toString(),et_pass_word.getText().toString());
                        myRef.child("App Users").child(friendUsingThisPhone.getUsername()).setValue(friendUsingThisPhone);
                        Toast.makeText(SigninOrRegister_Activity.this, "registered successfully",Toast.LENGTH_LONG).show();
                        myNotificationService = new Intent(SigninOrRegister_Activity.this,MyFirebase_Notification_Service.class);
                        startService(myNotificationService);

                        Intent i1 = new Intent(SigninOrRegister_Activity.this, friendDetails.class);
                        startActivity(i1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        else if(view == btn_log_in){
            Query q = myRef.child("App Users").orderByValue();
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean userFound = false;
                    for (DataSnapshot dst : snapshot.getChildren()) {
                        friend f = dst.getValue(friend.class);
                        if (f.getUsername().equals(et_username.getText().toString()) && f.getPassword().equals(et_pass_word.getText().toString())) {
                            friendUsingThisPhone = new friend(et_username.getText().toString(),et_pass_word.getText().toString());
                            Toast.makeText(SigninOrRegister_Activity.this, "logged in successfully", Toast.LENGTH_LONG).show();
                            myNotificationService = new Intent(SigninOrRegister_Activity.this,MyFirebase_Notification_Service.class);
                            startService(myNotificationService);
                            userFound = true;
                        }
                    }

                    if (userFound) {
                        Intent i1 = new Intent(SigninOrRegister_Activity.this, MainActivityFriends.class);
                        startActivity(i1);
                    }
                    else
                        Toast.makeText(SigninOrRegister_Activity.this, "username or password are incorrect", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




    }

    @Override
    protected void onDestroy() {
        stopService(myNotificationService);
        super.onDestroy();
    }
}