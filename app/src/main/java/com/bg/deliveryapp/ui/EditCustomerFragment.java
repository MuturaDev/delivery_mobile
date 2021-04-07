package com.bg.deliveryapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bg.deliveryapp.R;

public class EditCustomerFragment extends Fragment {

    


    private static final String EXTRA_TEXT = "text";

    public static EditCustomerFragment createFor(String text) {
        EditCustomerFragment fragment = new EditCustomerFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_customer_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
//        final String text = args != null ? args.getString(EXTRA_TEXT) : "";
//        TextView textView = view.findViewById(R.id.text);
//        textView.setText(text);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
