package com.example.bottomsheet;

import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

public class StudySpot {
    private final LatLng position;
    private final String title;
    private final String snippet;

    public StudySpot(double lat, double lng, String title, String snippet) {
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }
}