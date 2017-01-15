package com.daxh.explore.madtest02.classic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class InfiniteRecyclerViewActivity extends AppCompatActivity {

    private Button btLarge;
    private Button btMedium;
    private Button btSmall;

    private RecyclerView rvItems;
    private LinearLayoutManager llmItems;
    private LinkedList<ArrayList<Object>> pages;
    private AsyncTask<Object, Object, ArrayList<Object>> longRunningTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        llmItems = new LinearLayoutManager(this);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        rvItems.setLayoutManager(llmItems);

        btLarge = (Button) findViewById(R.id.btLarge);
        btLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseData(R.array.lorem_ipsum);
            }
        });
        btLarge.performClick();

        btMedium = (Button) findViewById(R.id.btMedium);
        btMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseData(R.array.medium);
            }
        });

        btSmall = (Button) findViewById(R.id.btSmall);
        btSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseData(R.array.small);
            }
        });
    }

    private void parseData(int resId){
        if (longRunningTask != null && (!longRunningTask.isCancelled() || longRunningTask.getStatus() != AsyncTask.Status.FINISHED)) {
            longRunningTask.cancel(true);
        }

        // Preparing data
        String [] dataArray = getResources().getStringArray(resId);
        pages = new LinkedList<>();
        for (String data : dataArray) {
            ArrayList<String> strings = new ArrayList<>(Arrays.asList(data.split(" ")));
            ArrayList<Object> items = new ArrayList<>();
            for (String s : strings) items.add(new Item(s));
            pages.add(items);
        }

        // Creating adapter
        InfiniteRecyclerViewAdapter adapter = new InfiniteRecyclerViewAdapter(pages.removeFirst(), new InfiniteScrollingListener());

        // Setting up adapter for RecyclerView
        rvItems.setAdapter(adapter);
    }

    private void asyncLoadNextPage(final InfiniteRecyclerViewAdapter adapter) {
        // This is just a dirty example of long
        // running longRunningTask. Obviously we shouldn't
        // do such things in production code.
        if (longRunningTask != null &&
                longRunningTask.getStatus() != AsyncTask.Status.FINISHED) {
            return;
        }

        longRunningTask = new AsyncTask<Object, Object, ArrayList<Object>>() {

            @Override
            protected void onPreExecute() {
                adapter.showProgress(true);
            }

            @Override
            protected ArrayList<Object> doInBackground(Object... voids) {
                SystemClock.sleep(2000);
                return pages.size() > 0 ? pages.removeFirst() : null;
            }

            @Override
            protected void onPostExecute(ArrayList<Object> objects) {
                if (objects != null && objects.size() > 0) {
                    adapter.addAll(objects);
                }
                else {
                    adapter.showProgress(false);
                }
            }
        };
        longRunningTask.execute();
    }

    public class InfiniteScrollingListener implements InfiniteRecyclerViewAdapter.Listener {
        @Override
        public void onNeedMoreData(InfiniteRecyclerViewAdapter adapter) {
            asyncLoadNextPage(adapter);
        }

        @Override
        public void onPlaceForMoreDataAvailable(InfiniteRecyclerViewAdapter adapter) {
            asyncLoadNextPage(adapter);
        }

        @Override
        public void onDataInserted(InfiniteRecyclerViewAdapter adapter) {
            if (longRunningTask != null &&
                    longRunningTask.getStatus() != AsyncTask.Status.FINISHED) {
                return;
            }
            adapter.showProgress(false);
        }
    }
}
