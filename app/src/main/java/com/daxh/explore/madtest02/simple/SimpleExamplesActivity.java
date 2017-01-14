package com.daxh.explore.madtest02.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.utils.BindingUtils;

public class SimpleExamplesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_examples);

        BindingUtils.bindButton(this, R.id.btSimpleRecyclerView, () -> new Intent(this, SimpleRecyclerViewActivity.class));
        BindingUtils.bindButton(this, R.id.btRecyclerViewWithHeaders, () -> new Intent(this, RecyclerViewWithHeadersActivity.class));
        BindingUtils.bindButton(this, R.id.btMutableRecyclerView, () -> new Intent(this, MutableRecyclerViewActivity.class));
    }
}