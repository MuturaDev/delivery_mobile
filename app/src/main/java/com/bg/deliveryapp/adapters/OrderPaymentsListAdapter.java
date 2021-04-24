package com.bg.deliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.R;
import com.bg.deliveryapp.api.models.responses.OrderPaymentsListResponse;
import com.bg.deliveryapp.api.models.responses.OrderPaymentsListResponse;
import com.bg.deliveryapp.ui.ViewIndividualOrdersFragment;

import java.text.DecimalFormat;
import java.util.List;


public class OrderPaymentsListAdapter extends RecyclerView.Adapter<OrderPaymentsListAdapter.CustomViewHolder> {

    private List<OrderPaymentsListResponse> dataList;
    private ViewIndividualOrdersFragment fragment;

    public OrderPaymentsListAdapter(ViewIndividualOrdersFragment fragment ,List<OrderPaymentsListResponse> dataList){
        this.fragment = fragment;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_date, txt_amount,txt_method,txt_ref;
        private ImageView btn_edit;
        CustomViewHolder(View itemView) {
            super(itemView);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_method = itemView.findViewById(R.id.txt_method);
            txt_ref = itemView.findViewById(R.id.txt_ref);
            btn_edit = itemView.findViewById(R.id.btn_edit);
        }

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_individual_order_inner_sub_payment_item_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.txt_date.setText(dataList.get(position).getDelDate() != null ?dataList.get(position).getDelDate(): "" );
        holder.txt_method.setText(dataList.get(position).getPaymentMode() != null?dataList.get(position).getPaymentMode().toUpperCase(): "");
        holder.txt_amount.setText(new DecimalFormat("#,###.00").format(Double.valueOf(String.valueOf(dataList.get(position).getAmountPaid() != null?dataList.get(position).getAmountPaid().toString(): "0"))) + "/=");
        holder.txt_ref.setText(dataList.get(position).getReference()!=null?dataList.get(position).getReference():"");
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.showPaymentDialog(dataList.get(position).getPaymentMode(), String.valueOf(dataList.get(position).getAmountPaid()).replace(".00", "").replace(".0", ""), dataList.get(position).getReference(), dataList.get(position).getDelDate(), String.valueOf(dataList.get(position).getId()));
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
        return dataList.size();
    }
}


