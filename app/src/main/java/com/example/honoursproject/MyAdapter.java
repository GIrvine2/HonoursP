package com.example.honoursproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    ArrayList<Model> List;
    HomeActivity context;
    List<String> basket = new ArrayList<String>();
    List<LatLng> locations = new ArrayList<LatLng>();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference order = db.getReference().child("Basket");

    public MyAdapter(HomeActivity context, ArrayList<Model> List){
        this.List = List;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.restaurants, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Model model = List.get(position);
        holder.restaurants.setText(model.getRestaurant());
        holder.deliveryPrice.setText(model.getAbout());
        holder.location.setText(model.getLocation());
        holder.eta.setText(model.getEta());
        holder.ratingBar.setRating(model.getRating());
        Picasso.get().load(model.getImage()).into(holder.rsImage);

        String restaurant = holder.restaurants.getText().toString();

        double lat = model.getLat();
        double lng = model.getLng();

        LatLng location = new LatLng(lat, lng);
        holder.addBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(basket.contains(restaurant)) {
                    Toast.makeText(context.getApplicationContext(),"Restaurant Already in Basket!",Toast.LENGTH_SHORT).show();
                } else {

                    locations.add(location);
                    System.out.println(locations);

                    basket.add(restaurant);

                    order.child(userID).child("Restaurant").setValue(basket);
                    order.child(userID).child("Locations").setValue(locations);
                    System.out.println(basket);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        System.out.println(List.size());
        return List.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView restaurants, location, deliveryPrice, eta;
        RatingBar ratingBar;
        ImageView rsImage;
        Button addBasket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurants = itemView.findViewById(R.id.restaurant_text);
            location = itemView.findViewById(R.id.location_text);
            deliveryPrice = itemView.findViewById(R.id.deliveryprice_text);
            eta = itemView.findViewById(R.id.eta_text);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            rsImage = itemView.findViewById(R.id.rsImage);
            addBasket = itemView.findViewById(R.id.addBasket);
        }
    }
}
