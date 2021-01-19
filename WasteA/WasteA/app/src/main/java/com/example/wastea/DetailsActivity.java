package com.example.wastea;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    ListView mlist;
    ArrayList<ModelF> modelFArrayList =new ArrayList<>();
String mkey=new String();
String mName=new String();
String mPhnum=new String();
    private DatabaseReference mdataR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mlist=findViewById(R.id.listvv);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));

        mdataR = FirebaseDatabase.getInstance().getReference("driverdata");

        mdataR.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                  Log.v("datasnap","dats"+dataSnapshot1.getKey());

                  mkey = dataSnapshot1.getKey();
                  mName = dataSnapshot1.child("name").getValue(String.class);
                  mPhnum = dataSnapshot1.child("phnum").getValue(String.class);
                  ModelF modelF = new ModelF();
                  modelF.setKey(mkey);
                  modelF.setName(mName);
                  modelF.setPhnum(mPhnum);


                  modelFArrayList.add(modelF);

                  DetailsAdapter detailsAdapter = new DetailsAdapter(DetailsActivity.this, modelFArrayList);
                  mlist.setAdapter(detailsAdapter);
              }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }

      });


    }
}
