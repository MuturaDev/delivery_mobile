package com.bg.deliveryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.R;


public class ViewPendingOrdersAdapter extends RecyclerView.Adapter<ViewPendingOrdersAdapter.CustomViewHolder> {

    //private List<RetroPhoto> dataList;
    private Context context;

    public ViewPendingOrdersAdapter(Context context /*,List<RetroPhoto> dataList*/){
        this.context = context;
        // this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView order_status;

        CustomViewHolder(View itemView) {
            super(itemView);
            order_status = itemView.findViewById(R.id.order_status);
        }

        public void bind(){
            order_status.setImageResource(R.drawable.green_circle);
            order_status.setVisibility(View.GONE);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_orders_activity_item_layout, parent, false);
        return new CustomViewHolder(view);


    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.bind();

//        Picasso.Builder builder = new Picasso.Builder(context);
//        builder.downloader(new OkHttp3Downloader(context));
//        builder.build().load(dataList.get(position).getThumbnailUrl())
//                .placeholder((R.drawable.ic_launcher_background))
//                .error(R.drawable.ic_launcher_background)
//                .into(holder.coverImage);

    }

    @Override
    public int getItemCount() {
        return 30;//dataList.size();
    }
}
