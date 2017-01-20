package com.daxh.explore.madtest02.classic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class GenericRecyclerViewAdapter<T, V> extends
        RecyclerView.Adapter<GenericRecyclerViewAdapter.GenericViewHolder<T, V>> {

    private int itemViewId;
    private ArrayList<T> items;
    private final GenericViewHolderParser<V> parser;
    private final GenericViewHolderBinder binder;

    public GenericRecyclerViewAdapter(ArrayList<T> items, int itemViewId, GenericViewHolderParser<V> parser, GenericViewHolderBinder binder) {
        this.itemViewId = itemViewId;
        this.items = items;
        this.parser = parser;
        this.binder = binder;
    }

    @Override
    public GenericViewHolder<T, V> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(itemViewId, parent, false);
        GenericViewHolder<T, V> vh = new GenericViewHolder<>(itemView);
        vh.tree = parser.parse(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder<T, V> holder, int position) {
        holder.bindItem(items.get(position));
        binder.bind(holder);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class GenericViewHolder<TT, VV> extends RecyclerView.ViewHolder {

        private TT item;
        private VV tree;

        public GenericViewHolder(View rootView) {
            super(rootView);
        }

        public void bindItem(TT item) {
            this.item = item;
        }

        public TT getItem() {
            return item;
        }

        public VV getTree() {
            return tree;
        }
    }

    public interface GenericViewHolderParser<VV> {
        VV parse(View rootView);
    }

    public interface GenericViewHolderBinder {
        void bind(GenericRecyclerViewAdapter.GenericViewHolder viewHolder);
    }
}
