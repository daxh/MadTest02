package com.daxh.explore.madtest02.simple;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Header;
import com.daxh.explore.madtest02.common.HeaderViewHolder;
import com.daxh.explore.madtest02.common.Item;
import com.daxh.explore.madtest02.common.ItemViewHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class RecyclerViewWithHeadersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER    = 0x0001;
    private static final int TYPE_ITEM      = 0x0002;

    private LinkedHashMap<Header, ArrayList<Item>> dataset;

    private ArrayList<Object> items;
    private ItemFilter itemFilter;

    public RecyclerViewWithHeadersAdapter(LinkedHashMap<Header, ArrayList<Item>> dataset) {
        this.dataset = dataset;

        items = new ArrayList<>();
        itemFilter = new ItemFilter();

        itemFilter.filter("");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_view, parent, false);
            return new HeaderViewHolder(headerView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view, parent, false);
            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEADER) {
            ((HeaderViewHolder)holder).bindItem((Header) items.get(position));
        } else {
            ((ItemViewHolder)holder).bindItem((Item) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Header) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    // According to official documentation:
    // https://developer.android.com/reference/android/widget/Filter.html
    // This class allows to execute filtering operations
    // asynchronously and properly cancel that iterations
    // that became un-relevant. Obviously, we using it here,
    // but for flatting original dataset to items list that
    // could be easily displayed. Probably there is a more
    // true way to do it, but right now I didn't know it.
    public class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            FilterResults results = new FilterResults();
            final ArrayList<Object> tmpItemsList = new ArrayList<>();

            final Set<Header> headers = dataset.keySet();
            for (Header header : headers) {
                tmpItemsList.add(header);
                tmpItemsList.addAll(dataset.get(header));
            }

            results.values = tmpItemsList;
            results.count = tmpItemsList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            items.clear();
            items = (ArrayList<Object>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
