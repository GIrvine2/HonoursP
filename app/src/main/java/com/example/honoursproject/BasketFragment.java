package com.example.honoursproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * BasketFragment used to display the items that users have added to their basket
 * Items are stored onto the firebase database
 * And displayed into a ListView within the BasketFragment
 */
public class BasketFragment extends Fragment {

    //Setting Variables
    View view;
    ListView listView;
    Button confirmBtn, clearBtn;

    //Get Current Firebase User & ID
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();

    //Create ArrayList that will be used to store the data from Firebase
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    //Get Firebase Reference
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    //Get Reference to specific UserID basket and their chosen restaurants
    private DatabaseReference order = db.getReference().child("Basket").child(userID).child("Restaurant");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Setting variables for all buttons/views
        view = inflater.inflate(R.layout.fragment_basket, container, false);
        confirmBtn = (Button) view.findViewById(R.id.confirmBtn);
        clearBtn = (Button) view.findViewById(R.id.clearBtn);
        listView = (ListView) view.findViewById(R.id.listView);

        order.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (getActivity() != null) {
                    //Get the Value from the Firebase Realtime database
                    String value = snapshot.getValue(String.class);
                    //Add it to the arrayList and display into listView
                    arrayList.add(value);
                    arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
                    listView.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Confirm Button for when users want to confirm their order and proceed to the MapsActivity
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the users basket is empty and do not proceed to MapsActivity if its true
                if (arrayList.isEmpty()) {
                    Toast.makeText(getActivity(),"Your basket is empty!",Toast.LENGTH_SHORT).show();
                } else {
                    //Else simply proceed to the MapsActivity
                    Intent intent2 = new Intent(getActivity(), MapsActivity.class);
                    startActivity(intent2);
                }
            }
        });


        //Clear Button to allow users to clear their basket and remove data from the Firebase Database
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check again to see if the users basket is already empty and provide a visual warning if so.
                if (arrayList.isEmpty()) {
                    Toast.makeText(getActivity(),"Your basket is  already empty!",Toast.LENGTH_SHORT).show();
                } else {
                    //else remove value from the firebase database
                    order.removeValue();
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }


            }
        });


        return view;
    }

}
