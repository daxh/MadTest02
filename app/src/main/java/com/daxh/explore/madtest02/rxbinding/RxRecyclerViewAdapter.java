package com.daxh.explore.madtest02.rxbinding;


import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/** Static factory methods for creating {@linkplain Observable observables} for {@link RecyclerView.Adapter}. */
public final class RxRecyclerViewAdapter {
    /**
     * Create an observable of item range inserted events for {@code RecyclerView.adapter}.
     * <p>
     * <em>Note:</em> A value will be emitted immediately on subscribe.
     */
    @CheckResult
    @NonNull
    public static <T extends RecyclerView.Adapter> Observable<RecyclerAdapterItemRangeInsertedEvent> itemRangeInserted(
            @NonNull T adapter) {
        checkNotNull(adapter, "adapter == null");
        return Observable.create(new RecyclerAdapterItemRangeInsertedOnSubscribe(adapter));
    }

    private RxRecyclerViewAdapter() {
        throw new AssertionError("No instances.");
    }
}
