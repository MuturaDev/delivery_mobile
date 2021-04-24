package com.bg.deliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.R;
import com.bg.deliveryapp.api.models.responses.OrderProductsDetailsResponse;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailsProductListAdapter extends RecyclerView.Adapter<OrderDetailsProductListAdapter.CustomViewHolder> {

    private List<OrderProductsDetailsResponse> dataList;
    private Context context;

    public OrderDetailsProductListAdapter(Context context ,List<OrderProductsDetailsResponse> dataList){
        this.context = context;
         this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_quantity, txt_item,txt_price;
        CustomViewHolder(View itemView) {
            super(itemView);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            txt_item = itemView.findViewById(R.id.txt_item);
            txt_price = itemView.findViewById(R.id.txt_price);
        }

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_individual_order_inner_sub_item_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

            holder.txt_item.setText(dataList.get(position).getProdDesc());
            holder.txt_quantity.setText(dataList.get(position).getQuantity() != null?dataList.get(position).getQuantity().toString(): "1");
            holder.txt_price.setText(new DecimalFormat("#,###.00").format(Integer.valueOf(String.valueOf(dataList.get(position).getPrice() != null?dataList.get(position).getPrice().toString(): "0"))) + "/=");

//        Picasso.Builder builder = new Picasso.Builder(context);
//        builder.downloader(new OkHttp3Downloader(context));
//        builder.build().load(dataList.get(position).getThumbnailUrl())
//                .placeholder((R.drawable.ic_launcher_background))
//                .error(R.drawable.ic_launcher_background)
//                .into(holder.coverImage);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}


