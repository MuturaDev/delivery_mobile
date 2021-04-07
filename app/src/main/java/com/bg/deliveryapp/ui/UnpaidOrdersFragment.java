package com.bg.deliveryapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.R;
import com.bg.deliveryapp.adapters.ViewPendingOrdersAdapter;
import com.bg.deliveryapp.adapters.ViewUnpaidOrdersAdapter;

public class UnpaidOrdersFragment extends Fragment {

    private static final String EXTRA_TEXT = "text";

    public static UnpaidOrdersFragment createFor(String text) {
        UnpaidOrdersFragment fragment = new UnpaidOrdersFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_unpaid_orders_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        final String text = args != null ? args.getString(EXTRA_TEXT) : "";

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ViewUnpaidOrdersAdapter adapter = new ViewUnpaidOrdersAdapter(getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}