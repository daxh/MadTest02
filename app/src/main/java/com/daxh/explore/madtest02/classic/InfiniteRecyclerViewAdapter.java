package com.daxh.explore.madtest02.classic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;
import com.daxh.explore.madtest02.common.ItemViewHolder;
import com.daxh.explore.madtest02.common.Progress;
import com.daxh.explore.madtest02.common.ProgressViewHolder;

import java.util.ArrayList;

public class InfiniteRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS  = 0x0001;
    private static final int TYPE_ITEM      = 0x0002;

    private ArrayList<Object> items;

    private boolean isProgressShown = false;
    private Progress progress;

    public InfiniteRecyclerViewAdapter(ArrayList<Object> items) {
        this.items = items;
    }

    public boolean isProgressShown() {
        return isProgressShown;
    }

    public void showProgress(boolean show) {
        if (show && !isProgressShown) {
            isProgressShown = true;
            progress = new Progress();
            items.add(progress);
            notifyItemInserted(items.indexOf(progress));
        } else if(!show && isProgressShown) {
            int pos = items.indexOf(progress);
            items.remove(progress);
            notifyItemRemoved(pos);
            progress = null;
            isProgressShown = false;
        }
    }

    public void addAll(ArrayList<Object> items){
        int startPos = this.items.size()-(isProgressShown ? 2 : 1);
        this.items.addAll(items);

        notifyItemRangeInserted(startPos, items.size());
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
            ((ProgressViewHolder)holder).bindItem((Progress) items.get(position));
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
        if (items.get(position) instanceof Progress) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }
}
