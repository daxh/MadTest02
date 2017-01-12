package com.daxh.explore.madtest02.advanced;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.utils.BindingUtils;

public class RxExamplesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examples);

        BindingUtils.bindButton(this, R.id.btFiltering, () -> new Intent(this, RxFilteredRecyclerViewActivity.class));
    }
}
