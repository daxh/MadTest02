package com.daxh.explore.madtest02.classic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class SortedRecyclerViewActivity extends AppCompatActivity {

    private Button btSortingOff;
    private Button btSortByLength;
    private Button btSortByOrder;

    private RecyclerView rvItems;
    private LinearLayoutManager llmItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorted_recyclerview);

        llmItems = new LinearLayoutManager(this);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        rvItems.setLayoutManager(llmItems);

        // Preparing data
        String [] dataArray = getResources().getStringArray(R.array.lorem_ipsum);
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(dataArray[0].split(" ")));
        ArrayList<Item> items = new ArrayList<>();
        for (String s : strings) items.add(new Item(s));

        // Creating adapter
        SortedRecyclerViewAdapter adapter = new SortedRecyclerViewAdapter(items);

        // Setting up adapter for RecyclerView
        rvItems.setAdapter(adapter);

        btSortingOff = (Button) findViewById(R.id.btSortingOff);
        btSortByLength = (Button) findViewById(R.id.btSortByLength);
        btSortByOrder = (Button) findViewById(R.id.btSortByOrder);

        btSortingOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.sort(null);
            }
        });

        btSortByLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.sort(new LengthComparator());
            }
        });
        btSortByOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.sort(new OrderComparator());
            }
        });
    }

    public static class LengthComparator implements Comparator<Item> {
        @Override
        public int compare(Item item1, Item item2) {
            if (item1.getText().length() < item2.getText().length()) return -1;
            if (item1.getText().length() > item2.getText().length()) return 1;
            return 0;
        }
    }

    public static class OrderComparator implements Comparator<Item> {
        @Override
        public int compare(Item item1, Item item2) {
            final String str1 = item1.getText();
            final String str2 = item2.getText();
            int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
            if (res == 0) {
                res = str1.compareTo(str2);
            }
            return res;
        }
    }
}
