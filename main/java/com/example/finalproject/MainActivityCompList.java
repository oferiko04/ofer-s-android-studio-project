package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivityCompList extends menuActivity implements View.OnClickListener {
    int counter = 0 ;
    Button btn_add;
    ArrayList<String> arr_comp;
    ListView comp_listView ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    String compType ;
    Map<String,String> comps ;
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complist);
        comp_listView = (ListView)findViewById(R.id.comp_listView);
        btn_add = (Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        arr_comp = new ArrayList<>();
        compType = getIntent().getExtras().get("compType").toString();


        Query q = myRef.child("App Users").child(SigninOrRegister_Activity.friendUsingThisPhone.getUsername()).orderByValue();
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comps = snapshot.getValue(friend.class).getAddedComps() ;
                if (comps == null){
                    comps = new HashMap<String,String>();
                }
                if(compType.compareTo("bro") == 0) {
                    arr_comp.add("happy birthday bro!");
                    arr_comp.add("live to 120 yrs");
                    arr_comp.add("congrats you fat bastard");
                }
                else if (compType.compareTo("boss") == 0){
                    arr_comp.add("happy birthday");
                    arr_comp.add("wish you were dead");
                }
                else if (compType.compareTo("aquaintence") == 0){
                    arr_comp.add("happy birthday, havent seen you in a while");
                }
                else if (compType.compareTo("family") == 0 ){
                    arr_comp.add("happy birthday brother");
                }
                for (Map.Entry<String,String > entry : comps.entrySet()){
                    if (entry.getValue().compareTo(compType) == 0){
                        arr_comp.add(entry.getKey());
                    }
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivityCompList.this,android.R.layout.simple_list_item_1, android.R.id.text1, arr_comp);
                comp_listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Query q = myRef.child("App Users").child(MainActivityFriends.selected_friend.getUsername()).orderByValue();
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (flag) {
                                    friend f = snapshot.getValue(friend.class);

                                    Message m = new Message(SigninOrRegister_Activity.friendUsingThisPhone.getUsername(),arr_comp.get(i));

                                    if (f.getMsgArr() == null){
                                        f.setMsgArr( new ArrayList<>());
                                    }
                                    f.addMsg(m);
                                    myRef.child("App Users").child(MainActivityFriends.selected_friend.getUsername()).setValue(f);
                                    flag = false;
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
                comp_listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void onClick (View view){
        EditText inputEditTextField= new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter your birthday wish!")
                .setView(inputEditTextField)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(inputEditTextField.getText().toString().trim().length()!=0 &&
                                inputEditTextField.getLineCount() == 1){
                            String editTextInput = inputEditTextField.getText().toString();
                            arr_comp.add(editTextInput);
                            SigninOrRegister_Activity.friendUsingThisPhone.AddAddedComps(editTextInput ,  compType);
                            myRef.child("App Users").child(SigninOrRegister_Activity.friendUsingThisPhone.getUsername()).setValue(SigninOrRegister_Activity.friendUsingThisPhone);
                        }
                        else
                            Toast.makeText(MainActivityCompList.this, "type your wish and make sure it is written in one line", Toast.LENGTH_LONG).show();

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivityCompList.this,android.R.layout.simple_list_item_1, android.R.id.text1, arr_comp);
                        comp_listView.setAdapter(arrayAdapter);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}