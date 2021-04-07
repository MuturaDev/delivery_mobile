package com.bg.deliveryapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

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
        progress_circular.setVisibility(View.VISIBLE);

        Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
        if(object!= null)
            if(((List<AuthenticationResponse>) object).size() > 0)
               authenticationResponse = ((List<AuthenticationResponse>) object).get(0);

        if(authenticationResponse != null){
            ApiService apiService =
                    ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

            Call<ViewCustomerResponse> call = apiService.getCustomers(((MainActivity)getActivity()).et_search.getText().toString().trim(), 1,10);
            call.enqueue(new Callback<ViewCustomerResponse>() {
                @Override
                public void onResponse(Call<ViewCustomerResponse> call, Response<ViewCustomerResponse> response) {

                    if(response.isSuccessful()){
                        if(response.code() == 200){
                            adapter = new ViewCustomersAdapter(activity,getActivity(),response.body().getContent());
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
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
    
    
    
    //conver to fragment
    private static final String EXTRA_TEXT = "text";

    public static ViewCustomerFragment createFor(String text) {
        ViewCustomerFragment fragment = new ViewCustomerFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_customer_fragment_layout, container, false);
    }

    private ProgressBar progress_circular;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        final String text = args != null ? args.getString(EXTRA_TEXT) : "";
//        TextView textView = view.view.findViewById(R.id.text);
//        textView.setText(text);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
//            }
//        });

        activity = (MainActivity) getActivity();


        recyclerView = view.findViewById(R.id.recyclerView);
        btn_add_customer = view.findViewById(R.id.btn_add_customer);
        progress_circular = view.findViewById(R.id.progress_circular);



        populateRecyclerView();


        btn_add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.displayFragment("Add Customer");
            }
        });
    }


}
