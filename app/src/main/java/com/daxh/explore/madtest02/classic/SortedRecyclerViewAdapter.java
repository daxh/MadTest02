package com.daxh.explore.madtest02.classic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;
import com.daxh.explore.madtest02.common.ItemViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortedRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private ArrayList<Item> originalItems;
    private ArrayList<Item> sortedItems;

    private ItemFilter itemFilter;
    private Comparator<Item> comparator = null;

    public SortedRecyclerViewAdapter(ArrayList<Item> items) {
        this.originalItems = items;

        sortedItems = new ArrayList<>(this.originalItems);
        itemFilter = new ItemFilter();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bindItem(sortedItems.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedItems == null ? 0 : sortedItems.size();
    }

    public void sort(Comparator<Item> comparator){
        this.comparator = comparator;
        itemFilter.filter("");
    }

    // According to official documentation:
    // https://developer.android.com/reference/android/widget/Filter.html
    // This class allows to execute filtering operations
    // asynchronously and properly cancel that iterations
    // that became un-relevant. Obviously, we using it here,
    // but for sorting. Probably there is a more true way
    // to do it, but right now I didn't know it.
    public class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            FilterResults results = new FilterResults();
            final ArrayList<Item> tempFilterList = new ArrayList<>(originalItems);

            if (comparator != null) {
                Collections.sort(tempFilterList, comparator);
            }

            results.values = tempFilterList;
            results.count = tempFilterList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            sortedItems.clear();
            sortedItems = (ArrayList<Item>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
