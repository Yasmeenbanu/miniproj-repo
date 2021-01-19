package com.example.wasteu;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DummyA extends AppCompatActivity {
ImageView mGt;
Button mGet;

    private static FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        mGet=findViewById(R.id.grt);
        mGt=findViewById(R.id.img);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbRef=database.getReference("complaints").child(cleanEmail(currentUser.getEmail()));

        mGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
dbRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists())
        {
            String mg=dataSnapshot.child("ph").getValue(String.class);
            Uri g= Uri.parse(mg);
            mGt.setImageURI(g);
            mGt.requestFocus();
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});



            }
        });
    }

    private String cleanEmail(String originalEmail) {
        return originalEmail.replaceAll("\\.", ",");
    }
}
