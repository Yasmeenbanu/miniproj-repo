package com.example.wastea;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private Query mDatabaseRef;
    private DatabaseReference mdatR;
    String list= new String();
    List<ModelF> modelFList=new ArrayList<>();
EditText mDate1,mDate2;
ListView mlist;
Button mReprt;
int year;
int month;
int dayofmonth;
Calendar calendar;

String mD1,mD2;
    String na=new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        
        ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));

        mReprt=findViewById(R.id.ddd);
        mlist=findViewById(R.id.listv);
        mDate1=findViewById(R.id.date1);
mDate2=findViewById(R.id.date2);
mDate1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
         calendar = Calendar.getInstance();
     year=calendar.get(Calendar.YEAR);
     month=calendar.get(Calendar.MONTH);
     dayofmonth=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog=new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {



                if(month<10) {

                    mDate1.setText(year + "-" +"0"+ (month + 1) + "-" + day);
                }
                else
                {
                    mDate1.setText(year + "-" +(month + 1) + "-" + day);
                }
            }
        },year,month,dayofmonth);
        datePickerDialog.show();
    }
});

        mDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                dayofmonth=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        if(month<10) {

                            mDate2.setText(year + "-" +"0"+ (month + 1) + "-" + day);
                        }
                        else
                        {
                            mDate2.setText(year + "-" +(month + 1) + "-" + day);
                        }

                    }
                },year,month,dayofmonth);
                datePickerDialog.show();
            }
        });

        mReprt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mD1=mDate1.getText().toString();
                mD2=mDate2.getText().toString();

           //     Toast.makeText(ReportActivity.this,"dates :"+mD1+mD2,Toast.LENGTH_SHORT).show();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("DoneC").orderByChild("Date").startAt(mD1).endAt(mD2);
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        modelFList.clear();
                        for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                            list = (foodSnapshot.getKey());

                            na= foodSnapshot.child("name").getValue(String.class);

//mdatR=FirebaseDatabase.getInstance().getReference("DoneC").child(list);
//mdatR.addListenerForSingleValueEvent(new ValueEventListener() {
//    @Override
//    public void onDataChange(DataSnapshot dataSnapshot) {
//        na=dataSnapshot.child("name").getValue(String.class);
//             Toast.makeText(ReportActivity.this,"dates :"+na,Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onCancelled(DatabaseError databaseError) {
//
//    }
//});



                            ModelF modelF=new ModelF();
                            modelF.setKey(list);
                            modelF.setName(na);
                            modelFList.add(modelF);

                            Myadaptr myadaptr=new Myadaptr(ReportActivity.this,modelFList);
                            mlist.setAdapter(myadaptr);

                            Log.v("dateddata","datessss: "+na);
                            Toast.makeText(ReportActivity.this,"dates :"+list,Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
