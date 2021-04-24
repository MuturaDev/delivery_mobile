package com.bg.deliveryapp.sectionedrecyclerview;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.bg.deliveryapp.MainActivity;
import com.bg.deliveryapp.R;
import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;

import java.util.List;

/**
 * Created by apple on 11/7/16.
 */

public class AdapterSectionRecycler extends SectionRecyclerViewAdapter<SectionHeader, Child, SectionViewHolder, ChildViewHolder> {

    Context context;

    public AdapterSectionRecycler(Context context, List<SectionHeader> sectionHeaderItemList) {
        super(context, sectionHeaderItemList);
        this.context = context;
    }

    @Override
    public SectionViewHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.section_item, sectionViewGroup, false);
        return new SectionViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, childViewGroup, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindSectionViewHolder(SectionViewHolder sectionViewHolder, int sectionPosition, SectionHeader sectionHeader) {
        sectionViewHolder.name.setText(sectionHeader.sectionText);

        if(sectionHeader.getImage_icon() == 0)
            sectionViewHolder.left_image.setVisibility(View.GONE);
        else
            sectionViewHolder.left_image.setBackgroundResource(sectionHeader.getImage_icon());

        final MainActivity activity = (MainActivity)context;

        if(clickedTitle.equals(sectionViewHolder.name.getText().toString()))
            showClicked(sectionViewHolder.name, true, activity);
        else
            showClicked(sectionViewHolder.name, false, activity);

        if(sectionViewHolder.name.getText().toString().equalsIgnoreCase("View Customers")){
            sectionViewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.displayFragment("View Customers",null);
                    whichIsClicked(sectionViewHolder.name, true,activity);
                }
            });
        }else if(sectionViewHolder.name.getText().toString().equalsIgnoreCase("View Orders")){
            sectionViewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.displayFragment("Pending Orders",null);
                    whichIsClicked(sectionViewHolder.name, true,activity);
                }
            });
        }

    }


    private static String clickedTitle = "";

    @Override
    public void onBindChildViewHolder(final ChildViewHolder childViewHolder, int sectionPosition, int childPosition, final Child child) {
//        final MainActivity activity = (MainActivity)context;
//        childViewHolder.name.setText(child.getName());

//        if(clickedTitle.equals(childViewHolder.name.getText().toString()))
//            showClicked(childViewHolder.name, true, activity);
//        else
//            showClicked(childViewHolder.name, false, activity);
//
//        childViewHolder.item_layout_ID.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activity.displayFragment(child.getName(),null);
//                whichIsClicked(childViewHolder.name, true,activity);
//            }
//        });
//
//        if(child.getImage_icon() == 0)
//            childViewHolder.left_image.setVisibility(View.GONE);
//        else
//            childViewHolder.left_image.setBackgroundResource(child.getImage_icon());
//
//        if(child.getName().equals("View Orders")){
//            childViewHolder.paid_unpaid_orders.setVisibility(View.VISIBLE);
//            childViewHolder.sub_child.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    activity.displayFragment(childViewHolder.sub_child.getText().toString(),null);
//
//                    whichIsClicked(childViewHolder.sub_child, false,activity);
//
//                    showClicked(childViewHolder.sub_child,true,activity);
//                    showClicked(childViewHolder.sub2_child,false,activity);
//                }
//            });
//            childViewHolder.sub2_child.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    activity.displayFragment(childViewHolder.sub2_child.getText().toString(),null);
//
//                    whichIsClicked(childViewHolder.sub2_child, false,activity);
//
//                    showClicked(childViewHolder.sub2_child,true,activity);
//                    showClicked(childViewHolder.sub_child,false,activity);
//                }
//            });
//        }else{
//            childViewHolder.paid_unpaid_orders.setVisibility(View.GONE);
//        }





    }

    private void whichIsClicked(TextView view, boolean show, MainActivity activity){
        if(show){
            clickedTitle = view.getText().toString();
        }else{
            clickedTitle = "";
        }
         activity.adapterRecycler.notifyDataSetChanged();
    }

    private void showClicked(TextView view, boolean show, MainActivity activity){
        if(show){
            view.setTypeface(view.getTypeface(), Typeface.BOLD);
            Typeface typeface = ResourcesCompat.getFont(context, R.font.cust_poppins_bold);
            view.setTypeface(typeface);
            //the default textview color
            view.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        }else{
            view.setTypeface(view.getTypeface(), Typeface.NORMAL);
            Typeface typeface = ResourcesCompat.getFont(context, R.font.cust_poppins_regular);
            view.setTypeface(typeface);
            //the default textview color
            view.setTextColor(activity.getResources().getColor(R.color.textColorSecondary));
        }


    }
}