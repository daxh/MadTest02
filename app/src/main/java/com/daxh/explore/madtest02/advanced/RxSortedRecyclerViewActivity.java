package com.daxh.explore.madtest02.advanced;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;

import rx.functions.Func2;

public class RxSortedRecyclerViewActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorted_recyclerview);

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
        RxSortedRecyclerViewAdapter adapter = new RxSortedRecyclerViewAdapter(items);

        // Setting up adapter for RecyclerView
        rvItems.ifPresent(rv -> rv.setAdapter(adapter));

        Optional.ofNullable((Button) findViewById(R.id.btSortingOff))
                .ifPresent(bt -> adapter.sort(RxView.clicks(bt)
                        .compose(bindToLifecycle())
                        .map(aVoid -> (Func2<Item, Item, Integer>) (item1, item2) -> 0)
                ));

        Optional.ofNullable((Button) findViewById(R.id.btSortByLength))
                .ifPresent(bt -> adapter.sort(RxView.clicks(bt)
                        .compose(bindToLifecycle())
                        .map(aVoid -> (Func2<Item, Item, Integer>) (item1, item2) -> {
                            if (item1.getText().length() < item2.getText().length()) return -1;
                            if (item1.getText().length() > item2.getText().length()) return 1;
                            return 0;
                        })
                ));

        Optional.ofNullable((Button) findViewById(R.id.btSortByOrder))
                .ifPresent(bt -> adapter.sort(RxView.clicks(bt)
                        .compose(bindToLifecycle())
                        .map(aVoid -> (Func2<Item, Item, Integer>) (item1, item2) -> {
                            final String str1 = item1.getText();
                            final String str2 = item2.getText();
                            int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                            if (res == 0) {
                                res = str1.compareTo(str2);
                            }
                            return res;
                        })
                ));
    }
}