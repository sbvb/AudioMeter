package br.ufrj.silvinovieira.audiometer.viewdata;

/**
 * Created by silvino on 29/01/16.
 * Project AudioMeter
 *
 */
public class VolumeBarViewData implements CustomViewData {

    private final String TAG = "VolumeBarViewData";

    String mTitle;
    int mVolume;
    int mMaxVolume;

    public VolumeBarViewData(String title, int maxVolume) {
        this.mTitle = title;
        this.mMaxVolume = maxVolume;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getVolume() {
        return mVolume;
    }

    public void setVolume(int volume) {
        this.mVolume = volume;
    }

    public int getMaxVolume() {
        return mMaxVolume;
    }

    @Override
    public int getDataViewType() {
        return VOLUME_BAR_VIEW;
    }
}
