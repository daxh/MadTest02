package com.daxh.explore.madtest02.simple;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.common.Item;
import com.daxh.explore.madtest02.common.ItemViewHolder;

import java.util.ArrayList;

public class MutableRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private ArrayList<Item> items;

    public MutableRecyclerViewAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bindItem(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void add(Item item){
        items.add(item);

        notifyItemInserted(items.indexOf(item));
    }

    public void insert(Item item, int position){
        if (position <= items.size()) {
            items.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void replace(Item item, int position){
        if (position <= items.size()) {
            items.set(position, item);

            notifyItemChanged(position);
        }
    }

    public void move(int startPos, int endPos) {
        Item item = items.get(startPos);
        items.remove(startPos);
        items.add(endPos, item);

        notifyItemMoved(startPos, endPos);
    }

    public void remove(int position) {
        items.remove(position);

        notifyItemRemoved(position);
    }
}
