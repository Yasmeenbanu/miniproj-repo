package com.example.wasteu;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewpActivity extends AppCompatActivity {
TextView mPoint;
int fetchP;
    private DatabaseReference mdataR;
    public static FirebaseUser currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewp);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));

        mPoint=findViewById(R.id.points);

        currentuser= FirebaseAuth.getInstance().getCurrentUser();
        mdataR = FirebaseDatabase.getInstance().getReference("userdata").child(cleanEmail(currentuser.getEmail()));
        mdataR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
if(dataSnapshot.child("point").exists()){
    fetchP = dataSnapshot.child("point").getValue(Integer.class);
    Log.v("Ssss","pts:"+fetchP);
    if (fetchP!='0') {
        SetText(fetchP);
}
}

else
{Log.v("str","fff"+dataSnapshot);
    Toast.makeText(ViewpActivity.this,"No points are added to your account",Toast.LENGTH_LONG).show();
    SetText(0);
}
//                    else {
//
//                        SetText(0);
//                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void SetText(int fetchP) {
        try {
            mPoint.setText(Integer.toString(fetchP));

        }
        catch (Exception e)
        {
            Log.v("excetion","msgE: "+e.getMessage());
        }
    }

    private String cleanEmail(String originalEmail) {
        return originalEmail.replaceAll("\\.", ",");
    }
}
