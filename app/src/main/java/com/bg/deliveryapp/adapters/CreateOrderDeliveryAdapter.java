package com.bg.deliveryapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.R;
import com.bg.deliveryapp.api.models.responses.CreateOrderDeliveryResponse;

import java.util.List;

import android.widget.ImageView;
import android.widget.TextView;

import com.bg.deliveryapp.ui.CreateOrderFragment;

public class CreateOrderDeliveryAdapter extends RecyclerView.Adapter<CreateOrderDeliveryAdapter.CustomViewHolder> {

    private List<CreateOrderDeliveryResponse> dataList;
    private CreateOrderFragment fragment;

    public CreateOrderDeliveryAdapter(CreateOrderFragment fragment){
        this.fragment = fragment;

    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView delivery_status;
        private TextView delivery_name;

        CustomViewHolder(View itemView) {
            super(itemView);
            delivery_name = itemView.findViewById(R.id.delivery_name);
            delivery_status = itemView.findViewById(R.id.delivery_status);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.create_order_delivery_layout, parent, false);
        return new CustomViewHolder(view);

    }

    private void click(int position){
        fragment.dataDeliveryList.get(position).setSelected("Y");
        for(int i =0; i<fragment.dataDeliveryList.size(); i++){
            if(i == position){
            }else{
                fragment.dataDeliveryList.get(i).setSelected("N");
            }
        }

        fragment.deliveryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.delivery_name.setText(fragment.dataDeliveryList.get(position).getDesc());
        if(fragment.dataDeliveryList.get(position).getSelected().equalsIgnoreCase("Y"))
            holder.delivery_status.setImageResource(R.drawable.blue_circle);
        else
            holder.delivery_status.setImageResource(R.drawable.white_circle);

        holder.delivery_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(position);
            }
        });

        holder.delivery_name.setOnClickListener(new View.OnClickListener() {
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
        return fragment.dataDeliveryList.size();
    }
}



