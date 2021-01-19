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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText mUser,mPass,mPhnum,mUsername;
    private FirebaseAuth auth;
    String username;
    String phnum;
    Button mSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#384950")));


        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        mUser= findViewById(R.id.mail1);
        mPass= findViewById(R.id.passwrd1);
        mSignup= findViewById(R.id.login1);
        mPhnum=findViewById(R.id.phno);
        mUsername=findViewById(R.id.name1);

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(SignupActivity.this,"did:"+did,Toast.LENGTH_LONG).show();

                final String email = mUser.getText().toString().trim();
                String password = mPass.getText().toString().trim();
                username = mUsername.getText().toString().trim();
                phnum = mPhnum.getText().toString().trim();



                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }







                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                mSignup.setEnabled(true);

                                if (!task.isSuccessful())
                                {
                                    Toast.makeText(SignUpActivity.this, "Sign Up failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {


                                    //    Toast.makeText(SignupActivity.this,"lid:"+did,Toast.LENGTH_LONG).show();
                                    Intent intent1 = new Intent(SignUpActivity.this, MainActivity.class);
                                    intent1.putExtra("username",username);
                                    intent1.putExtra("phnumU",phnum);
                                    startActivity(intent1);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
