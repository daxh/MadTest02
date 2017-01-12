package com.daxh.explore.madtest02.advanced;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import rx.Observable;

public class RxFilteredRecyclerViewActivity extends RxAppCompatActivity {

    private RecyclerView rvItems;
    private LinearLayoutManager llmItems;
    private EditText etKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_recyclerview);

        llmItems = new LinearLayoutManager(this);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        rvItems.setLayoutManager(llmItems);

        // Preparing data
        String [] dataArray = getResources().getStringArray(R.array.lorem_ipsum);
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(dataArray[0].split(" ")));
        ArrayList<Item> items = new ArrayList<>();
        for (String s : strings) items.add(new Item(s));

        etKeyword = (EditText) findViewById(R.id.etKeyword);
        Observable<String> keywordsObservable = RxTextView
                .afterTextChangeEvents(etKeyword)
                .compose(bindToLifecycle())
                .map(event -> event.editable().toString());

        // Creating adapter
        RxFilteredRecyclerViewAdapter adapter = new RxFilteredRecyclerViewAdapter(items, keywordsObservable);

        // Setting up adapter for RecyclerView
        rvItems.setAdapter(adapter);
    }
}