package com.daxh.explore.madtest02.classic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;

import java.util.ArrayList;
import java.util.Arrays;


public class ClassicRecyclerViewUsageExampleActivity extends AppCompatActivity {

    private RecyclerView rvItems;
    private LinearLayoutManager llmItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_recyclerview_usage_example);

        llmItems = new LinearLayoutManager(this);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        rvItems.setLayoutManager(llmItems);

        // Preparing data
        String [] dataArray = getResources().getStringArray(R.array.lorem_ipsum);
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(dataArray[0].split(" ")));
        ArrayList<Item> items = new ArrayList<>();
        for (String s : strings) items.add(new Item(s));

        // Creating adapter
        ClassicRecyclerViewAdapter adapter = new ClassicRecyclerViewAdapter(items);

        // Setting up adapter for RecyclerView
        rvItems.setAdapter(adapter);
    }
}
