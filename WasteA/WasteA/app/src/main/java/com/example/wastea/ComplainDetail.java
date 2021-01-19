package com.example.wastea;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ComplainDetail extends AppCompatActivity {
Button mCall,mLocate;
TextView mEdt,mEd1;
String key;
    String unum;
    Double Lat,Lng;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 34;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_detail);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));

        mEdt=findViewById(R.id.cmp);
        mCall=findViewById(R.id.cl);
        mLocate=findViewById(R.id.lcte);
        mEd1=findViewById(R.id.cmpd);
        Intent intent=getIntent();
        key= intent.getStringExtra("key");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(key);
mDatabaseRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
   String name=dataSnapshot.child("name").getValue(String.class);
 Lat=dataSnapshot.child("Lat").getValue(Double.class);
  Lng=dataSnapshot.child("Lng").getValue(Double.class);
  mEd1.setText(name);

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});


        mEdt.setText(key);

        mRef=FirebaseDatabase.getInstance().getReference("userdata").child(key);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               unum=dataSnapshot.child("phnum").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final Intent callIntent = new Intent(Intent.ACTION_CALL);

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ComplainDetail.this,android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ComplainDetail.this,
                            android.Manifest.permission.CALL_PHONE))
                        Toast.makeText(ComplainDetail.this,"hiii",Toast.LENGTH_SHORT).show();


                }
                ActivityCompat.requestPermissions(ComplainDetail.this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
//                Toast.makeText(DetailsActivity.this,"in"+localPhone,Toast.LENGTH_SHORT).show();
                callIntent.setData(Uri.parse("tel:"+unum));
//                Toast.makeText(DetailsActivity.this,"in"+callIntent,Toast.LENGTH_SHORT).show();
                startActivity(callIntent);
            }
        });

        mLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ComplainDetail.this,GetLocationA.class);
                intent1.putExtra("latk",Lat);
                intent1.putExtra("lngk",Lng);
                startActivity(intent1);
            }
        });

    }
}
