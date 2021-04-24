package com.bg.deliveryapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.TextView;

import com.bg.deliveryapp.R;
import com.bg.deliveryapp.ui.CreateOrderFragment;

public class CreateOrderCategoryAdapter extends RecyclerView.Adapter<CreateOrderCategoryAdapter.CustomViewHolder> {


    private CreateOrderFragment fragment;

    public CreateOrderCategoryAdapter(CreateOrderFragment fragment){
        this.fragment = fragment;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView category_status;
        private TextView category_name;

        CustomViewHolder(View itemView) {
            super(itemView);
            category_status = itemView.findViewById(R.id.category_status);
            category_name = itemView.findViewById(R.id.category_name);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.create_order_category_layout, parent, false);
        return new CustomViewHolder(view);

    }

    private void click(int position){
        fragment.dataCategoryList.get(position).setSelected("Y");
        for(int i =0; i<fragment.dataCategoryList.size(); i++){
            if(i == position){
            }else{
                fragment.dataCategoryList.get(i).setSelected("N");
            }
        }

        fragment.categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.category_name.setText(fragment.dataCategoryList.get(position).getDesc());
        if(fragment.dataCategoryList.get(position).getSelected().equalsIgnoreCase("Y"))
            holder.category_status.setImageResource(R.drawable.blue_circle);
        else if(fragment.dataCategoryList.get(position).getSelected().equalsIgnoreCase("N"))
            holder.category_status.setImageResource(R.drawable.white_circle);

        holder.category_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(position);
            }
        });

        holder.category_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(position);
            }
        });

//        Picasso.Builder builder = new Picasso.Builder(context);
//        builder.downloader(new OkHttp3Downloader(context));
//        builder.build().load(dataList.get(position).getThumbnailUrl())
//                .placeholder((R.drawable.ic_launcher_background))
//                .error(R.drawable.ic_launcher_background)
//                .into(holder.coverImage);

    }

    @Override
    public int getItemCount() {
        return fragment.dataCategoryList.size();
    }
}


