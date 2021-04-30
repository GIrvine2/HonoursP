package com.example.honoursproject;

import android.media.Image;

import com.google.android.gms.maps.model.LatLng;

/**
 * Model.java used to retrieve data from firebase Database
 */
public class Model {

    String Restaurant, Location, About, eta, image;
    int rating;
    double lat, lng;

    public String getRestaurant() {
        return Restaurant;
    }

    public String getLocation() {
        return Location;
    }

    public String getAbout() {
        return About;
    }

    public String getEta() {
        return eta;
    }

    public int getRating() {
        return rating;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getImage() {
        return image;
    }

}
