package com.daxh.explore.madtest02.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daxh.explore.madtest02.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView tvText;
    private Item item;

    public ItemViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        tvText = (TextView) itemView.findViewById(R.id.tvText);
    }

    public void bindItem(Item item) {
        this.item = item;

        tvText.setText(this.item.getText());
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), item.getText() + " clicked!", Toast.LENGTH_SHORT).show();
    }
}
