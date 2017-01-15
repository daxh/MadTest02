package com.daxh.explore.madtest02.classic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;
import com.daxh.explore.madtest02.common.ItemViewHolder;
import com.daxh.explore.madtest02.common.Progress;
import com.daxh.explore.madtest02.common.ProgressViewHolder;

import java.util.ArrayList;

public class InfiniteRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS  = 0x0001;
    private static final int TYPE_ITEM      = 0x0002;

    private ArrayList<Object> originalItems;

    private ArrayList<Object> attachedItems;
    private ItemFilter itemFilter;

    private boolean isProgressShown = false;

    private Progress progress;
    public InfiniteRecyclerViewAdapter(ArrayList<Object> items) {
        this.originalItems = items;

        itemFilter = new ItemFilter();
    }

    public boolean isProgressShown() {
        return isProgressShown;
    }

    public void showProgress(boolean show) {
        if (show && !isProgressShown) {
            isProgressShown = true;
            progress = new Progress();
            originalItems.add(progress);
            notifyItemInserted(originalItems.indexOf(progress));
        } else if(!show && isProgressShown) {
            int pos = originalItems.indexOf(progress);
            originalItems.remove(progress);
            notifyItemRemoved(pos);
            progress = null;
            isProgressShown = false;
        }
    }

    public void addAll(ArrayList<Object> items){
        attachedItems = items;
        itemFilter.filter("");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PROGRESS) {
            View progressView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_view, parent, false);
            return new ProgressViewHolder(progressView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_view, parent, false);
            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_PROGRESS) {
            ((ProgressViewHolder)holder).bindItem((Progress) originalItems.get(position));
        } else {
            ((ItemViewHolder)holder).bindItem((Item) originalItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return originalItems == null ? 0 : originalItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (originalItems.get(position) instanceof Progress) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    // According to official documentation:
    // https://developer.android.com/reference/android/widget/Filter.html
    // This class allows to execute filtering operations
    // asynchronously and properly cancel iterations that
    // became un-relevant. Obviously, we using it here, but
    // for appending new data to original dataset. Probably
    // there is a more true way to do it, but right now I
    // didn't know it.
    public class ItemFilter extends Filter {

        private int startPos;

        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            FilterResults results = new FilterResults();
            final ArrayList<Object> tmpItemsList = new ArrayList<>(originalItems);

            tmpItemsList.addAll(attachedItems);
            startPos = originalItems.size()-(isProgressShown ? 2 : 1);

            attachedItems.clear();
            attachedItems = null;

            results.values = tmpItemsList;
            results.count = tmpItemsList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            originalItems = (ArrayList<Object>) filterResults.values;
            notifyItemRangeInserted(startPos, originalItems.size());
        }
    }
}
