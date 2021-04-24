package com.bg.deliveryapp.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.MainActivity;
import com.bg.deliveryapp.R;
import com.bg.deliveryapp.adapters.ViewCustomersAdapter;
import com.bg.deliveryapp.api.ApiService;
import com.bg.deliveryapp.api.ServiceGenerator;
import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;
import com.bg.deliveryapp.api.models.responses.ViewCustomerResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.ClientSubResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.PendingOrderContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCustomerFragment extends Fragment {

    private Button btn_add_customer;
    private RecyclerView recyclerView;



    private ViewCustomersAdapter adapter;


    private MainActivity activity;

    private AuthenticationResponse authenticationResponse = new AuthenticationResponse();

    //TODO: ADD LOAD MORE LATER AND SHIMMER
    //https://www.journaldev.com/24041/android-recyclerview-load-more-endless-scrolling
    //https://howtodoandroid.medium.com/shimmer-effect-for-android-recyclerview-example-a9315b46cdc0
    private void populateRecyclerView(){
        if(((MainActivity)getActivity()) != null){
            progress_circular.setVisibility(View.VISIBLE);
            activity.no_content_after_filter.setVisibility(View.GONE);

            Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
            if(object!= null)
                if(((List<AuthenticationResponse>) object).size() > 0)
                    authenticationResponse = ((List<AuthenticationResponse>) object).get(0);

            if(authenticationResponse != null){
                ApiService apiService =
                        ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

                String filterText = ((MainActivity)getActivity()).et_search.getText().toString().trim();

                Call<ViewCustomerResponse> call = apiService.getCustomers(filterText, 1,10);
                call.enqueue(new Callback<ViewCustomerResponse>() {
                    @Override
                    public void onResponse(Call<ViewCustomerResponse> call, Response<ViewCustomerResponse> response) {

                        if(response.isSuccessful()){
                            if(response.code() == 200){

                                List<ClientSubResponse> dataList = new ArrayList<>();
                                for(int i=0; i<response.body().getContent().size(); i++){
                                    ClientSubResponse dataItem = response.body().getContent().get(i);
                                        //filter

                                       // if(TextUtils.isEmpty(filterText)){
                                            dataList.add(dataItem);
//                                        }else{
//                                            if((dataItem.getFirstname() + " " + dataItem.getLastname()).toLowerCase().contains(filterText.toLowerCase())
//                                                    || dataItem.getPhoneNumber().toLowerCase().contains(filterText.toLowerCase())){
//                                                dataList.add(dataItem);
//                                            }
//                                        }
                                }

                                adapter = new ViewCustomersAdapter(activity,ViewCustomerFragment.this,dataList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                if(((MainActivity) getActivity()) != null)
                                ((MainActivity) getActivity()).showNoContentAfterFilter( dataList.size(), activity);
                            }
                        }

                        progress_circular.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ViewCustomerResponse> call, Throwable t) {
                        progress_circular.setVisibility(View.GONE);
                    }
                });
            }
        }

    }
    
    
    
    //conver to fragment
    private static final String EXTRA_TEXT = "text";


    public static ViewCustomerFragment createFor(Object message) {
        ViewCustomerFragment fragment = new ViewCustomerFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TEXT, (Serializable) message);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_customer_fragment_layout, container, false);
    }

    private ProgressBar progress_circular;

    public String message;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        message = args != null ? (String) args.getSerializable(EXTRA_TEXT) : null;



        LinearLayout search_layout_ID_select_customer = view.findViewById(R.id.search_layout_ID_select_customer);
        EditText et_search_select_customer = view.findViewById(R.id.et_search_select_customer);

        activity = (MainActivity) getActivity();


        recyclerView = view.findViewById(R.id.recyclerView);
        btn_add_customer = view.findViewById(R.id.btn_add_customer);
        progress_circular = view.findViewById(R.id.progress_circular);


        populateRecyclerView();

        btn_add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.displayFragment("Add Customer", null);
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


        final Object message = args != null ? args.getSerializable(EXTRA_TEXT) : "";
        if(message instanceof  String){
            if(message.equals("Select Customer")){
                activity.getSupportActionBar().hide();
                search_layout_ID_select_customer.setVisibility(View.VISIBLE);
                et_search_select_customer.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        activity.et_search.setText(s.toString());
                    }
                });
            }
        }

    }

//    @Override
//    public void onPause() {
//        activity.showNoContentAfterFilter(-1);
//        super.onPause();
//    }

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
