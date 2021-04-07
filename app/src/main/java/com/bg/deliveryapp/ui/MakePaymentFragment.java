package com.bg.deliveryapp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bg.deliveryapp.R;


public class MakePaymentFragment extends Fragment {


    private static final String EXTRA_TEXT = "text";

    public static MakePaymentFragment createFor(String text) {
        MakePaymentFragment fragment = new MakePaymentFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.make_payment_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        final String text = args != null ? args.getString(EXTRA_TEXT) : "";
    }






}
