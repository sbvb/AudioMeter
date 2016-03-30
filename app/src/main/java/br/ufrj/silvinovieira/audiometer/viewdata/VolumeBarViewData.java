package br.ufrj.silvinovieira.audiometer.viewdata;

/**
 * Created by silvino on 29/01/16.
 * Project AudioMeter
 *
 */
public class VolumeBarViewData implements CustomViewData {

    private final String TAG = "VolumeBarViewData";

    private final String title;
    private int volume;
    private int maxVolume;

    public VolumeBarViewData(String title, int maxVolume) {
        this.title = title;
        this.maxVolume = maxVolume;
        this.volume = 0;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getDataViewType() {
        return VOLUME_BAR_VIEW;
    }
}
