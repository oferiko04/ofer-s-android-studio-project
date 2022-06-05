package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class menuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayShowHomeEnabled(true);
        


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(menuActivity.this);
            builder.setTitle("About The App");
            builder.setMessage("Bday app is designed to bring you and your friends closer togehther by helping you remember their birthdays and sending thoughtful birthday wishes");
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            builder.show();
        }

        if (item.getItemId() == R.id.item_developer) {

            AlertDialog.Builder builder = new AlertDialog.Builder(menuActivity.this);
            builder.setTitle("About The Developer");
            builder.setMessage(" hi i am ofer astrachan a soon to be software engineer and this is my app");
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            builder.show();
        }
        return super.onOptionsItemSelected(item) ;
    }
}





