package com.example.finalproject;

import static android.Manifest.permission.READ_CONTACTS;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

public class MainActivityFriends extends menuActivity {
    ListView lv_friends;
    public static final int REQUEST_READ_CONTACTS = 79;
    ArrayList<String> mobileArray;
    ArrayList<String> numberArray;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    ReceiverBattery receiverBattery = new ReceiverBattery();
    public static friend selected_friend ;
    TextView noContacts ;
    TextView tv_headline1 ;
    TextView tv_headline2 ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private static final int RESULT_PICK_CONTACT = 1;

    char c;
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiverBattery, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiverBattery);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_activity);
        lv_friends = findViewById(R.id.lv_friends);
        noContacts = (TextView)findViewById(R.id.noContacts);
        tv_headline1 = (TextView)findViewById(R.id.tv_headline1);
        tv_headline2 = (TextView)findViewById(R.id.tv_headline2);
        PackageManager pm=getPackageManager();

        numberArray = new ArrayList();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            mobileArray = getAllContacts();
        }
        else {
            requestPermission();
        }




        //friend array
        ArrayList<friend> friendsArr = new ArrayList<>();
        Query q = myRef.child("App Users").orderByValue();
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dst : snapshot.getChildren()) {
                    friend f = dst.getValue(friend.class);
                    for(int i = 0 ; i < numberArray.size() ; i ++){
                        if (! ((f.getUsername().equals(SigninOrRegister_Activity.friendUsingThisPhone.getUsername()))&&(f.getPassword().equals(SigninOrRegister_Activity.friendUsingThisPhone.getPassword())))){
                            if (f.getPhoneNumber().equals(numberArray.get(i))){
                                friendsArr.add(f);
                            }
                        }
                    }
                }
                if(friendsArr.size() == 0){
                    noContacts.setVisibility(View.VISIBLE);
                    tv_headline1.setVisibility(View.INVISIBLE);
                    tv_headline2.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //friends adapter
        friendAdpter friend_Adapter = new friendAdpter(this, 0 , 0  , friendsArr);
        lv_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                friend chosenFriend = friendsArr.get(i);
                selected_friend = chosenFriend ;
                Intent startComplementActivity = new Intent(getBaseContext(), MainActivityComp.class);
                startActivity(startComplementActivity);
            }
        });
        lv_friends.setAdapter(friend_Adapter);
    }


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mobileArray = getAllContacts();
                } else {
                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
    private ArrayList getAllContacts() {
        ArrayList<String> nameList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                nameList.add(name);

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String correctNo="";
                        for (int i = 0 ; i < phoneNo.length() ; i ++){
                            c = phoneNo.charAt(i);
                            if (Character.isDigit(c)){
                                correctNo += c ;
                            }

                        }

                        numberArray.add(correctNo);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return nameList;
    }
}



