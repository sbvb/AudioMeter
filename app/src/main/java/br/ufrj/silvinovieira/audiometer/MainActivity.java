package br.ufrj.silvinovieira.audiometer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.XYSeriesFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import br.ufrj.silvinovieira.audiometer.viewdata.ChartViewData;
import br.ufrj.silvinovieira.audiometer.viewdata.CustomViewData;
import br.ufrj.silvinovieira.audiometer.viewdata.VolumeBarViewData;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private Queue<Short> mAudioData;

    private Thread mCaptureThread;
    private Thread mProcessorThread;
    private AudioCaptureRunnable mCaptureRunnable;
    private AudioProcessorRunnable mProcessorRunnable;

    List<CustomViewData> mViewDataSet;
    MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mViewDataSet = new ArrayList<>();

        PixelUtils.init(this);

        mViewDataSet.add(new VolumeBarViewData(getString(R.string.volume_bar_title), Short.MAX_VALUE / 2));

        XYSeriesFormatter formatter = new LineAndPointFormatter(Color.CYAN, null, null, null);
        mViewDataSet.add(new ChartViewData(getString(R.string.oscilloscope_chart_label), formatter, 1600, -30000, 30000));

        formatter = new BarFormatter(Color.RED, Color.RED);
        mViewDataSet.add(new ChartViewData(getString(R.string.frequency_chart_label), formatter, 1024, 0, 20000000));

        mAudioData = new LinkedBlockingQueue<>();

        mAdapter = new MainAdapter(mViewDataSet);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");

        startProcesses();
    }

    private void startProcesses() {
        mCaptureRunnable = new AudioCaptureRunnable(mAudioData);
        mCaptureThread = new Thread(mCaptureRunnable);
        mCaptureThread.start();

        mProcessorRunnable = new AudioProcessorRunnable(this, mAdapter, mViewDataSet, mAudioData);
        mProcessorThread = new Thread(mProcessorRunnable);
        mProcessorThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");

        stopProcesses();
    }

    private void stopProcesses() {
        mCaptureRunnable.stopCapturing();
        mCaptureThread.interrupt();
        mCaptureThread = null;

        mProcessorRunnable.stopProcessing();
        mProcessorThread.interrupt();
        mProcessorThread = null;
    }
}
