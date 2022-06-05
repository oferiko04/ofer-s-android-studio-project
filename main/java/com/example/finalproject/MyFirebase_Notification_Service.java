package com.example.finalproject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyFirebase_Notification_Service extends IntentService {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Query q;
    NotificationCompat.Builder builder;
    int arrSize=0;

    public MyFirebase_Notification_Service() {
        super("ggg");
    }




    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        q = myRef.child("App Users").child(SigninOrRegister_Activity.friendUsingThisPhone.getUsername()).child("msgArr").orderByKey();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrSize = (int) snapshot.getChildrenCount();
                int count =0 ;
                Message m;
                if (arrSize > 0) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        m = dataSnapshot.getValue(Message.class);
                        if (count == arrSize - 1) {
                            builder = new NotificationCompat.Builder(getBaseContext())
                                    .setSmallIcon(R.drawable.papa)
                                    .setContentTitle(m.getFromSender() + " sent you a birthday wish:")
                                    .setContentText(m.getComp())
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setPriority(NotificationCompat.PRIORITY_MAX);

                            NotificationManager manager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            manager.notify(0, builder.build());
                            deleteMsg();
                        } else count++;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    public void onStop(){
    }
    private void deleteMsg(){

        q = myRef.child("App Users").child(SigninOrRegister_Activity.friendUsingThisPhone.getUsername()).orderByKey();
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friend f = snapshot.getValue(friend.class);



                if (f.getMsgArr() == null) {
                    f.setMsgArr(new ArrayList<>());
                }
                int index = f.getMsgArr().size()-1;
                if (index >=0 && index < f.getMsgArr().size())
                    f.removeMsg(index);
                myRef.child("App Users").child(SigninOrRegister_Activity.friendUsingThisPhone.getUsername()).setValue(f);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
