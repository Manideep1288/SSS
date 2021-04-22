package com.example.sss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText lEmail,lPassword;
    Button lLogin;
    TextView lRegister,resetPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        lEmail = findViewById(R.id.login_email);
        lPassword = findViewById(R.id.login_password);
        lLogin = findViewById(R.id.login);
        lRegister = findViewById(R.id.register_page);
        resetPassword = findViewById(R.id.forget_password);

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        lLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login_email = lEmail.getText().toString().trim();
                String login_password = lPassword.getText().toString().trim();

                if(login_email.isEmpty()){

                    lEmail.setError("Email Required");
                    Toast.makeText(getApplicationContext(),"Please enter EMAIL",Toast.LENGTH_SHORT).show();
                    return;

                }

                if (login_password.isEmpty()){

                    lPassword.setError("Password is Required");
                    Toast.makeText(getApplicationContext(),"Please enter Password",Toast.LENGTH_SHORT).show();
                    return;

                }

                else {

                    mAuth.signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();

                                    }

                                    else {
                                        Toast.makeText(getApplicationContext(), "Authentication failed."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }

                            });

                }

            }
        });

        lRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }

        });

        resetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final EditText etEmail = new EditText(v.getContext());
                AlertDialog.Builder reset_password = new AlertDialog.Builder(v.getContext());
                reset_password.setTitle("Reset Password");
                reset_password.setMessage("Verify Email");
                reset_password.setView(etEmail);

                reset_password.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String email = etEmail.getText().toString().trim();

                        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getApplicationContext(),"Reset Link sent to your Email",Toast.LENGTH_SHORT).show();

                            }

                        }).addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(getApplicationContext(),"Error! "+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }

                        });

                    }

                });

                reset_password.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       //close the dialog
                    }

                });

                reset_password.create().show();

            }

        });

    }

}
