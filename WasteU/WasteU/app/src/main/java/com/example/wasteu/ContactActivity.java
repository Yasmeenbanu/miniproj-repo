package com.example.wasteu;

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

public class ContactActivity extends AppCompatActivity {
TextView mphnum;
Button mCall;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 34;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));

        mphnum=findViewById(R.id.hlpnum);
        mCall=findViewById(R.id.callb);

        final Intent callIntent = new Intent(Intent.ACTION_CALL);

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ContactActivity.this,android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ContactActivity.this,
                            android.Manifest.permission.CALL_PHONE))
                        Toast.makeText(ContactActivity.this,"hiii",Toast.LENGTH_SHORT).show();


                }
                ActivityCompat.requestPermissions(ContactActivity.this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
//                Toast.makeText(DetailsActivity.this,"in"+localPhone,Toast.LENGTH_SHORT).show();
                callIntent.setData(Uri.parse("tel:"+mCall));
//                Toast.makeText(DetailsActivity.this,"in"+callIntent,Toast.LENGTH_SHORT).show();
                startActivity(callIntent);

            }
        });

    }
}
