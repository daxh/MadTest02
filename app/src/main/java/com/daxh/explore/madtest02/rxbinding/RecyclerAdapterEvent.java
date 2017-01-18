package com.daxh.explore.madtest02.rxbinding;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * A target recyclerView adapter  on which an event occurred.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated {@link Context}.
 * // TODO: really, keep ?
 */

public abstract class RecyclerAdapterEvent<T extends RecyclerView.Adapter> {
    private final T recyclerAdapter;

    protected RecyclerAdapterEvent(@NonNull T recyclerAdapter) {
        this.recyclerAdapter = recyclerAdapter;
    }

    /** The recyclerAdapter from which this event occurred. */
    @NonNull public T recyclerAdapter() {
        return recyclerAdapter;
    }
}
