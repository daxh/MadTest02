package com.daxh.explore.madtest02.rxbinding;


import android.support.v7.widget.RecyclerView;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

public class RecyclerAdapterItemRangeInsertedOnSubscribe implements Observable.OnSubscribe<RecyclerAdapterItemRangeInsertedEvent> {
    final RecyclerView.Adapter adapter;

    RecyclerAdapterItemRangeInsertedOnSubscribe(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void call(Subscriber<? super RecyclerAdapterItemRangeInsertedEvent> subscriber) {
        verifyMainThread();

        final RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(RecyclerAdapterItemRangeInsertedEvent.create(adapter, positionStart, itemCount));
                }
            }
        };

        subscriber.add(new MainThreadSubscription() {
            @Override protected void onUnsubscribe() {
                adapter.unregisterAdapterDataObserver(observer);
            }
        });

        adapter.registerAdapterDataObserver(observer);
    }
}
