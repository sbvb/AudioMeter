package br.ufrj.silvinovieira.audiometer;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.util.List;

import br.ufrj.silvinovieira.audiometer.viewdata.CustomViewData;
import br.ufrj.silvinovieira.audiometer.viewdata.VolumeBarViewData;

/**
 * Created by silvino on 29/01/16.
 * Project AudioMeter
 */
public class AudioRunnable implements Runnable {

    private final String TAG = "AudioRunnable";

    private List<CustomViewData> mDataSet;
    private Activity mParent;
    private MainAdapter mAdapter;

    AudioRecord mRecorder;
    private boolean isRecording = false;

    private static final int DEFAULT_BUFFER_INCREASE_FACTOR = 3;
    private static final int mSampleRate = 44100; //Hz
    private static final int mChannel = AudioFormat.CHANNEL_IN_MONO;
    private static final int mEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private static final long BUFFER_READ_PERIOD = 20; //ms

    public AudioRunnable(Activity parent, MainAdapter adapter, List<CustomViewData> dataSet) {
        this.mParent = parent;
        this.mAdapter = adapter;
        this.mDataSet = dataSet;
    }

    @Override
    public void run() {
        int mBufferSize = AudioRecord.getMinBufferSize(mSampleRate, mChannel, mEncoding) * DEFAULT_BUFFER_INCREASE_FACTOR;
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, mSampleRate, mChannel, mEncoding, mBufferSize);

        if (mRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
            mRecorder.startRecording();
            isRecording = true;
        }

        while (isRecording) {
            try {
                Thread.sleep(BUFFER_READ_PERIOD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final short[] buffer = new short[mBufferSize];
            final int readSize = mRecorder.read(buffer, 0, buffer.length);

            final int finalRms = getRms(buffer, readSize);

            mParent.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (CustomViewData data :
                            mDataSet) {
                        int viewType = data.getDataViewType();
                        if (viewType == CustomViewData.VOLUME_BAR_VIEW) {
                            VolumeBarViewData volumeData = (VolumeBarViewData) data;
                            volumeData.setVolume(finalRms);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void stopRecording() {
        mRecorder.release();
        isRecording = false;
    }

    private int getRms(short[] buffer, int readSize) {
        double sum = 0;
        int rms = 0;
        for (int i = 0; i < readSize; i++) {
            sum += buffer[i] * buffer[i];
        }
        if (readSize > 0) {
            double mean = sum / readSize;
            rms = (int) Math.sqrt(mean);
        }
        return rms;
    }
}
