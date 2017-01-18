package com.daxh.explore.madtest02.advanced;

import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
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
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxInfiniteRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS  = 0x0001;
    private static final int TYPE_ITEM      = 0x0002;

    private ArrayList<Object> originalItems;

    private boolean isProgressShown = false;
    private Progress progress;

    private Listener listener;

    private Subscription scroll;
    private int offset = 3;
    private boolean notify = true;

    private DataObserver dataObserver;

    private LinearLayoutManager llm;

    private Handler handler = new Handler();

    public RxInfiniteRecyclerViewAdapter(ArrayList<Object> items, Listener listener) {
        this.originalItems = items;
        this.listener = listener;

        dataObserver = new DataObserver(this);
    }

    public Listener getListener() {
        return listener;
    }

    public boolean isProgressShown() {
        return isProgressShown;
    }

    public void showProgress(boolean show) {
        if (show && !isProgressShown) {
            isProgressShown = true;
            progress = new Progress();
            originalItems.add(progress);

            try { unregisterAdapterDataObserver(dataObserver); }
                catch (IllegalStateException ignored) {}
            notifyItemInserted(originalItems.indexOf(progress));
            if (dataObserver != null) { registerAdapterDataObserver(dataObserver); }
        } else if(!show && isProgressShown) {
            int pos = originalItems.indexOf(progress);
            originalItems.remove(progress);

            try { unregisterAdapterDataObserver(dataObserver); }
                catch (IllegalStateException ignored) {}
            notifyItemRemoved(pos);
            if (dataObserver != null) { registerAdapterDataObserver(dataObserver); }

            progress = null;
            isProgressShown = false;
            isProgressShown = false;
        }
    }

    public void addAll(ArrayList<Object> items){
        Observable.fromCallable(() -> {
            int startPos = originalItems.size() - (isProgressShown ? 1 : 0);
            int insertionNum = items.size();
            originalItems.addAll(startPos, items);
            return new Pair<>(startPos, insertionNum);
        })
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(pair -> {
            notifyItemRangeInserted(pair.first, pair.second);
        });
    }

    public int getOriginalItemCount() {
        return originalItems == null ? 0 : isProgressShown ? originalItems.size()-1 : originalItems.size();
    }

    public boolean isPlaceForMoreDataAvailable() {
        if (llm != null) {
            int first = llm.findFirstCompletelyVisibleItemPosition();
            int last = llm.findLastCompletelyVisibleItemPosition();

            if (first == 0 && ((!isProgressShown && last == getItemCount()-1) ||
                    (isProgressShown && last == getOriginalItemCount()))){
                return true;
            }
        }

        return false;
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (llm == null) {
            try{
                llm = (LinearLayoutManager) recyclerView.getLayoutManager();
            } catch (ClassCastException ignored) {
            }
        }

        scroll = RxRecyclerView.scrollEvents(recyclerView).subscribe(this::handleScroll);
        registerAdapterDataObserver(dataObserver);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        llm = null;

        scroll.unsubscribe();
        unregisterAdapterDataObserver(dataObserver);
    }

    private void handleScroll(RecyclerViewScrollEvent event) {
        if (llm == null) return;

        // Catching the case when number of
        // elements available for scrolling
        // is less then offset
        final int visibleItemsNum = llm.findLastVisibleItemPosition() - llm.findFirstVisibleItemPosition();
        if (getItemCount() - visibleItemsNum < offset)
            offset = getItemCount() - visibleItemsNum;

        if (llm.findLastCompletelyVisibleItemPosition() >= getOriginalItemCount()-offset && notify){
            notify = false;

            /*
             *  We need this handler due to the following problem:
             *  Cannot call this method in a scroll callback. Scroll callbacks
             *  might be run during a measure & layout pass where you cannot
             *  change the RecyclerView data. Any method call that might change
             *  the structure of the RecyclerView or the adapter contents should
             *  be postponed to the next frame.
             *
             *  Solution described here:
             *
             *  http://stackoverflow.com/questions/39445330/cannot-call-notifyiteminserted-method-in-a-scroll-callback-recyclerview-v724-2
             */
            event.view().post(new Runnable() {
                @Override
                public void run() {
                    if (getListener() != null) {
                        getListener().onNeedMoreData(RxInfiniteRecyclerViewAdapter.this);
                    }
                }
            });
        }

        if (llm.findLastCompletelyVisibleItemPosition() < getOriginalItemCount()-offset && !notify){
            notify = true;
        }
    }

    public interface Listener {
        void onNeedMoreData(RxInfiniteRecyclerViewAdapter adapter);

        void onPlaceForMoreDataAvailable(RxInfiniteRecyclerViewAdapter adapter);

        void onDataInserted(RxInfiniteRecyclerViewAdapter adapter);
    }

    public static class DataObserver extends RecyclerView.AdapterDataObserver {

        private RxInfiniteRecyclerViewAdapter adapter;

        public DataObserver(RxInfiniteRecyclerViewAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            if (adapter.getListener() != null) {

                // This dirty trick allows us to show
                // recyclerView animations in a right
                // order

                adapter.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.isPlaceForMoreDataAvailable()) {
                            adapter.getListener().onPlaceForMoreDataAvailable(adapter);
                        }

                        adapter.getListener().onDataInserted(adapter);
                    }
                }, 0);
            }
        }
    }
}
