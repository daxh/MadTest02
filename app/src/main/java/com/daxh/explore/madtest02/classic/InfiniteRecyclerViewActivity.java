package com.daxh.explore.madtest02.classic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class InfiniteRecyclerViewActivity extends AppCompatActivity {

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

        // Preparing data
        String [] dataArray = getResources().getStringArray(R.array.lorem_ipsum);
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

    public class InfiniteScrollingListener implements InfiniteRecyclerViewAdapter.Listener {
        @Override
        public void onNeedMoreData(InfiniteRecyclerViewAdapter adapter) {
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
                    SystemClock.sleep(4000);
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

        @Override
        public void onDataInserted(InfiniteRecyclerViewAdapter adapter) {
            adapter.showProgress(false);
        }
    }
}
