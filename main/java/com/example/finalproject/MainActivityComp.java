package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.AccessController;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivityComp extends menuActivity {
    ListView lvComptypes;
    public static compType selected_comp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        lvComptypes = findViewById(R.id.lvComptypes);
        


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        //comptype array
        ArrayList<compType> compTypeArr = new ArrayList<>();

        compType bro = new compType("bro","happy birthday dude!",R.drawable.friend);
        compType aquaintence = new compType("aquaintence","happy birthday",R.drawable.aqu);
        compType family = new compType("family","wish you a very happy birthday!",R.drawable.family);
        compType boss = new compType("boss","fuck off!?",R.drawable.bossreal);


        compTypeArr.add(bro);
        compTypeArr.add(aquaintence);
        compTypeArr.add(family);
        compTypeArr.add(boss);

        //comptype adapter
        compTypeAdapter compTypeAdapter;
        compTypeAdapter = new compTypeAdapter(this, 0 , 0  , compTypeArr);
        lvComptypes = findViewById(R.id.lvComptypes);

        lvComptypes.setAdapter(compTypeAdapter);





    }

}