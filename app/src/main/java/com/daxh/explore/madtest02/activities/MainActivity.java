package com.daxh.explore.madtest02.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.advanced.RxExamplesActivity;
import com.daxh.explore.madtest02.classic.ClassicExamplesActivity;
import com.daxh.explore.madtest02.simple.SimpleRecyclerViewActivity;
import com.daxh.explore.madtest02.utils.BindingUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BindingUtils.bindButton(this, R.id.btSimpleRecycleView, () -> new Intent(this, SimpleRecyclerViewActivity.class));
        BindingUtils.bindButton(this, R.id.btClassicRecycleView, () -> new Intent(this, ClassicExamplesActivity.class));
        BindingUtils.bindButton(this, R.id.btRxRecycleView, () -> new Intent(this, RxExamplesActivity.class));
    }
}