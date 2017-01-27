package com.daxh.explore.madtest02.classic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class GenericRecyclerViewAdapter<Item, Parsed> extends
        RecyclerView.Adapter<GenericRecyclerViewAdapter.GenericViewHolder<Item, Parsed>> {

    private int itemViewId;
    private ArrayList<Item> items;
    private final GenericViewHolderParser<Parsed> parser;
    private final GenericViewHolderBinder binder;

    public GenericRecyclerViewAdapter(ArrayList<Item> items, int itemViewId, GenericViewHolderParser<Parsed> parser, GenericViewHolderBinder binder) {
        this.itemViewId = itemViewId;
        this.items = items;
        this.parser = parser;
        this.binder = binder;
    }

    @Override
    public GenericViewHolder<Item, Parsed> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(itemViewId, parent, false);
        GenericViewHolder<Item, Parsed> vh = new GenericViewHolder<>(itemView);
        vh.parsed = parser.parse(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(GenericViewHolder<Item, Parsed> holder, int position) {
        holder.bindItem(items.get(position));
        binder.bind(holder);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class GenericViewHolder<I, P> extends RecyclerView.ViewHolder {
        private I item;
        private P parsed;

        public GenericViewHolder(View rootView) {
            super(rootView);
        }

        public void bindItem(I item) {
            this.item = item;
        }

        public I getItem() {
            return item;
        }

        public P getParsed() {
            return parsed;
        }
    }

    public interface GenericViewHolderParser<P> {
        P parse(View rootView);
    }

    public interface GenericViewHolderBinder {
        void bind(GenericRecyclerViewAdapter.GenericViewHolder viewHolder);
    }
}
