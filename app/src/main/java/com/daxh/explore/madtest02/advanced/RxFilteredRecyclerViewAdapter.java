package com.daxh.explore.madtest02.advanced;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;
import com.daxh.explore.madtest02.common.ItemViewHolder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class RxFilteredRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    // TODO: Add Optionals
    private ArrayList<Item> originalItems;
    private ArrayList<Item> filteredItems;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public RxFilteredRecyclerViewAdapter(ArrayList<Item> items, Observable<String> keywords) {
        this.originalItems = items;
        filteredItems = new ArrayList<>(this.originalItems);

        keywords
                .debounce(10, TimeUnit.MILLISECONDS)
                .subscribe(keyword -> { compositeSubscription.add(Observable.from(originalItems)
                        .subscribeOn(Schedulers.computation())
                        .filter(item -> item.getText().contains(keyword))
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(filteredItems -> {
                            this.filteredItems.clear();
                            this.filteredItems = (ArrayList<Item>) filteredItems;
                            notifyDataSetChanged();
                        }, Throwable::printStackTrace));
                }, Throwable::printStackTrace);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bindItem(filteredItems.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredItems == null ? 0 : filteredItems.size();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        compositeSubscription.unsubscribe();
    }
}