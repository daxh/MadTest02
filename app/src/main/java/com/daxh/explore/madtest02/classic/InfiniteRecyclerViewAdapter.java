package com.daxh.explore.madtest02.classic;

import android.os.Handler;
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

import java.util.ArrayList;

public class InfiniteRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PROGRESS  = 0x0001;
    private static final int TYPE_ITEM      = 0x0002;

    private ArrayList<Object> originalItems;

    private ArrayList<Object> attachedItems;
    private ItemFilter itemFilter;

    private boolean isProgressShown = false;
    private Progress progress;

    private Listener listener;
    private OnScrollListener onScrollListener;
    private DataObserver dataObserver;

    private LinearLayoutManager llm;

    private Handler handler = new Handler();

    public InfiniteRecyclerViewAdapter(ArrayList<Object> items, Listener listener) {
        this.originalItems = items;
        this.listener = listener;

        itemFilter = new ItemFilter();
        onScrollListener = new OnScrollListener();
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
            unregisterAdapterDataObserver(dataObserver);
                notifyItemInserted(originalItems.indexOf(progress));
            registerAdapterDataObserver(dataObserver);
        } else if(!show && isProgressShown) {

            // This dirty trick allows us to show
            // recyclerView animations in a right
            // order: one after one, items inserted
            // animation (when adding new items),
            // item removed animation (when hiding
            // progress item)

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int pos = originalItems.indexOf(progress);
                    originalItems.remove(progress);
                    unregisterAdapterDataObserver(dataObserver);
                        notifyItemRemoved(pos);
                    registerAdapterDataObserver(dataObserver);
                    progress = null;
                    isProgressShown = false;
                }
            }, 0);
        }
    }

    public void addAll(ArrayList<Object> items){
        attachedItems = items;
        itemFilter.filter("");
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
        recyclerView.addOnScrollListener(onScrollListener);
        registerAdapterDataObserver(dataObserver);

        if (llm == null) {
            try{
                llm = (LinearLayoutManager) recyclerView.getLayoutManager();
            } catch (ClassCastException ignored) {
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(onScrollListener);
        unregisterAdapterDataObserver(dataObserver);

        llm = null;
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
        private int insertionsNum;

        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            FilterResults results = new FilterResults();
            final ArrayList<Object> tmpItemsList = new ArrayList<>(originalItems);

            startPos = originalItems.size()-(isProgressShown ? 1 : 0);
            insertionsNum = attachedItems.size();
            tmpItemsList.addAll(startPos, attachedItems);

            attachedItems.clear();
            attachedItems = null;

            results.values = tmpItemsList;
            results.count = tmpItemsList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            originalItems.clear();
            originalItems = (ArrayList<Object>) filterResults.values;
            notifyItemRangeInserted(startPos, insertionsNum);
        }
    }

    public interface Listener {
        void onNeedMoreData(InfiniteRecyclerViewAdapter adapter);

        void onPlaceForMoreDataAvailable(InfiniteRecyclerViewAdapter adapter);

        void onDataInserted(InfiniteRecyclerViewAdapter adapter);
    }

    public static class DataObserver extends RecyclerView.AdapterDataObserver {

        private InfiniteRecyclerViewAdapter adapter;

        public DataObserver(InfiniteRecyclerViewAdapter adapter) {
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

    public static class OnScrollListener extends RecyclerView.OnScrollListener {

        private int offset = 3;
        private LinearLayoutManager llm;
        private InfiniteRecyclerViewAdapter adapter;

        private boolean notify = true;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (adapter == null || llm == null) {
                try{
                    adapter = (InfiniteRecyclerViewAdapter) recyclerView.getAdapter();
                    llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                } catch (ClassCastException e) {
                    return;
                }
            }

            if (llm.findLastCompletelyVisibleItemPosition() >= adapter.getOriginalItemCount()-offset && notify){
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
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getListener() != null) {
                            adapter.getListener().onNeedMoreData(adapter);
                        }
                    }
                });
            }

            if (llm.findLastCompletelyVisibleItemPosition() < adapter.getOriginalItemCount()-offset && !notify){
                notify = true;
            }
        }
    }
}
