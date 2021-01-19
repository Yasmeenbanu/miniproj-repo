package com.example.wasted;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PointsActivity extends AppCompatActivity {
EditText mMail;
Button mPoint;
String mail;
int i;
int fetchP;
    private DatabaseReference mdataR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));


        mMail=findViewById(R.id.mailU);
        mPoint=findViewById(R.id.btnp);

        mPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = mMail.getText().toString();
                if (mail != null) {
                    mdataR = FirebaseDatabase.getInstance().getReference("userdata").child(cleanEmail(mail));
                    mdataR.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            fetchP = dataSnapshot.child("point").getValue(Integer.class);
                            writepointstodb(fetchP,mail);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                   // Toast.makeText(PointsActivity.this, "points: " + (i++), Toast.LENGTH_SHORT).show();

                }

                else {
                    Toast.makeText(PointsActivity.this, "Enter Mail-id " , Toast.LENGTH_SHORT).show();

                }
            }

        });

    }

    private void writepointstodb(int fetchP, String mail) {

        DatabaseReference.CompletionListener completionListener=new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError!=null)
                    {
                        Toast.makeText(PointsActivity.this, "Error:" +databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
            }
        };
        mdataR = FirebaseDatabase.getInstance().getReference("userdata").child(cleanEmail(mail));
//                if (mail!=null)
//                    {
                        int obt=fetchP++;
                        mdataR.child("point").setValue(fetchP++,completionListener);

                        Log.v("points","pt :"+fetchP++);
                        Log.v("pointsaddd","pt :"+ fetchP);

//                    }
    }

    private String cleanEmail(String originalEmail) {
        return originalEmail.replaceAll("\\.", ",");
    }

}
