package com.example.sss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;

    TextView tvName, tvAge, tvGender, tvEmail, tvPhone, tvEmergencyContact;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //--- Navigation Drawer ---

        drawerLayout = findViewById(R.id.profile_drawer);
        navigationView = findViewById(R.id.profile_nav);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        tvName = findViewById(R.id.Name);
        tvAge = findViewById(R.id.Age);
        tvGender = findViewById(R.id.Gender);
        tvEmail = findViewById(R.id.Email);
        tvPhone = findViewById(R.id.Phone);
        tvEmergencyContact = findViewById(R.id.Emergency_Contact);

        UserID = mAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = mFirestore.collection("Users").document(UserID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                tvName.setText(documentSnapshot.getString("Name"));
                tvAge.setText(documentSnapshot.getString("Age"));
                tvGender.setText(documentSnapshot.getString("Gender"));
                tvEmail.setText(documentSnapshot.getString("Email"));
                tvPhone.setText(documentSnapshot.getString("Phone"));
                tvEmergencyContact.setText(documentSnapshot.getString("Emergency_Contact"));

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.menu_home:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;

            case R.id.menu_profile:
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
