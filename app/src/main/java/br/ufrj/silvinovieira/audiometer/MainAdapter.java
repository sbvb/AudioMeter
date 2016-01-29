package br.ufrj.silvinovieira.audiometer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

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
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(getItemViewType(position) == CustomViewData.VOLUME_BAR_VIEW) {
            VolumeBarViewHolder volumeBarHolder = (VolumeBarViewHolder) holder;
            VolumeBarViewData volumeBarData = (VolumeBarViewData) mDataSet.get(position);

            volumeBarHolder.textView.setText(volumeBarData.getTitle());
            volumeBarHolder.progressBar.setMax(volumeBarData.getMaxVolume());
            volumeBarHolder.progressBar.setProgress(volumeBarData.getVolume());
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
}

