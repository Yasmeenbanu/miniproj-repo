package com.example.wasted;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateActivity extends AppCompatActivity {

    DatabaseReference mDataref,dbRef;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser currentusr;

    String mname,mphum;
    EditText nEdN,mEdP;
    Button mUpdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        nEdN=findViewById(R.id.edt1);
        mEdP=findViewById(R.id.edt2);
        mUpdt=findViewById(R.id.updt);

        auth= FirebaseAuth.getInstance();
        currentusr=FirebaseAuth.getInstance().getCurrentUser();
        mDataref=FirebaseDatabase.getInstance().getReference("driverdata").child(cleanEmail(currentusr.getEmail()));

        mDataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mname=dataSnapshot.child("name").getValue(String.class);
                mphum=dataSnapshot.child("phnum").getValue(String.class);
                Log.v("uname","val:"+mname);
                nEdN.setText(mname);
                mEdP.setText(mphum);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUpdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Fn=nEdN.getText().toString();
                String Fp= mEdP.getText().toString();
                DatabaseReference.CompletionListener completionListener =
                        new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(UpdateActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                Toast.makeText(UpdateActivity.this,"Data updated succesfully",Toast.LENGTH_LONG).show();
                //Log.d("Tag","PhoneId" +devId);
                dbRef = FirebaseDatabase.getInstance().getReference("driverdata").child(cleanEmail(currentusr.getEmail()));

                if(Fn!=null)
                {
                    dbRef.child("name").setValue(Fn, completionListener);

                }
                if(Fp!=null)
                {
                    dbRef.child("phnum").setValue(Fp, completionListener);

                }
            }
        });

    }

    private String cleanEmail(String email) {
        return email.replaceAll("\\.", ",");

    }

}
