package com.daxh.explore.madtest02.classic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.utils.BindingUtils;

public class ClassicExamplesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examples);

        BindingUtils.bindButton(this, R.id.btFiltering, () -> new Intent(this, FilteredRecyclerViewActivity.class));
        BindingUtils.bindButton(this, R.id.btSorting, () -> new Intent(this, SortedRecyclerViewActivity.class));
        BindingUtils.bindButton(this, R.id.btInfinite, () -> new Intent(this, InfiniteRecyclerViewActivity.class));
        BindingUtils.bindButton(this, R.id.btGeneric, () -> new Intent(this, GenericRecyclerViewActivity.class));
    }
}
