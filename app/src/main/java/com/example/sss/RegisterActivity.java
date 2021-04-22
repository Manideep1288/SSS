package com.example.sss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    //--- Initializing Instance Variable ---
    EditText rName,rAge,rPhone,rEmail,rRetypePassword,rPassword,rEmergencyContact;
    Button rRegister;
    TextView rLogin;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //--- Assigning Variable ---
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        rName = findViewById(R.id.register_name);
        rAge = findViewById(R.id.register_age);
        radioGroup = findViewById(R.id.register_gender);
        rPhone = findViewById(R.id.register_phone_number);
        rEmail = findViewById(R.id.register_email);
        rPassword = findViewById(R.id.register_password);
        rRetypePassword = findViewById(R.id.register_retype_password);
        rEmergencyContact = findViewById(R.id.emergency_phone_number);
        rRegister = findViewById(R.id.register);
        rLogin = findViewById(R.id.login_page);

        rRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = rName.getText().toString().trim();
                final String age = rAge.getText().toString().trim();
                final String phone = rPhone.getText().toString().trim();
                final String email = rEmail.getText().toString().trim();
                final String password = rPassword.getText().toString().trim();
                final String retype_password = rRetypePassword.getText().toString().trim();
                final String emergency_contact = rEmergencyContact.getText().toString().trim();
                final int radioId = radioGroup.getCheckedRadioButtonId();

                if( name.isEmpty() ){
                    rName.setError("Name is Required");
                    Toast.makeText(getApplicationContext(),"Please enter USERNAME",Toast.LENGTH_SHORT).show();
                    return;
                }

                if( age.isEmpty() ){
                    rAge.setError("Age is Required");
                    Toast.makeText(getApplicationContext(),"Please enter USERNAME",Toast.LENGTH_SHORT).show();
                    return;
                }

                if( radioId == -1 ){
                    Toast.makeText(getApplicationContext(),"Please select your GENDER",Toast.LENGTH_SHORT).show();
                }

                if( phone.isEmpty() ){
                    rPhone.setError("Phone is Required");
                    Toast.makeText(getApplicationContext(),"Please enter USERNAME",Toast.LENGTH_SHORT).show();
                    return;
                }

                if( email.isEmpty() ){
                    rEmail.setError("Email is Required");
                    Toast.makeText(getApplicationContext(),"Please enter USERNAME",Toast.LENGTH_SHORT).show();
                    return;
                }

                if( password.isEmpty() ){
                    rPassword.setError("Password is Required");
                    Toast.makeText(getApplicationContext(),"Please enter PASSWORD",Toast.LENGTH_SHORT).show();
                    return;
                }

                if( retype_password.isEmpty() ){
                    rRetypePassword.setError("Retype Password");
                    Toast.makeText(getApplicationContext(),"Please Retype PASSWORD",Toast.LENGTH_SHORT).show();
                    return;
                }

                if( emergency_contact.isEmpty() ){
                    rEmergencyContact.setError("Emergency Contact is Required");
                    Toast.makeText(getApplicationContext(),"Please enter USERNAME",Toast.LENGTH_SHORT).show();
                    return;
                }

                if( password.length() < 8 ){
                    rPassword.setError(">=8 Characters");
                    Toast.makeText(getApplicationContext(),"PASSWORD length must be minimum '8' characters",Toast.LENGTH_SHORT).show();
                    return;
                }

                else{

                    if( password.equals(retype_password) ){

                        radioButton = findViewById(radioId);
                        final String gender = (String) radioButton.getText();

                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if( task.isSuccessful() ) {

                                            UserID = mAuth.getCurrentUser().getUid();
                                            final DocumentReference documentReference = mFirestore.collection("Users").document(UserID);

                                            final Map<String,Object> user = new HashMap<>();

                                            user.put("Name",name);
                                            user.put("Age",age);
                                            user.put("Gender",gender);
                                            user.put("Phone",phone);
                                            user.put("Email",email);
                                            user.put("Emergency_Contact",emergency_contact);

                                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                    finish();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Toast.makeText(getApplicationContext(),"Registration Failed! "+documentReference.set(user).getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                    return;

                                                }
                                            });

                                        }

                                        else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getApplicationContext(), "Authentication failed."+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                    }

                    else{
                        rRetypePassword.setError("Password do not Match");
                        Toast.makeText(getApplicationContext(),"PASSWORD do not match",Toast.LENGTH_SHORT).show();
                    }

                }

            }

        });

        rLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();

            }
        });

    }

}
