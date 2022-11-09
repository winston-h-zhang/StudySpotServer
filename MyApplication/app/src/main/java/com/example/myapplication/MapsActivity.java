package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.net.HttpURLConnection;

//https://www.digitalocean.com/community/tutorials/android-expandablelistview-example-tutorial
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Button bottomsheet;

    //AutoComplete
    private HashMap<String, StudySpot> names_to_spots;
    private AutoCompleteTextView autocomplete_searchField;
    private ArrayList<String> places;
    private ArrayList<StudySpot> spots;


    private ArrayList<StudySpot> generateStudySpots() {
        ArrayList<StudySpot> cur_spots = new ArrayList<StudySpot>();
        StudySpot leavey = new StudySpot(34.02193, -118.28277, "Leavey Library", "Snippet");
        StudySpot doheny = new StudySpot(34.02015, -118.28372, "Doheny Library", "Snippet");
        StudySpot sidney = new StudySpot(34.02235, -118.28512, "Sydney Harman", "Snippet");
        cur_spots.add(leavey);
        cur_spots.add(doheny);
        cur_spots.add(sidney);
        return cur_spots;
    }

    private void place_markers() {
        // synchronized?
        HashMap<String, StudySpot> temp = new HashMap<String, StudySpot>();
        for (int i = 0; i < spots.size(); ++i) {
            StudySpot cur = spots.get(i);
            Log.d("myTag", cur.getTitle());
            temp.put(cur.getTitle(), cur);
            mMap.addMarker(new MarkerOptions().position(cur.getPosition()).title(cur.getTitle()).snippet(cur.getSnippet()));
        }
        names_to_spots = temp;
    }

    private ArrayList<String> get_dropDown_titles() {
        ArrayList<String> places = new ArrayList<String>();
        for (int i = 0; i < spots.size(); ++i) {
            places.add(spots.get(i).getTitle());
        }
        return places;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.spots = generateStudySpots();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        place_markers();
        // Synchronize on study spots arraylist?
        

        //Filters


        // Colors and Appearance!!!
        // https://stackoverflow.com/questions/30763879/clicked-drop-down-item-in-autocompletetextview-does-not-respond-on-the-first-cli
        autocomplete_searchField = (AutoCompleteTextView) findViewById(R.id.map_toolbar);
//        displayField = (TextView) findViewById(R.id.dropdown_item);

        // Gets the string array
        places = get_dropDown_titles();

        // Creates the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, places);
        autocomplete_searchField.setAdapter(adapter);
        autocomplete_searchField.setThreshold(1);
        autocomplete_searchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autocomplete_searchField.showDropDown();
            }
        });
        autocomplete_searchField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                String selected_Place = autocomplete_searchField.getText().toString();
                Log.d("myTag", "Clicked Dropdown Item " + selected_Place);
                StudySpot cur_spot = names_to_spots.get(selected_Place);
//                StudySpot cur_spot = new StudySpot(34,34, "Doheny", "K");
                Log.d("myTag", "Place " + cur_spot.getTitle());

                // on below line we are adding marker to that position.
                mMap.addMarker(new MarkerOptions().position(cur_spot.getPosition()).title(selected_Place));
                Log.d("myTag", "Marker Placed");

                // below line is to animate camera to that position.
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cur_spot.getPosition(), 18));
                Log.d("myTag", "Camera Moved");
            }
        });
        mapFragment.getMapAsync(this);




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        place_markers();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.0224, -118.2851), 15));
    }

}