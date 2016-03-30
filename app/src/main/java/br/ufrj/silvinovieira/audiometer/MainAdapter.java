package br.ufrj.silvinovieira.audiometer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.XYPlot;

import java.util.List;

import br.ufrj.silvinovieira.audiometer.viewdata.ChartViewData;
import br.ufrj.silvinovieira.audiometer.viewdata.CustomViewData;
import br.ufrj.silvinovieira.audiometer.viewdata.VolumeBarViewData;

/**
 * Created by silvino on 29/01/16.
 * Project AudioMeter
 *
 * Adapter for MainActivity view
 *
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final String TAG = "MainAdapter";

    List<CustomViewData> mDataSet;

    public MainAdapter(List<CustomViewData> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        Context context = parent.getContext();
        View view;

        if( viewType == CustomViewData.VOLUME_BAR_VIEW) {
            view = LayoutInflater.from(context).inflate(R.layout.volume_bar_card, parent, false);
            holder = new VolumeBarViewHolder(view);
        } else if (viewType == CustomViewData.CHART_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_card, parent, false);
            holder = new ChartViewHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if(viewType == CustomViewData.VOLUME_BAR_VIEW) {
            VolumeBarViewHolder volumeBarHolder = (VolumeBarViewHolder) holder;
            VolumeBarViewData volumeBarData = (VolumeBarViewData) mDataSet.get(position);

            volumeBarHolder.textView.setText(volumeBarData.getTitle());
            volumeBarHolder.progressBar.setMax(volumeBarData.getMaxVolume());
            volumeBarHolder.progressBar.setProgress(volumeBarData.getVolume());

        } else if (viewType == CustomViewData.CHART_VIEW) {
            ChartViewHolder lineChartHolder = (ChartViewHolder) holder;
            ChartViewData lineChartData = (ChartViewData) mDataSet.get(position);
            XYPlot plot = lineChartHolder.xyPlot;

            plot.clear();

            plot.setDomainBoundaries(0, lineChartData.getChartPoints(), BoundaryMode.FIXED);
            plot.setRangeBoundaries(lineChartData.getYMin(),lineChartData.getYMax(), BoundaryMode.FIXED);
            plot.addSeries(lineChartData.getSeries(), lineChartData.getFormatter());
            plot.setTitle(lineChartData.getFullTitle());

            lineChartHolder.xyPlot.redraw();
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getDataViewType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class VolumeBarViewHolder extends ViewHolder {
        TextView textView;
        ProgressBar progressBar;

        public VolumeBarViewHolder(View v) {
            super(v);

            Log.d(TAG, "Volume bar view created");

            textView = (TextView) v.findViewById(R.id.volumeBarTitle);
            progressBar = (ProgressBar) v.findViewById(R.id.volumeProgressBar);
        }
    }

    public class ChartViewHolder extends ViewHolder {
        final XYPlot xyPlot;

        public ChartViewHolder(View v) {
            super(v);

            xyPlot = (XYPlot) v.findViewById(R.id.chartPlot);

            xyPlot.getGraphWidget().setDomainTickLabelWidth(0.0f);
            xyPlot.getGraphWidget().setDomainTickLabelPaint(null);
            xyPlot.getGraphWidget().setDomainOriginTickLabelPaint(null);
            xyPlot.getGraphWidget().setRangeTickLabelWidth(0.0f);
            xyPlot.getGraphWidget().setRangeTickLabelPaint(null);
            xyPlot.getGraphWidget().setRangeOriginTickLabelPaint(null);
            xyPlot.getLayoutManager().remove(xyPlot.getLegendWidget());
        }
    }
}

