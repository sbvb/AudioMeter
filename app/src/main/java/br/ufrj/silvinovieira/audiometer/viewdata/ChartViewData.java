package br.ufrj.silvinovieira.audiometer.viewdata;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeriesFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by silvino on 30/03/16.
 * Project AudioMeter
 */
public class ChartViewData implements CustomViewData {

    private final String TAG = "ChartViewData";

    private final SimpleXYSeries series;
    private final XYSeriesFormatter formatter;
    private final int chartPoints;
    private int yMin;
    private int yMax;
    private String rangeInfo = "";

    public ChartViewData(String title, XYSeriesFormatter formatter, int chartPoints, int yMin, int yMax) {
        this.formatter = formatter;
        this.chartPoints = chartPoints;
        this.yMin = yMin;
        this.yMax = yMax;

        this.series = new SimpleXYSeries(title);
        this.series.useImplicitXVals();
    }

    public SimpleXYSeries getSeries() {
        return series;
    }

    public XYSeriesFormatter getFormatter() {
        return formatter;
    }

    public int getChartPoints() {
        return chartPoints;
    }

    public int getYMin() {
        return yMin;
    }

    public int getYMax() {
        return yMax;
    }

    public void setRangeInfo(String rangeInfo) {
        this.rangeInfo = rangeInfo;
    }

    @Override
    public String getTitle() {
        return series.getTitle();
    }

    public String getFullTitle() {
        return getTitle() + rangeInfo;
    }

    public void resetSeries(Short[] values, int length) {
        int max = (length > chartPoints) ? chartPoints : length;
        List<Number> model = new ArrayList<>();
        model.addAll(Arrays.asList(values).subList(0, max));
        series.setModel(model, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
    }

    public void resetSeries(double[] values, int length) {
        int max = (length > chartPoints) ? chartPoints : length;
        List<Number> model = new ArrayList<>();
        for (int i = 0; i < max; i++)
        {
            model.add(values[i]);
        }
        series.setModel(model, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
    }

    @Override
    public int getDataViewType() {
        return CHART_VIEW;
    }
}
