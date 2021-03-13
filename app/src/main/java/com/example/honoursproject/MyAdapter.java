package com.example.honoursproject;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    ArrayList<Model> List;
    HomeActivity context;

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

        System.out.println("Image URL " + model.getImage());
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurants = itemView.findViewById(R.id.restaurant_text);
            location = itemView.findViewById(R.id.location_text);
            deliveryPrice = itemView.findViewById(R.id.deliveryprice_text);
            eta = itemView.findViewById(R.id.eta_text);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            rsImage = itemView.findViewById(R.id.rsImage);
        }
    }
}
