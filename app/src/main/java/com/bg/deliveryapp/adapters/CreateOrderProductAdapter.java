package com.bg.deliveryapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.MainActivity;
import com.bg.deliveryapp.R;
import com.bg.deliveryapp.api.ApiService;
import com.bg.deliveryapp.api.ServiceGenerator;
import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;
import com.bg.deliveryapp.api.models.responses.ComputeTotalResponse;
import com.bg.deliveryapp.api.models.responses.CreateOrderCategoryResponse;
import com.bg.deliveryapp.api.models.responses.CreateOrderDeliveryResponse;
import com.bg.deliveryapp.api.models.responses.CreateOrderProductsResponse;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.R;
import com.bg.deliveryapp.api.models.responses.CreateOrderProductsResponse;
import com.bg.deliveryapp.ui.CreateOrderFragment;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrderProductAdapter extends RecyclerView.Adapter<CreateOrderProductAdapter.CustomViewHolder> {


    private CreateOrderFragment fragment;

    public CreateOrderProductAdapter(CreateOrderFragment fragment){
        this.fragment = fragment;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        private EditText txt_item_name;
        private EditText txt_quantity;
        private EditText txt_price;
        private ProgressBar progress_circular;

        CustomViewHolder(View itemView) {
            super(itemView);
            txt_item_name = itemView.findViewById(R.id.txt_item_name);
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            txt_price = itemView.findViewById(R.id.txt_price);
            progress_circular = itemView.findViewById(R.id.progress_circular);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.create_order_product_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder,final int position) {

        holder.txt_item_name.setText(fragment.dataProductsList.get(position).getDesc());
        holder.txt_quantity.setText("1");
        fragment.dataProductsList.get(position).setQuantity(String.valueOf(1));
        computeSum(Long.valueOf(holder.txt_quantity.getText().toString()), holder,fragment.dataProductsList.get(position).getId(), position);

        holder.txt_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s.toString())) {
                    fragment.dataProductsList.get(position).setQuantity(String.valueOf(s.toString()));
                    computeSum(Long.valueOf(s.toString()), holder, fragment.dataProductsList.get(position).getId(), position);
                }else{
                    fragment.dataProductsList.get(position).setQuantity(String.valueOf(1));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return fragment.dataProductsList.size();
    }

    private void computeSum(final Long quantity, CustomViewHolder holder, String productID, int position){

        int deliveryId = 0;
        int categoryId = 0;



        for(CreateOrderDeliveryResponse item : fragment.dataDeliveryList){
            if(item.getSelected().equalsIgnoreCase("Y")){
                deliveryId = item.getId();
                break;
            }
        }

        for(CreateOrderCategoryResponse item : fragment.dataCategoryList){
            if(item.getSelected().equalsIgnoreCase("Y")){
                categoryId = item.getId();
                break;
            }
        }

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if(((MainActivity)fragment.getActivity()) != null){
            holder.progress_circular.setVisibility(View.VISIBLE);

            Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
            if(object!= null)
                if(((List<AuthenticationResponse>) object).size() > 0)
                    authenticationResponse = ((List<AuthenticationResponse>) object).get(0);

            if(authenticationResponse != null){
                ApiService apiService =
                        ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

                Call<ComputeTotalResponse> call = apiService.computeTotalWith(productID,String.valueOf(categoryId), String.valueOf(deliveryId));
                call.enqueue(new Callback<ComputeTotalResponse>() {
                    @Override
                    public void onResponse(Call<ComputeTotalResponse> call, Response<ComputeTotalResponse> response) {

                        if(response.isSuccessful()){
                            if(response.code() == 200){
                                if(response.body().getPrice() != null) {
                                    Long total = Long.valueOf(response.body().getPrice()) * quantity;
                                    holder.txt_price.setText(new DecimalFormat("#,###.00").format(Integer.valueOf(String.valueOf(total))) + "/=");
                                    fragment.dataProductsList.get(position).setQuantity(String.valueOf(quantity));
                                    fragment.dataProductsList.get(position).setTotalPrice(String.valueOf(String.valueOf(total)));
                                    hideKeyboard((MainActivity)fragment.getActivity());
                                }else{
                                    holder.txt_price.setText(String.valueOf(0));
                                }
                            }
                        }

                        holder.progress_circular.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ComputeTotalResponse> call, Throwable t) {
                        holder.progress_circular.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}


