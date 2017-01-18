package com.daxh.explore.madtest02.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daxh.explore.madtest02.R;

public class ProgressViewHolder extends RecyclerView.ViewHolder{

    private ProgressBar pbLoading;
    private Progress progress;

    public ProgressViewHolder(View rootView) {
        super(rootView);

        pbLoading = (ProgressBar) rootView.findViewById(R.id.pbLoading);
    }

    public void bindItem(Progress progress) {
        this.progress = progress;
    }
}
