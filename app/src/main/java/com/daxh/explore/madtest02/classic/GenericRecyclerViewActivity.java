package com.daxh.explore.madtest02.classic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;

import java.util.ArrayList;
import java.util.Arrays;

public class GenericRecyclerViewActivity extends AppCompatActivity {

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
        ArrayList<Item> items = new ArrayList<>();
        for (String s : strings) items.add(new Item(s));

        // Creating adapter
        GenericRecyclerViewAdapter<Item, ParsedItemViews> adapter = new GenericRecyclerViewAdapter<>(
                items, R.layout.item_view,
                new GenericRecyclerViewAdapter.GenericViewHolderParser<ParsedItemViews>() {
                    @Override
                    public ParsedItemViews parse(View rootView) {
                        ParsedItemViews parsedItemViews = new ParsedItemViews();
                        parsedItemViews.tvText = (TextView) rootView.findViewById(R.id.tvText);

                        rootView.setOnClickListener(parsedItemViews);
                        return parsedItemViews;
                    }
                },
                new GenericRecyclerViewAdapter.GenericViewHolderBinder() {
                    @Override
                    public void bind(GenericRecyclerViewAdapter.GenericViewHolder viewHolder) {
                        Item item = (Item) viewHolder.getItem();
                        ParsedItemViews parsedItemViews = (ParsedItemViews) viewHolder.getParsed();

                        parsedItemViews.title = item.getText();
                        parsedItemViews.tvText.setText(item.getText());
                    }
                }
        );

        // Setting up adapter for RecyclerView
        rvItems.setAdapter(adapter);
    }

    public static class ParsedItemViews implements View.OnClickListener {
        public String title;
        public TextView tvText;

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), title + " clicked!", Toast.LENGTH_SHORT).show();
        }
    }
}
