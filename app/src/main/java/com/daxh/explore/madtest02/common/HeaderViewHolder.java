package com.daxh.explore.madtest02.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daxh.explore.madtest02.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView tvText;
    private Header header;

    public HeaderViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        tvText = (TextView) itemView.findViewById(R.id.tvText);
    }

    public void bindItem(Header item) {
        this.header = item;

        tvText.setText(this.header.getText());
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), header.getText() + " clicked!", Toast.LENGTH_SHORT).show();
    }
}
