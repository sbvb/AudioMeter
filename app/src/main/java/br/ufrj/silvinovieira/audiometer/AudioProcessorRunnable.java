package br.ufrj.silvinovieira.audiometer;

import android.util.Log;

import java.util.List;
import java.util.Queue;

import br.ufrj.silvinovieira.audiometer.viewdata.ChartViewData;
import br.ufrj.silvinovieira.audiometer.viewdata.CustomViewData;
import br.ufrj.silvinovieira.audiometer.viewdata.VolumeBarViewData;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

/**
 * Created by silvino on 30/03/16.
 * Project AudioMeter
 */
public class AudioProcessorRunnable implements Runnable {

    private final String TAG = "AudioProcessorRunnable";

    private static final int PROCESSING_ARRAY_SIZE = 2048;
    private static final int FFT_POINTS = 2048;

    private final MainActivity mParent;
    private final MainAdapter mAdapter;
    private final List<CustomViewData> mDataSet;
    private final Queue<Short> mAudioData;

    private boolean isProcessing;

    private static final int REFRESH_RATE = 50; //ms

    private int mLimit;

    public AudioProcessorRunnable(MainActivity mParent, MainAdapter mAdapter, List<CustomViewData> mDataSet, Queue<Short> mAudioData) {
        this.mParent = mParent;
        this.mAdapter = mAdapter;
        this.mDataSet = mDataSet;
        this.mAudioData = mAudioData;

        Log.i(TAG, "AudioProcessorRunnable created");
    }

    @Override
    public void run() {
        Log.i(TAG, "Processing audio");

        isProcessing = true;

        while (isProcessing) {
            try {
                Thread.sleep(REFRESH_RATE);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            final Short[] audioDataArray = toShortArray(mAudioData, PROCESSING_ARRAY_SIZE);

            final int rms = calculateRMS(audioDataArray);
            final double[] powerSpectralDensity = calculatePowerSpectralDensity(audioDataArray);

            mParent.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (CustomViewData data :
                            mDataSet) {
                        int viewType = data.getDataViewType();
                        if (viewType == CustomViewData.VOLUME_BAR_VIEW) {

                            VolumeBarViewData pData = (VolumeBarViewData) data;
                            pData.setVolume(rms);

                        } else if (viewType == CustomViewData.CHART_VIEW) {

                            ChartViewData lData = (ChartViewData) data;
                            if (lData.getTitle().equals(mParent.getString(R.string.oscilloscope_chart_label))) {
                                lData.resetSeries(audioDataArray, audioDataArray.length);
                                lData.setRangeInfo(" (" + lData.getChartPoints() * 1000 / AudioCaptureRunnable.SAMPLE_RATE + "ms)");

                            } else if (lData.getTitle().equals(mParent.getString(R.string.frequency_chart_label))) {
                                lData.resetSeries(powerSpectralDensity, powerSpectralDensity.length);
                                lData.setRangeInfo(" (" + AudioCaptureRunnable.SAMPLE_RATE / 2 + "Hz)");
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void stopProcessing() {
        isProcessing = false;
    }

    private Short[] toShortArray(Queue<Short> shortQueue, int size) {
        int length = (shortQueue.size() < size) ? shortQueue.size() : size;

        Short[] shortArray = new Short[length];
        shortArray = shortQueue.toArray(shortArray);

        return shortArray;
    }

    private double[] calculatePowerSpectralDensity(Short[] dataArray) {
        double[] fftData = new double[FFT_POINTS * 2];
        double[] magnitude = new double[FFT_POINTS / 2];

        DoubleFFT_1D fft = new DoubleFFT_1D(FFT_POINTS);

        if (dataArray.length >= FFT_POINTS) {
            for (int i = 0; i < FFT_POINTS; i++) {
                fftData[2 * i] = dataArray[i];
                fftData[2 * i + 1] = 0;
            }

            fft.complexForward(fftData);

            for (int i = 0; i < FFT_POINTS / 2; i++) {
                double re = fftData[2 * i];
                double im = fftData[2 * i + 1];
                magnitude[i] = Math.sqrt(re * re + im * im);
            }
        }

        return magnitude;
    }

    private int calculateRMS(Short[] dataArray) {
        int rms = 0;
        double sum = 0;

        if (dataArray.length > 0) {
            for (Short aDataArray : dataArray) {
                sum += aDataArray * aDataArray;
            }
            double meanSquare = sum / dataArray.length;
            rms = (int) Math.sqrt(meanSquare);
        }

        return rms;
    }
}