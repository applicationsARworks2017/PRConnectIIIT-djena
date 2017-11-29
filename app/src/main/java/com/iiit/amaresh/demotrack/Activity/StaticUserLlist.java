package com.iiit.amaresh.demotrack.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.iiit.amaresh.demotrack.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mobileapplication on 11/29/17.
 */

public class StaticUserLlist extends AppCompatActivity {

    ListView static_listView;
    SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.iiit.amaresh.demotrack.R.layout.activity_static_userlist);

        static_listView=(ListView)findViewById(R.id.static_listView);
        search=(SearchView)findViewById(R.id.searchView1);

        // Initializing a new String Array
        String[] fruits = new String[] {
                "Drink",
                "Capuli cherry"
        };

        // Create a List from String Array elements
        final List<String> fruits_list = new ArrayList<String>(Arrays.asList(fruits));

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list);

        // DataBind ListView with items from ArrayAdapter
        static_listView.setAdapter(arrayAdapter);
    }
}
