package com.daxh.explore.madtest02.classic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;


public class GenericRecyclerViewAdapter<T> extends
        RecyclerView.Adapter<GenericRecyclerViewAdapter.GenericViewHolder<T>> {

    private int itemViewId;
    private ArrayList<T> items;
    private final GenericViewHolderParser parser;
    private final GenericViewHolderBinder binder;

    public GenericRecyclerViewAdapter(ArrayList<T> items, int itemViewId, GenericViewHolderParser parser, GenericViewHolderBinder binder) {
        this.itemViewId = itemViewId;
        this.items = items;
        this.parser = parser;
        this.binder = binder;
    }

    @Override
    public GenericViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(itemViewId, parent, false);
        GenericViewHolder<T> vh = new GenericViewHolder<T>(itemView);
        vh.tree = parser.parse(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder<T> holder, int position) {
        holder.bindItem(items.get(position));
        binder.bind(holder);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class GenericViewHolder<U> extends RecyclerView.ViewHolder {

        private U item;
        private HashMap<Integer, ? extends View> tree;

        public GenericViewHolder(View rootView) {
            super(rootView);
        }

        public void bindItem(U item) {
            this.item = item;
        }

        public U getItem() {
            return item;
        }

        public HashMap<Integer, ? extends View> getTree() {
            return tree;
        }
    }

    public interface GenericViewHolderParser {
        HashMap<Integer, ? extends View> parse(View rootView);
    }

    public interface GenericViewHolderBinder {
        void bind(GenericRecyclerViewAdapter.GenericViewHolder viewHolder);
    }
}
