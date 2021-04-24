package com.bg.deliveryapp.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.MainActivity;
import com.bg.deliveryapp.R;
import com.bg.deliveryapp.adapters.ViewUnpaidOrdersAdapter;
import com.bg.deliveryapp.api.ApiService;
import com.bg.deliveryapp.api.ServiceGenerator;
import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;
import com.bg.deliveryapp.api.models.responses.PendingOrdersResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.PendingOrderContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnpaidOrdersFragment extends Fragment {

    private static final String EXTRA_TEXT = "text";

    private CardView btn_add_Order;
    private RecyclerView recyclerView;


    private ViewUnpaidOrdersAdapter adapter;


    private MainActivity activity;

    private AuthenticationResponse authenticationResponse = new AuthenticationResponse();

    public static UnpaidOrdersFragment createFor(Object message) {
        UnpaidOrdersFragment fragment = new UnpaidOrdersFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TEXT, (Serializable) message);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_unpaid_orders_layout, container, false);
    }


    //TODO: ADD LOAD MORE LATER AND SHIMMER
    //https://www.journaldev.com/24041/android-recyclerview-load-more-endless-scrolling
    //https://howtodoandroid.medium.com/shimmer-effect-for-android-recyclerview-example-a9315b46cdc0
    private void populateRecyclerView() {
        if (((MainActivity) getActivity()) != null) {
            progress_circular.setVisibility(View.VISIBLE);
            activity.no_content_after_filter.setVisibility(View.GONE);

            Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
            if (object != null)
                if (((List<AuthenticationResponse>) object).size() > 0)
                    authenticationResponse = ((List<AuthenticationResponse>) object).get(0);

            if (authenticationResponse != null) {
                ApiService apiService =
                        ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

                String filterText = ((MainActivity)getActivity()).et_search.getText().toString().trim();

                Call<PendingOrdersResponse> call = apiService.getPendingPaymentOrders(filterText, 1, 10);
                call.enqueue(new Callback<PendingOrdersResponse>() {
                    @Override
                    public void onResponse(Call<PendingOrdersResponse> call, Response<PendingOrdersResponse> response) {

                        if (response.isSuccessful()) {
                            if (response.code() == 200) {

                                List<PendingOrderContent> dataList = new ArrayList<>();
                                for (int i = 0; i < response.body().getContent().size(); i++) {
                                    PendingOrderContent dataItem = response.body().getContent().get(i);
                                    if (dataItem.getOrderStatus().equalsIgnoreCase("Pending")) {
                                        //filter

                                       // if(TextUtils.isEmpty(filterText)){
                                            dataList.add(dataItem);
//                                        }else{
//                                            if(dataItem.getCustomerName().toLowerCase().contains(filterText.toLowerCase())
//                                                    || dataItem.getPhoneNo().toLowerCase().contains(filterText.toLowerCase())){
//                                                dataList.add(dataItem);
//                                            }
//                                        }
                                    }
                                }

                                adapter = new ViewUnpaidOrdersAdapter(activity, getActivity(), dataList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                if(getActivity() != null)
                                  ((MainActivity) getActivity()).showNoContentAfterFilter(dataList.size(), activity);
                            }
                        }

                        progress_circular.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<PendingOrdersResponse> call, Throwable t) {
                        progress_circular.setVisibility(View.GONE);
                    }
                });
            }
        }

    }


    private ProgressBar progress_circular;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        final String text = args != null ? args.getString(EXTRA_TEXT) : "";


        activity = (MainActivity) getActivity();


        recyclerView = view.findViewById(R.id.recyclerView);
        btn_add_Order = view.findViewById(R.id.btn_add_Order);
        progress_circular = view.findViewById(R.id.progress_circular);


        populateRecyclerView();

        btn_add_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.displayFragment("Add Order", null);
            }
        });


        ((MainActivity) getActivity()).et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                populateRecyclerView();
            }
        });


    }

    @Override
    public void onStop() {
        activity.showNoContentAfterFilter(-1, activity);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        ((MainActivity)getActivity()).showNoContentAfterFilter(-1, activity);
        super.onDestroy();
    }
}





