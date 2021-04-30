package com.example.honoursproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * HomeActivity is the first Activity users will be brought to upon signing in.
 * Displays all of the restaurants information from the firebase realtime database.
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer; //Side Navigation Drawer

    private FirebaseAuth mAuth; //Firebase Authentication
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private RecyclerView recyclerView; //RecyclerView to display restaurants information into

    private FirebaseDatabase db = FirebaseDatabase.getInstance(); //Get Database Instance
    private DatabaseReference root = db.getReference().child("Restaurants"); //Along with both restaurants & orders child
    private DatabaseReference order = db.getReference().child("Orders");

    private MyAdapter adapter; //RecyclerView adapter
    private ArrayList<Model> list; //ArrayList to retrieve data from Model.java

    Button addBasket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recyclerView);
        addBasket = findViewById(R.id.addBasket);

        //List used for Firebase Reference
        list = new ArrayList<>();

        //RecyclerView settings
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, list);

        recyclerView.setAdapter(adapter);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Get currently logged in user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Get Users email address
        String userid = user.getEmail();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(userid); //User email address displayed at top of application

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Model model = dataSnapshot.getValue(Model.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Used to allow users to navigate throughout the app
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent home = new Intent(this, HomeActivity.class);
                startActivity(home);
                break;
            case R.id.nav_basket:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BasketFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;
            case R.id.nav_favorite:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavouriteFragment()).commit();
                break;
            case R.id.nav_signout:
                FirebaseAuth.getInstance().signOut();
                Intent signOut = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(signOut);
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}