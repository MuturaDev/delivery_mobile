package com.bg.deliveryapp.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.MainActivity;
import com.bg.deliveryapp.R;
import com.bg.deliveryapp.api.models.responses.subResponses.ClientSubResponse;
import com.bg.deliveryapp.ui.ViewCustomerFragment;


import java.util.List;

public class ViewCustomersAdapter extends RecyclerView.Adapter<ViewCustomersAdapter.CustomViewHolder> {

    private List<ClientSubResponse> dataList;
    private ViewCustomerFragment context;
    private MainActivity activity;

    public ViewCustomersAdapter(MainActivity activity,ViewCustomerFragment context ,List<ClientSubResponse> dataList){
        this.context = context;
       this.dataList = dataList;
       this.activity = activity;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView label_name, label_phonenumber, label_location;
        private ImageView btn_edit;
        private CardView view_customer_activity_item_layout_id;

        CustomViewHolder(View itemView) {
            super(itemView);
            label_name = itemView.findViewById(R.id.label_name);
            label_phonenumber = itemView.findViewById(R.id.label_phonenumber);
            label_location = itemView.findViewById(R.id.label_location);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            view_customer_activity_item_layout_id = itemView.findViewById(R.id.view_customer_activity_item_layout_id);

        }
        
        
        public void bind(final ClientSubResponse response){
            label_name.setText(response.getFirstname() + " " + response.getLastname());
            label_phonenumber.setText(response.getPhoneNumber());
            label_location.setText(response.getLocation());

            try{
                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.displayFragment("Edit Customer",response);
                    }
                });
                view_customer_activity_item_layout_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if((context.message != null)){
                            if(context.message.equalsIgnoreCase("Select Customer")){
                                activity.displayFragment("Add Order",response);
                            }else{
                                btn_edit.performClick();
                            }
                        }else{
                            btn_edit.performClick();
                        }

                    }
                });
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_customer_activity_item_layout, parent, false);
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
