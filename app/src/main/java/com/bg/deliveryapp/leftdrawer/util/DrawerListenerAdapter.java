package com.bg.deliveryapp.leftdrawer.util;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bg.deliveryapp.R;
import com.bg.deliveryapp.leftdrawer.callback.DragListener;
import com.bg.deliveryapp.leftdrawer.callback.DragStateListener;


/**
 * Created by yarolegovich on 26.03.2017.
 */

public class DrawerListenerAdapter implements DragListener, DragStateListener {

    private DrawerLayout.DrawerListener adaptee;
    private View drawer;
    private Activity activity;
    private ActionBarDrawerToggle toggle;


    public DrawerListenerAdapter(DrawerLayout.DrawerListener adaptee, View drawer, Activity activity,ActionBarDrawerToggle toggle) {
        this.adaptee = adaptee;
        this.drawer = drawer;
        this.activity = activity;
        this.toggle = toggle;
    }

    @Override
    public void onDrag(float progress) {
        adaptee.onDrawerSlide(drawer, progress);
    }

    @Override
    public void onDragStart() {
        adaptee.onDrawerStateChanged(DrawerLayout.STATE_DRAGGING);
    }

    @Override
    public void onDragEnd(boolean isMenuOpened) {
        if (isMenuOpened) {
            //customize
            Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.left_menu_icon , activity.getTheme());
            toggle.setHomeAsUpIndicator(drawable);
            toggle.syncState();

            adaptee.onDrawerOpened(drawer);

        } else {
            //customize
            Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(), R.drawable.menu_squared_icon2, activity.getTheme());
            toggle.setHomeAsUpIndicator(drawable);
            toggle.syncState();

            adaptee.onDrawerClosed(drawer);
        }
        adaptee.onDrawerStateChanged(DrawerLayout.STATE_IDLE);
    }
}
