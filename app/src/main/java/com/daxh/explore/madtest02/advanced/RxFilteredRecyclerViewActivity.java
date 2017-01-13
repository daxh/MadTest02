package com.daxh.explore.madtest02.advanced;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.widget.EditText;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;

import rx.functions.Func1;

public class RxFilteredRecyclerViewActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_recyclerview);

        LinearLayoutManager llmItems = new LinearLayoutManager(this);
        Optional<RecyclerView> rvItems = Optional.ofNullable((RecyclerView) findViewById(R.id.rvItems));
        rvItems.ifPresent(rv -> rv.setLayoutManager(llmItems));

        // Preparing data
        ArrayList<Item> items = Stream.of(getResources().getStringArray(R.array.lorem_ipsum))
                .findFirst()
                .map(s -> Stream.of(s.split(" "))
                        .map(Item::new)
                        .collect(Collectors.toCollection(ArrayList::new))).orElse(new ArrayList<>());

        // Creating adapter
        RxFilteredRecyclerViewAdapter adapter = new RxFilteredRecyclerViewAdapter(items);

        // Setting up adapter for RecyclerView
        rvItems.ifPresent(rv -> rv.setAdapter(adapter));

        Optional.ofNullable((EditText) findViewById(R.id.etKeyword))
            .ifPresent(et -> adapter.filter(RxTextView.afterTextChangeEvents(et)
                    .compose(bindToLifecycle())
                    .map(event -> (Func1<Item, Boolean>) (item) -> item.getText().contains(
                            Optional.ofNullable(event.editable()).map(Editable::toString).orElse("")))
            ));
    }
}