package br.ufrj.silvinovieira.audiometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.ufrj.silvinovieira.audiometer.viewdata.CustomViewData;
import br.ufrj.silvinovieira.audiometer.viewdata.VolumeBarViewData;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    List<CustomViewData> mViewDataSet;
    RecyclerView mRecyclerView;
    MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        mRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mViewDataSet = new ArrayList<>();
        mViewDataSet.add(new VolumeBarViewData(getString(R.string.volume_bar_title), 50, 100));

        mAdapter = new MainAdapter(mViewDataSet);
        mRecyclerView.setAdapter(mAdapter);
    }
}
