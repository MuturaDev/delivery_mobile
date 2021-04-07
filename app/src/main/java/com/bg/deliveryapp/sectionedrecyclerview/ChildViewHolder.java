package com.bg.deliveryapp.sectionedrecyclerview;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.R;

/**
 * Created by apple on 11/7/16.
 */

public class ChildViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    ImageView left_image;
    CardView item_layout_ID;
    LinearLayout paid_unpaid_orders;
    TextView sub_child,sub2_child;

    public ChildViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.child);
        left_image = (ImageView) itemView.findViewById(R.id.left_image);
        item_layout_ID = (CardView) itemView.findViewById(R.id.item_layout_ID);
        paid_unpaid_orders = (LinearLayout) itemView.findViewById(R.id.paid_unpaid_orders);
        sub_child = (TextView) itemView.findViewById(R.id.sub_child);
        sub2_child = (TextView) itemView.findViewById(R.id.sub2_child);
    }
}