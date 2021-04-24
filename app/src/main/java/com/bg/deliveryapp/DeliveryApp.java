package com.bg.deliveryapp;

import android.app.Application;

import com.bg.deliveryapp.networkcheck.ConnectivityReceiver;
import com.narify.netdetect.NetDetect;


//import me.bemind.customfontlibrary.FontUtil;

public class DeliveryApp  extends  com.orm.SugarApp{
    private static DeliveryApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        NetDetect.init(this);
        mInstance = this;
    }

    public static synchronized DeliveryApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
