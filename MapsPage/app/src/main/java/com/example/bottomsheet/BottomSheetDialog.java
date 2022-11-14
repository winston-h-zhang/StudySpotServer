package com.example.bottomsheet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

//https://www.geeksforgeeks.org/modal-bottom-sheet-in-android-with-examples/
public class BottomSheetDialog extends BottomSheetDialogFragment {

    private List<Boolean> filter_status;
    private MapsActivity shared;

    //https://medium.com/codex/android-bottom-sheet-how-to-use-it-in-practice-c1e8f110d624
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.TutorialBottomSheetDialog);
    }

    // https://interviewsansar.com/share-arraylist-between-classes-in-java-with-code/
    public void addList(List<Boolean> stuff) {
        filter_status = new ArrayList<Boolean>();
        synchronized (filter_status) {
            for (int i = 0; i < stuff.size(); ++i) {
                filter_status.add(stuff.get(i));
            }
        }
    }

    public void addMap(MapsActivity stuff) {
        this.shared = stuff;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet,
                container, false);

        Button filter_1_button = v.findViewById(R.id.filter_1);
        Button filter_2_button = v.findViewById(R.id.filter_2);
        Button filter_3_button = v.findViewById(R.id.filter_3);
        Button filter_4_button = v.findViewById(R.id.filter_4);
        Button apply_filters_button = v.findViewById(R.id.apply_filters);

        // Hardcoding over hashmap
        filter_1_button.setSelected(filter_status.get(0));
        filter_2_button.setSelected(filter_status.get(1));
        filter_3_button.setSelected(filter_status.get(2));
        filter_4_button.setSelected(filter_status.get(3));

        apply_filters_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(),
                        "Filters Applied!", Toast.LENGTH_SHORT)
                        .show();
                // send data over
                shared.sendFilterValues(filter_status);
                dismiss();
            }
        });

        filter_1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                filter_status.set(0, !filter_status.get(0));
                filter_1_button.setSelected(filter_status.get(0));
                Toast.makeText(getActivity(),
                        "Filter 1 selected", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        filter_2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                filter_status.set(1, !filter_status.get(1));
                filter_2_button.setSelected(filter_status.get(1));
                Toast.makeText(getActivity(),
                        "Filter 2 selected", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        filter_3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                filter_status.set(2, !filter_status.get(2));
                filter_3_button.setSelected(filter_status.get(2));
                Toast.makeText(getActivity(),
                        "Filter 3 selected", Toast.LENGTH_SHORT)
                        .show();
//                dismiss();
            }
        });

        filter_4_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                filter_status.set(3, !filter_status.get(3));
                filter_4_button.setSelected(filter_status.get(3));
                Toast.makeText(getActivity(),
                        "Filter 4 selected", Toast.LENGTH_SHORT)
                        .show();
//                dismiss();
            }
        });
        return v;
    }
}