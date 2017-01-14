package com.daxh.explore.madtest02.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;

import java.util.ArrayList;
import java.util.Arrays;


public class MutableRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView rvItems;
    private LinearLayoutManager llmItems;
    private MutableRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mutable_recyclerview);

        llmItems = new LinearLayoutManager(this);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        rvItems.setLayoutManager(llmItems);

        // Preparing data
        String [] dataArray = getResources().getStringArray(R.array.lorem_ipsum);
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(dataArray[0].split(" ")));
        ArrayList<Item> items = new ArrayList<>();
        for (String s : strings) items.add(new Item(s));

        // Creating adapter
        adapter = new MutableRecyclerViewAdapter(items);

        // Setting up adapter for RecyclerView
        rvItems.setAdapter(adapter);

        ((EditText)findViewById(R.id.etCommand)).setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    parseCommand(textView.getText().toString());
                }
                return false;
            }
        });
    }

    private void parseCommand(String command) {
        String [] parts = command.split(" ");
        switch (parts[0]){
            case "a":
                adapter.add(new Item(parts[1]));
                break;
            case "i":
                adapter.insert(new Item(parts[1]), Integer.parseInt(parts[2]));
                break;
            case "r":
                adapter.replace(new Item(parts[1]), Integer.parseInt(parts[2]));
                break;
            case "m":
                adapter.move(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                break;
            case "d":
                adapter.remove(Integer.parseInt(parts[1]));
                break;
        }
    }
}
