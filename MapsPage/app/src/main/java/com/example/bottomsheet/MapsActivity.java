package com.example.bottomsheet;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.bottomsheet.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private List<Boolean> filter_status;


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

    public void sendFilterValues(List<Boolean> stuff) {
        for (int i = 0; i < stuff.size(); ++i) {
            filter_status.set(i,stuff.get(i));
        }
        // update map to show markers that belong to filters
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.spots = generateStudySpots();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Synchronize on study spots arraylist?

        //Filters
        Button OpenBottomSheet = findViewById(R.id.open_bottom_sheet);

        //hardcoded over hashmap
        filter_status = Collections.synchronizedList(new ArrayList<Boolean>());
        filter_status.add(false);
        filter_status.add(false);
        filter_status.add(false);
        filter_status.add(false);
        OpenBottomSheet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        BottomSheetDialog bottomSheet = new BottomSheetDialog();
                        bottomSheet.setCancelable(false);
                        // send filter data
                        bottomSheet.addList(filter_status);
                        bottomSheet.addMap(MapsActivity.this);
                        bottomSheet.show(getSupportFragmentManager(),
                                "ModalBottomSheet");
                    }
                });


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
                mMap.addMarker(new MarkerOptions().position(cur_spot.getPosition()).title(selected_Place));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cur_spot.getPosition(), 18));
            }
        });
        mapFragment.getMapAsync(this);

    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        place_markers();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //switch page to studyspot page
                //startActivity(new Intent(MainActivity.this, MyOtherActivity.class));
            }
        });
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.0224, -118.2851), 15));
    }
}