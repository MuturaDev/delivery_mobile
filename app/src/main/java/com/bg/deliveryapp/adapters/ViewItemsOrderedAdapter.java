package com.bg.deliveryapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class ViewItemsOrderedAdapter extends RecyclerView.Adapter<ViewItemsOrderedAdapter.CustomViewHolder> {

    //private List<RetroPhoto> dataList;
    private Context context;

    public ViewItemsOrderedAdapter(Context context /*,List<RetroPhoto> dataList*/){
        this.context = context;
        // this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {



        CustomViewHolder(View itemView) {
            super(itemView);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.custom_row, parent, false);
//        return new CustomViewHolder(view);

        return null;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {


//        Picasso.Builder builder = new Picasso.Builder(context);
//        builder.downloader(new OkHttp3Downloader(context));
//        builder.build().load(dataList.get(position).getThumbnailUrl())
//                .placeholder((R.drawable.ic_launcher_background))
//                .error(R.drawable.ic_launcher_background)
//                .into(holder.coverImage);

    }

    @Override
    public int getItemCount() {
        return 0;//dataList.size();
    }
}

