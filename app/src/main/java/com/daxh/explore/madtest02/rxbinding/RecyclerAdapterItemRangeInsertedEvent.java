package com.daxh.explore.madtest02.rxbinding;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding.view.ViewEvent;

/**
 * A item range inserted event on a recyclerView adapter.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the recyclerView. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
public class RecyclerAdapterItemRangeInsertedEvent extends RecyclerAdapterEvent{

    @CheckResult @NonNull
    public static RecyclerAdapterItemRangeInsertedEvent create(@NonNull RecyclerView.Adapter recyclerAdapter, int positionStart, int itemCount) {
        return new RecyclerAdapterItemRangeInsertedEvent(recyclerAdapter, positionStart, itemCount);
    }

    private final int positionStart;
    private final int itemCount;

    protected RecyclerAdapterItemRangeInsertedEvent(@NonNull RecyclerView.Adapter recyclerAdapter, int positionStart, int itemCount) {
        super(recyclerAdapter);
        this.positionStart = positionStart;
        this.itemCount = itemCount;
    }

    public int positionStart() {
        return positionStart;
    }

    public int itemCount() {
        return itemCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof RecyclerAdapterItemRangeInsertedEvent)) return false;
        RecyclerAdapterItemRangeInsertedEvent other = (RecyclerAdapterItemRangeInsertedEvent) o;
        return other.recyclerAdapter() == recyclerAdapter()
                && positionStart == other.positionStart
                && itemCount == other.itemCount;
    }

    @Override public int hashCode() {
        // TODO: 17 ? 37 ?
        int result = 17;
        result = result * 37 + recyclerAdapter().hashCode();
        result = result * 37 + positionStart;
        result = result * 37 + itemCount;
        return result;
    }

    @Override public String toString() {
        return "RecyclerAdapterItemRangeInsertedEvent{recyclerAdapter="
                + recyclerAdapter()
                + ", dx="
                + positionStart
                + ", dy="
                + itemCount
                + '}';
    }
}
