package com.example.sss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

import static android.Manifest.permission.SEND_SMS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    TextView tvName, tvAge, tvGender, tvEmail, tvPhone, tvEmergencyContact;
    Button btActivate,btLogout;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    String UserID, phone, number, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //--- Navigation Drawer ---

        drawerLayout = findViewById(R.id.main_drawer);
        navigationView = findViewById(R.id.main_nav);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.menu_home);

        btActivate = findViewById(R.id.Activate);
        tvName = findViewById(R.id.Name);
        tvAge = findViewById(R.id.Age);
        tvGender = findViewById(R.id.Gender);
        tvEmail = findViewById(R.id.Email);
        tvPhone = findViewById(R.id.Phone);
        tvEmergencyContact = findViewById(R.id.Emergency_Contact);
        btLogout = findViewById(R.id.Logout);

        UserID = mAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = mFirestore.collection("Users").document(UserID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                phone = "+91"+documentSnapshot.getString("Emergency_Contact");
                tvName.setText(documentSnapshot.getString("Name"));
                tvAge.setText(documentSnapshot.getString("Age"));
                tvGender.setText(documentSnapshot.getString("Gender"));
                tvEmail.setText(documentSnapshot.getString("Email"));
                tvPhone.setText(documentSnapshot.getString("Phone"));
                tvEmergencyContact.setText(documentSnapshot.getString("Emergency_Contact"));

            }
        });

        btActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Action.class));

            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.menu_home:
                break;

            case R.id.menu_profile:
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                break;

            case R.id.menu_logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);

        }

        else {

            super.onBackPressed();

        }

        super.onBackPressed();
    }

}