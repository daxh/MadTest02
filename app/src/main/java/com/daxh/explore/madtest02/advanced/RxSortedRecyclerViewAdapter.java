package com.daxh.explore.madtest02.advanced;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Optional;
import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;
import com.daxh.explore.madtest02.common.ItemViewHolder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class RxSortedRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private Optional<ArrayList<Item>> originalItems;
    private Optional<ArrayList<Item>> filteredItems;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public RxSortedRecyclerViewAdapter(ArrayList<Item> items) {
        this.originalItems = Optional.ofNullable(items);
        this.originalItems
                .executeIfPresent(oi -> filteredItems = Optional.of(new ArrayList<>(oi)))
                .executeIfAbsent(() -> filteredItems = Optional.of(new ArrayList<Item>()));
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        filteredItems.ifPresent(fi -> holder.bindItem(fi.get(position)));
    }

    @Override
    public int getItemCount() {
        return filteredItems.mapToInt(ArrayList::size).orElse(0);
    }

    public void sort(Observable<Func2<Item, Item, Integer>> observableComparator) {
        // We using Rx to perform all sorting operations
        // on background thread and update results on the
        // main thread. 'debounce' operator used to throttle
        // some ongoing events, as we don't want to have any
        // so called 'backpressure problems'. These 2 aspects
        // (thread switching and 'debounce') need here to
        // replace Filter class, that was used in classic
        // implementation.
        Optional.ofNullable(observableComparator).ifPresent(c -> c
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribe(comparator -> { compositeSubscription.add(Observable.from(originalItems.get())
                        .subscribeOn(Schedulers.computation())
                        .toSortedList(comparator)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(fi -> {
                            filteredItems.ifPresent(ArrayList::clear);
                            filteredItems = Optional.of(new ArrayList<>(fi));
                            notifyDataSetChanged();
                        }));
                }));
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        compositeSubscription.unsubscribe();
    }
}