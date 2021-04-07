package com.bg.deliveryapp.sectionedrecyclerview;



import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.R;

/**
 * Created by shanky on 11/12/2016.
 */

public class SectionViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    ImageView left_image;
    public SectionViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.sectionHeader);
        left_image = (ImageView) itemView.findViewById(R.id.left_image);
    }
}
