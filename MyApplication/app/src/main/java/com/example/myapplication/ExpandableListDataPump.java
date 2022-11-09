package com.example.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//https://www.digitalocean.com/community/tutorials/android-expandablelistview-example-tutorial
public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> filters = new ArrayList<String>();
        filters.add("Bright");
        filters.add("Quiet");
        filters.add("Vibey");

        expandableListDetail.put("Filters", filters);
        return expandableListDetail;
    }
}