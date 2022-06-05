package com.example.finalproject;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class friendDetails extends menuActivity implements View.OnClickListener {
    EditText et_birthdayDay, et_birthdayMonth, et_birthdayYear;
    int int_birthDay, int_birthMonth, int_birthYear;
    Button btn_Done;
    Button btn_picture;
    String new_id = "";
    String pic_url = "";
    EditText et_phoneNumber;


    private static final int CAMERA_REQUEST = 1888;
    private ImageView iv_camera_pic;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    OutputStream outputStream;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    StorageReference storageReference;
    static Uri uriFromFile;
    static String url;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);
        storageReference = FirebaseStorage.getInstance().getReference();
        iv_camera_pic = (ImageView) findViewById(R.id.iv_camera_pic);
        et_birthdayDay = (EditText) findViewById(R.id.et_birthdayDay);
        et_birthdayMonth = (EditText) findViewById(R.id.et_birthdayMonth);
        et_birthdayYear = (EditText) findViewById(R.id.et_birthdayYear);
        btn_picture = (Button) findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_Done = (Button) findViewById(R.id.btn_Done);
        btn_Done.setOnClickListener(this);
        et_phoneNumber = (EditText) findViewById(R.id.et_phoneNmber);


    }


    public void onClick(View view) {
        if (view == btn_picture) {
            selectImage(this);
        }
        if (view == btn_Done) {
            SigninOrRegister_Activity.friendUsingThisPhone.setPhoneNumber(et_phoneNumber.getText().toString());
            Date birthdayDate = new Date();
            if (et_birthdayDay.getText().toString().trim().length() == 0 ||
                    et_birthdayMonth.getText().toString().trim().length() == 0 ||
                    et_birthdayYear.getText().toString().trim().length() == 0 ||
                    et_phoneNumber.getText().toString().trim().length() == 0
            )
                Toast.makeText(this, "you must enter all your details", Toast.LENGTH_SHORT).show();
            else {
                int_birthDay = Integer.valueOf(et_birthdayDay.getText().toString());
                int_birthMonth = Integer.valueOf(et_birthdayMonth.getText().toString());
                int_birthYear = Integer.valueOf(et_birthdayYear.getText().toString());
                Date dateOfUser = new Date(int_birthYear, int_birthMonth, int_birthDay);
                SigninOrRegister_Activity.friendUsingThisPhone.setBirthday(dateOfUser);
                Toast.makeText(this, "birthday:"+SigninOrRegister_Activity.friendUsingThisPhone.getAge(), Toast.LENGTH_SHORT).show();

                int calculatingYourAge = getPerfectAgeInYears(int_birthYear, int_birthMonth, int_birthDay);
                SigninOrRegister_Activity.friendUsingThisPhone.setDaysUntil(get_count_of_days(int_birthDay, int_birthMonth, int_birthYear));
                SigninOrRegister_Activity.friendUsingThisPhone.setAge(calculatingYourAge);


                myRef.child("App Users").child(SigninOrRegister_Activity.friendUsingThisPhone.getUsername()).setValue(SigninOrRegister_Activity.friendUsingThisPhone);
                //get_count_of_days(int_birthDay,int_birthMonth,int_birthYear);

                Intent i1 = new Intent(friendDetails.this, MainActivityFriends.class);
                startActivity(i1);

            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }





    public static int getPerfectAgeInYears(int yourBirthday_year, int yourBirthday_month, int yourBirthday_day) {
        int calculatedAge=0;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int today_year = calendar.get(Calendar.YEAR);
        int today_month = calendar.get(Calendar.MONTH) + 1 ;
        int today_day = calendar.get(Calendar.DAY_OF_MONTH) ;
        if (today_month < yourBirthday_month)   calculatedAge = today_year-yourBirthday_year - 1;

        else if(today_day < yourBirthday_day)   calculatedAge = (today_year-yourBirthday_year - 1);

        else    calculatedAge = (today_year-yourBirthday_year);

        return calculatedAge;
    }



    public String get_count_of_days(int yourBirthday_day,int yourBirthday_month, int yourBirthday_year) {
        String answer;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int today_year = calendar.get(Calendar.YEAR);
        int today_month = calendar.get(Calendar.MONTH) + 1 ;
        int today_day = calendar.get(Calendar.DAY_OF_MONTH) ;

        if (today_month == yourBirthday_month || today_month + 1 == yourBirthday_month || (yourBirthday_month==1 && today_month==12)){
            int today_month_has;
            if(today_month == 4 || today_month == 6 || today_month == 9 || today_month == 11) today_month_has = 30;
            else if (today_month == 2) today_month_has = 28;
            else today_month_has = 31;
            if (today_month == yourBirthday_month){
                if(today_day > yourBirthday_day){
                    return " more than one month";
                }
                if (today_day == yourBirthday_day){
                    return " today!" ;
                }
                else{
                    answer = " "+(yourBirthday_day-today_day);
                    return answer;
                }
            }
            else{
                answer = ""+ (yourBirthday_day + (today_month_has-today_day));
                return answer;
            }

        }
        else{
            return " more than one month";
        }

    }
    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                     startActivityForResult(takePicture, 123);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {

            if (requestCode == 123 && resultCode == RESULT_OK ) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                iv_camera_pic.setImageBitmap(imageBitmap);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alert");
                builder.setMessage("Are you happy with this picture?");
                AlertDialog dialog = builder.create();
                dialog.setButton(dialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveImage(friendDetails.this,imageBitmap,"face picture",".png");

                            }
                        });
                dialog.setButton(dialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                dialog.show();


            }
            else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        iv_camera_pic.setImageBitmap(selectedImage);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data2 = baos.toByteArray();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        raiseToast("Something went wrong");
                    }

                } else {
                    raiseToast("You haven't picked Image");
                }
            }
        }
    }


    public void saveImage(Context context, Bitmap bitmap, String name, String extension)
    {
        name = name + "." + extension;
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            uriFromFile = Uri.fromFile(getFileStreamPath(name));
            uploadImage(uriFromFile);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void uploadImage(Uri uri)
    {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading your face picture, please wait");
        pd.show();

        if (uri != null)
        {
            myRef.child("App Users").child(SigninOrRegister_Activity.friendUsingThisPhone.getUsername()).setValue(SigninOrRegister_Activity.friendUsingThisPhone);



            StorageReference filesRef = FirebaseStorage.getInstance().getReference().child("face of  "+SigninOrRegister_Activity.friendUsingThisPhone.getUsername()).child(SigninOrRegister_Activity.friendUsingThisPhone.getUsername()+"."+getFileExtension(uri));
            filesRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                    filesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            url = uri.toString();
                            SigninOrRegister_Activity.friendUsingThisPhone.setFace(url);
                            myRef.child("App Users").child(SigninOrRegister_Activity.friendUsingThisPhone.getUsername()).setValue(SigninOrRegister_Activity.friendUsingThisPhone);
                            pd.dismiss();
                        }
                    });
                }
            });
        }
    }


    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    void raiseToast(String msg)
    {
        Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
        toast.setMargin(0,-200);
        toast.show();
    }

}
