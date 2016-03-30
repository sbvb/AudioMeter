package br.ufrj.silvinovieira.audiometer.viewdata;

/**
 * Created by silvino on 29/01/16.
 * Project AudioMeter
 */
public interface CustomViewData {

    int VOLUME_BAR_VIEW = 0;
    int CHART_VIEW = 1;

    int getDataViewType();
    String getTitle();
}
