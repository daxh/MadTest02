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

public class FilteredRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private ArrayList<Item> originalItems;
    private ArrayList<Item> filteredItems;

    private ItemFilter itemFilter;

    public FilteredRecyclerViewAdapter(ArrayList<Item> items) {
        this.originalItems = items;

        filteredItems = new ArrayList<>(this.originalItems);
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
        holder.bindItem(filteredItems.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredItems == null ? 0 : filteredItems.size();
    }

    public ItemFilter getItemFilter() {
        return itemFilter;
    }

    public class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence keyword) {

            FilterResults results = new FilterResults();

            final ArrayList<Item> tempFilterList = new ArrayList<>();

            for (Item originalItem : originalItems) {
                if (originalItem.getText().contains(keyword)) {
                    tempFilterList.add(originalItem);
                }
            }

            results.values = tempFilterList;
            results.count = tempFilterList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredItems.clear();
            filteredItems = (ArrayList<Item>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}