package com.bg.deliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.MainActivity;
import com.bg.deliveryapp.R;
import com.bg.deliveryapp.api.models.responses.subResponses.PendingOrderContent;

import java.util.List;


public class ViewPendingOrdersAdapter extends RecyclerView.Adapter<ViewPendingOrdersAdapter.CustomViewHolder> {


    private List<PendingOrderContent> dataList;
    private Context context;
    private MainActivity activity;


    public ViewPendingOrdersAdapter(MainActivity activity,Context context ,List<PendingOrderContent> dataList){
        this.context = context;
        this.dataList = dataList;
        this.activity = activity;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        
        private TextView customer_name,txt_order_date,txt_amount_to_pay,txt_location,phoneNumber;
        private ImageView btn_order_details;
        private CardView pending_item_layout;
        
        CustomViewHolder(View itemView) {
            super(itemView);

            customer_name = itemView.findViewById(R.id.customer_name);
            txt_order_date = itemView.findViewById(R.id.txt_order_date);
            txt_amount_to_pay = itemView.findViewById(R.id.txt_amount_to_pay);
            txt_location = itemView.findViewById(R.id.txt_location);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            btn_order_details = itemView.findViewById(R.id.btn_order_details);
            pending_item_layout = itemView.findViewById(R.id.pending_item_layout);
        }

        public void bind(PendingOrderContent pendingItem){
            customer_name.setText(pendingItem.getCustomerName());
            txt_order_date.setText(pendingItem.getOrderDate());
            txt_amount_to_pay.setText(pendingItem.getTotalAmount());
            txt_location.setText(pendingItem.getLocation());
            phoneNumber.setText(pendingItem.getPhoneNo());
            btn_order_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.displayFragment("View Individual Orders", pendingItem);
                }
            });
            pending_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.displayFragment("View Individual Orders", pendingItem);
                }
            });
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_pending_order_item_layout, parent, false);
        return new CustomViewHolder(view);


    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.bind(dataList.get(position));

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
