package br.ufrj.silvinovieira.audiometer;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.util.Queue;

/**
 * Created by silvino on 30/03/16.
 * Project AudioMeter
 */
public class AudioCaptureRunnable implements Runnable {

    private final String TAG = "AudioCaptureRunnable";

    private final Queue<Short> mAudioData;
    private static final int MAX_AUDIO_DATA = 4096;

    private AudioRecord mRecorder;
    private boolean isRecording = false;

    private static final int DEFAULT_BUFFER_INCREASE_FACTOR = 3;
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public static final int SAMPLE_RATE = 44100; //Hz

    public AudioCaptureRunnable(Queue<Short> audioData) {
        this.mAudioData = audioData;
    }

    @Override
    public void run() {
        Log.i(TAG, "Capturing audio");

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        int mBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL, ENCODING) * DEFAULT_BUFFER_INCREASE_FACTOR;
        mRecorder = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE, CHANNEL, ENCODING, mBufferSize);

        if (mRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
            mRecorder.startRecording();
            isRecording = true;
        }

        while (isRecording) {
            final short[] buffer = new short[mBufferSize];
            final int readSize = mRecorder.read(buffer, 0, buffer.length);

            for (int i=0; i<readSize; i++) {
                mAudioData.offer(buffer[i]);
            }
            for (int i=0; i<(mAudioData.size()-MAX_AUDIO_DATA); i++) {
                mAudioData.poll();
            }
        }
    }

    public void stopCapturing() {
        mRecorder.release();
        isRecording = false;

        Log.i(TAG, "Stopped capturing");
    }
}

