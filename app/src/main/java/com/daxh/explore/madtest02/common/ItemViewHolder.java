package com.daxh.explore.madtest02.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.daxh.explore.madtest02.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    private TextView tvText;
    private Item item;

    public ItemViewHolder(View itemView) {
        super(itemView);

        tvText = (TextView) itemView.findViewById(R.id.tvText);
    }

    public void bindItem(Item item) {
        this.item = item;

        tvText.setText(this.item.getText());
    }
}
