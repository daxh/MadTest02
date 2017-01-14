package com.daxh.explore.madtest02.classic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Header;
import com.daxh.explore.madtest02.common.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;


public class InfiniteRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView rvItems;
    private LinearLayoutManager llmItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        llmItems = new LinearLayoutManager(this);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        rvItems.setLayoutManager(llmItems);

        // Preparing data
        String [] dataArray = getResources().getStringArray(R.array.lorem_ipsum);
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(dataArray[0].split(" ")));

        // Why we need this LinkedHashMap? Why
        // we couldn't use simple ArrayList? In
        // this particular case there are no any
        // real benefits. But in future, when we
        // will extend this example, this approach
        // will helps us to easily add filtering,
        // sorting (for items, or for headers)
        // and so on. So I believe that it is better
        // to keep all data structure and items
        // inter-dependencies directly.
        LinkedHashMap<Header, ArrayList<Item>> dataset = new LinkedHashMap<>();
        final int step = 5;
        for (int i = 0; i < strings.size();) {
            Header header = new Header(strings.get(i));
            i++;

            ArrayList<Item> items = new ArrayList<>();
            for (int j = 0; j < step && i < strings.size(); j++) {
                items.add(new Item(strings.get(i)));
                i++;
            }

            dataset.put(header, items);
        }

        // Creating adapter
        InfiniteRecyclerViewAdapter adapter = new InfiniteRecyclerViewAdapter(dataset);

        // Setting up adapter for RecyclerView
        rvItems.setAdapter(adapter);
    }
}
