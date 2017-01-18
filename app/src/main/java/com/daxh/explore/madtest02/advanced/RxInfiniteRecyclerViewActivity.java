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
import com.daxh.explore.madtest02.utils.LoggerUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxInfiniteRecyclerViewActivity extends RxAppCompatActivity {

    private Optional<RecyclerView> rvItems;
    private LinearLayoutManager llmItems;

    private LinkedList<ArrayList<Object>> pages;
    private Subscription lrtSubscr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        LoggerUtils.brief();

        llmItems = new LinearLayoutManager(this);
        rvItems = Optional.ofNullable((RecyclerView) findViewById(R.id.rvItems))
            .executeIfPresent(rv -> rv.setLayoutManager(llmItems));

        Optional.ofNullable((Button) findViewById(R.id.btLarge))
                .ifPresent(bt -> {
                    RxView.clicks(bt).subscribe(aVoid -> {parseData(R.array.lorem_ipsum);});
                    bt.performClick();
                });

        Optional.ofNullable((Button) findViewById(R.id.btMedium))
                .ifPresent(bt -> RxView.clicks(bt).subscribe(aVoid -> {parseData(R.array.medium);}));

        Optional.ofNullable((Button) findViewById(R.id.btSmall))
                .ifPresent(bt -> RxView.clicks(bt).subscribe(aVoid -> {parseData(R.array.small);}));
    }

    private void parseData(int resId){
        if (lrtSubscr != null && !lrtSubscr.isUnsubscribed()) {
            lrtSubscr.unsubscribe();
            lrtSubscr = null;
        }

        // Preparing data
        pages = Stream.of(getResources().getStringArray(resId))
                .map(s -> Stream.of(s.split(" "))
                        .map(Item::new)
                        .collect(Collectors.toCollection(ArrayList<Object>::new))
                ).collect(Collectors.toCollection(LinkedList::new));

        // Creating adapter
        RxInfiniteRecyclerViewAdapter adapter = new RxInfiniteRecyclerViewAdapter(pages.removeFirst(), new InfiniteScrollingListener());
        // Setting up adapter for RecyclerView
        rvItems.ifPresent(rv -> rv.setAdapter(adapter));
    }

    private void asyncLoadNextPage(final RxInfiniteRecyclerViewAdapter adapter) {
        if (lrtSubscr != null && !lrtSubscr.isUnsubscribed()) {
            return;
        }

        lrtSubscr = Observable.fromCallable(() -> {
                    Logger.d("LRT START");
                    Thread.sleep(2000);
                    Logger.d("LRT END");
                    return pages.size() > 0 ? pages.removeFirst() : null;
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() -> adapter.showProgress(true))
                .doOnUnsubscribe(() -> {
                    // This is just an example that we need
                    // to destroy our LRT in case of cancellation
                    // and free related threads and resources.
                    // But such code probably should never be
                    // used in production as we should manage
                    // properly our long running operations
                    if (lrtSubscr != null) {
                        Logger.d("doOnUnsubscribe");
                        Thread.currentThread().interrupt();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(objects -> {
                        if (objects != null && objects.size() > 0) {
                            adapter.addAll(objects);
                        } else {
                            adapter.showProgress(false);
                        }
                    },
                    throwable -> {
                        Logger.d(throwable.getLocalizedMessage());
                        adapter.showProgress(false);
                    }, () -> lrtSubscr = null
                );
    }

    public class InfiniteScrollingListener implements RxInfiniteRecyclerViewAdapter.Listener {
        @Override
        public void onNeedMoreData(RxInfiniteRecyclerViewAdapter adapter) {
            asyncLoadNextPage(adapter);
        }

        @Override
        public void onPlaceForMoreDataAvailable(RxInfiniteRecyclerViewAdapter adapter) {
            asyncLoadNextPage(adapter);
        }

        @Override
        public void onDataInserted(RxInfiniteRecyclerViewAdapter adapter) {
            if (lrtSubscr != null && !lrtSubscr.isUnsubscribed()) {
                return;
            }
            adapter.showProgress(false);
        }
    }
}
