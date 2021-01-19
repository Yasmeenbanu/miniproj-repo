package com.example.wasteu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {
    TextView mForgot,mSignup;
    Button mLogin;
    EditText mUser,mPass;
String name,phnum;
    String email;
    String password;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));


        mForgot=findViewById(R.id.forgot);
        mSignup=findViewById(R.id.registe);
        mLogin=findViewById(R.id.logi);
        mUser=findViewById(R.id.username);
        mPass=findViewById(R.id.pass);

        Intent intent=getIntent();
     name=   intent.getStringExtra("username");
        phnum=intent.getStringExtra("phnumU");

        auth = FirebaseAuth.getInstance();


        mForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForgotActivity.class);
                startActivity(intent);
            }
        });
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               email = mUser.getText().toString();
                password = mPass.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

//                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Incorrect Email ID/Password", Toast.LENGTH_SHORT).show();
                                } else {
                                    DatabaseReference.CompletionListener completionListener = new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if (databaseError != null) {
                                                Toast.makeText(MainActivity.this, "Error Signing In", Toast.LENGTH_SHORT).show();

                                            }


                                        }
                                    };
//
                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                                    intent.putExtra("sak",mDeviceId);
                                    intent.putExtra("customernam",name);
                                    intent.putExtra("customernum",phnum);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "Sucess", Toast.LENGTH_SHORT).show();
//

                                }

                            }

                        });

            }
        });

    }
}
