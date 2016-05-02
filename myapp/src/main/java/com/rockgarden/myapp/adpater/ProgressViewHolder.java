package com.rockgarden.myapp.adpater;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rockgarden.myapp.R;
import com.rockgarden.myapp.widget.CircleProgressBar;

/**
 * Created by rockgarden on 16/4/30.
 */
public class ProgressViewHolder extends RecyclerView.ViewHolder {
    public CircleProgressBar progressBar;

    public ProgressViewHolder(View v) {
        super(v);
        progressBar = (CircleProgressBar) v.findViewById(R.id.progressBar1);
        progressBar.setColorSchemeColors(android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);
        progressBar.setProgress(100);
    }
}
