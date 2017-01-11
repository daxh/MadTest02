package com.daxh.explore.madtest02.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daxh.explore.madtest02.R;
import com.daxh.explore.madtest02.classic.ClassicRecyclerViewUsageExampleActivity;
import com.daxh.explore.madtest02.utils.BindingUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BindingUtils.bindButton(this, R.id.btClassicRecycleView, () -> new Intent(this, ClassicRecyclerViewUsageExampleActivity.class));
    }
}
