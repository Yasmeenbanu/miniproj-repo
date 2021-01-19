package com.example.wasteu;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ComplaintActivity extends AppCompatActivity {
Button mSelect,mPrcd,mI;
EditText medt;
    private int GALLERY = 1, CAMERA = 2;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView videoView;
    Uri contentURI,mImageUri;
    private static FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private StorageReference imagepath;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    Double lat,lng;

    Calendar calendar;
    SimpleDateFormat simpledateformat;
    String Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
ActionBar actionBar= getSupportActionBar();
actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));

        mSelect = findViewById(R.id.vid);
        mPrcd=findViewById(R.id.upld);
       // mI=findViewById(R.id.upld1);
        videoView = findViewById(R.id.iv);
        medt=findViewById(R.id.medit);

        Intent intent=getIntent();
        lat=  intent.getDoubleExtra("Lat",0.00);
        lng= intent.getDoubleExtra("Lng",0.00);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date = simpledateformat.format(calendar.getTime());


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbRef=database.getReference("complaints").child(cleanEmail(currentUser.getEmail()));


        mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        mPrcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadFile();


            }
        });

    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

         fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(ComplaintActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload(medt.getText().toString().trim(),
                                    taskSnapshot.getDownloadUrl().toString());
                            String uploadId = mDatabaseRef.push().getKey();

                          //  Toast.makeText(ComplaintActivity.this, Date, Toast.LENGTH_LONG).show();

                            mDatabaseRef.child(cleanEmail(currentUser.getEmail())).setValue(upload);
                            mDatabaseRef.child(cleanEmail(currentUser.getEmail())).child("mail").setValue(cleanEmail(currentUser.getEmail()));
                            if(lat!=null)
                            {
                                mDatabaseRef.child(cleanEmail(currentUser.getEmail())).child("Lat").setValue(lat);

                            }
                            mDatabaseRef.child(cleanEmail(currentUser.getEmail())).child("Date").setValue(Date);
                            if(lng!=null)
                            {

                                mDatabaseRef.child(cleanEmail(currentUser.getEmail())).child("Lng").setValue(lng);

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    private String cleanEmail(String originalEmail) {
        return originalEmail.replaceAll("\\.", ",");
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("result", "" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(data.getData()).into(videoView);
        }



    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}
