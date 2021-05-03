package com.bg.deliveryapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.transition.Slide;
//import androidx.transition.Transition;
//import androidx.transition.TransitionManager;

import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.PendingOrderContent;
import com.bg.deliveryapp.customview.GifImageView;
import com.bg.deliveryapp.leftdrawer.SlidingRootNav;
import com.bg.deliveryapp.leftdrawer.SlidingRootNavBuilder;
import com.bg.deliveryapp.leftdrawer.ui.fragment.CenteredTextFragment;
import com.bg.deliveryapp.leftdrawer.ui.menu.DrawerAdapter;
import com.bg.deliveryapp.leftdrawer.ui.menu.DrawerItem;
import com.bg.deliveryapp.leftdrawer.ui.menu.SimpleItem;
import com.bg.deliveryapp.leftdrawer.ui.menu.SpaceItem;
import com.bg.deliveryapp.networkcheck.ConnectivityReceiver;
import com.bg.deliveryapp.pickers.DatePickerFragment;
import com.bg.deliveryapp.sectionedrecyclerview.AdapterSectionRecycler;
import com.bg.deliveryapp.sectionedrecyclerview.Child;
import com.bg.deliveryapp.sectionedrecyclerview.SectionHeader;
import com.bg.deliveryapp.ui.AddCustomerFragment;
import com.bg.deliveryapp.ui.CreateOrderFragment;
import com.bg.deliveryapp.ui.PendingOrdersFragment;
import com.bg.deliveryapp.ui.UnpaidOrdersFragment;
import com.bg.deliveryapp.ui.ViewCustomerFragment;
import com.bg.deliveryapp.ui.ViewIndividualOrdersFragment;
import com.narify.netdetect.NetDetect;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_CART = 3;
    private static final int POS_LOGOUT = 5;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    private AuthenticationResponse authenticationResponse = new AuthenticationResponse();

    public RelativeLayout no_content_after_filter;


    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        DeliveryApp.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Toast.makeText(this, "Testing", Toast.LENGTH_SHORT).show();
//        showSnack(isConnected);
        if(isConnected){
            ((TextView)  findViewById(R.id.internet_view)).setVisibility(View.GONE);
        }else{
            ((TextView)  findViewById(R.id.internet_view)).setVisibility(View.VISIBLE);
        }
    }



    public void showSuccessDialog(String title, String message, String btnStatus, boolean status,Object... backTo){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_succes_dialog, (ConstraintLayout) findViewById(R.id.layout_dialog_container));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.txtTitle)).setText(title);
        ((TextView) view.findViewById(R.id.txtTitle)).setTextColor(status ?  getResources().getColor(R.color.colorGreen) : getResources().getColor(android.R.color.holo_red_dark));


        ((TextView) view.findViewById(R.id.txtMessage)).setText(message);
        ((Button) view.findViewById(R.id.buttonAction)).setText(btnStatus);
        ((Button) view.findViewById(R.id.buttonAction)).setBackground(getResources().getDrawable(status ? R.drawable.button_success_background : R.drawable.button_error_background ));

        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(status ?  R.drawable.ic_done : R.drawable.ic_error);
        ((ImageView) view.findViewById(R.id.imageIcon)).setBackground(getResources().getDrawable(status ? R.drawable.title_success_background : R.drawable.title_error_background ));


        final android.app.AlertDialog alertDialog = builder.create();
        //Alvin asked to be removed
        //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(backTo != null){
                    if(backTo.length > 0)
                    if(backTo[0] instanceof  String) {
                        String backToString =  (String) backTo[0];
                        displayFragment(backToString, null);
                    }
                }

                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

    }


    //https://medium.com/dsc-alexandria/implementing-internet-connectivity-checker-in-android-apps-bf28230c4e86
    //https://github.com/SalahoAmro/NetDetect


    private GifImageView gifImageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.internet_connection);
        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        LinearLayout activity_main = findViewById(R.id.activity_main);
        activity_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                slidingRootNav.closeMenu();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        no_content_after_filter = findViewById(R.id.no_content_after_filter);

        Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
        if(object!= null)
            if(((List<AuthenticationResponse>) object).size() > 0)
                authenticationResponse = ((List<AuthenticationResponse>) object).get(0);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();


        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_CART),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

//        RecyclerView list = findViewById(R.id.list);
//        list.setNestedScrollingEnabled(false);
//        list.setLayoutManager(new LinearLayoutManager(this));
//        list.setAdapter(adapter);
//
//        adapter.setSelected(POS_DASHBOARD);

        customRecyclerView();

        displayFragment("Pending Orders",null);

    }

    RecyclerView recyclerView;
    public AdapterSectionRecycler adapterRecycler;
    List<SectionHeader> sectionHeaders;

    private void customRecyclerView(){

//initialize RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.list);

        //setLayout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        //Create a List of Child DataModel
        List<Child> childList = new ArrayList<>();
//        childList.add(new Child("View Customers",R.drawable.customers));
//        childList.add(new Child("Add Customer", R.drawable.orders));
        //childList.add(new Child("Edit Customer",R.drawable.edit_customer));

//        childList.add(new Child("Aakash"));

        //Create a List of SectionHeader DataModel implements SectionHeader
        sectionHeaders = new ArrayList<>();
        sectionHeaders.add(new SectionHeader(childList, "View Customers", 0,R.drawable.customers));

        childList = new ArrayList<>();
//        childList.add(new Child("View Orders",R.drawable.view_orders_icon));
//        childList.add(new Child("View Individual Orders",R.drawable.view_individual_orders));
//        childList.add(new Child("Add Order", R.drawable.add_order_icon));
        sectionHeaders.add(new SectionHeader(childList, "View Orders", 0,R.drawable.orders));

//        childList = new ArrayList<>();
//        childList.add(new Child("Make Payment",R.drawable.make_payment));
////        childList.add(new Child("Invincible Vinod"));
//        sectionHeaders.add(new SectionHeader(childList, "Payments", 1,0));

//        childList = new ArrayList<>();
//        childList.add(new Child("Jim Carry"));
//        sectionHeaders.add(new SectionHeader(childList, "J", 4));
//
//        childList = new ArrayList<>();
//        childList.add(new Child("Neil Patrick Harris"));
//        sectionHeaders.add(new SectionHeader(childList, "N", 3));
//
//        childList = new ArrayList<>();
//        childList.add(new Child("Orange"));
//        childList.add(new Child("Olive"));
//        sectionHeaders.add(new SectionHeader(childList, "O", 5));

        adapterRecycler = new AdapterSectionRecycler(this, sectionHeaders);
        recyclerView.setAdapter(adapterRecycler);
        adapterRecycler.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(int position) {
//        if (position == POS_LOGOUT) {
//            finish();
//        }
//        slidingRootNav.closeMenu();
//
//        Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
//        showFragment(CenteredTextFragment.createFor(screenTitles[position]));
    }



    public void showNoContentAfterFilter(int total, MainActivity activity){
        if(!TextUtils.isEmpty((et_search.getText().toString()))){
            if(total == 0){
                no_content_after_filter.setVisibility(View.VISIBLE);
                if(activity != null)
                    hideKeyboard(activity);
            }else if(total == -1){
                no_content_after_filter.setVisibility(View.GONE);
                et_search.setText("");

                et_search.setFocusableInTouchMode(false);
                et_search.setFocusable(false);
                et_search.setFocusableInTouchMode(true);
                et_search.setFocusable(true);
            }
            else{
                no_content_after_filter.setVisibility(View.GONE);
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


    public static String CurrentScreen = "";
    
    public void displayFragment(String title, Object message){

        NetDetect.check((isConnected -> {if(isConnected){
            ((TextView)  findViewById(R.id.internet_view)).setVisibility(View.GONE);
            gifImageView.setVisibility(View.GONE);

        }else{
            ((TextView)  findViewById(R.id.internet_view)).setVisibility(View.VISIBLE);
            gifImageView.setVisibility(View.GONE);
        }}));

        getSupportActionBar().show();
        hideKeyboard(MainActivity.this);
        no_content_after_filter.setVisibility(View.GONE);
        ((EditText)findViewById(R.id.et_search)).setText("");

        switch(title){
            case "View Customers":
                top_master_activity_header_with_backleft_layout("", false);
                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(true, "Search Customers");
                if(message instanceof String){
                    if(message.equals("Select Customer")) {
                        showFragment(ViewCustomerFragment.createFor(message));
                    }else{
                        showFragment(ViewCustomerFragment.createFor(message));
                    }
                }else{
                    showFragment(ViewCustomerFragment.createFor(message));
                }

                break;
            case "Edit Customer":
                top_master_activity_header_with_backleft_layout("Edit Customer", true);
                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(false, null);
                showFragment(AddCustomerFragment.createFor(message));
                break;
            case "Add Customer":
                top_master_activity_header_with_backleft_layout("Add Customer", true);
                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(false, null);
                showFragment(AddCustomerFragment.createFor(message));
                break;
            case "Add Order":
                if(message instanceof PendingOrderContent){
                    top_master_activity_header_with_backleft_layout("Edit Order", true);
                }else{
                    top_master_activity_header_with_backleft_layout("New Order", true);
                }

                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(false, null);
                showFragment(CreateOrderFragment.createFor(message));
                break;
            case "View Orders":
//                top_master_activity_header_with_backleft_layout("", false);
//                top_master_activity_header_with_backright_layout("Pending Orders", true);
//                top_master_action_bar_layout(true);
//                showFragment(ViewOrdersFragment.createFor(message));
                break;
            case "View Individual Orders":
                top_master_activity_header_with_backleft_layout(((PendingOrderContent)message).getCustomerName(), true);
                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(false, null);
                showFragment(ViewIndividualOrdersFragment.createFor(message));
                break;
//            case "Make Payment":
//                top_master_activity_header_with_backleft_layout("James Kimani", true);
//                top_master_activity_header_with_backright_layout("", false);
//                top_master_action_bar_layout(false);
//                showFragment(MakePaymentFragment.createFor(null));
//                break;
            case "Pending Orders":
                top_master_activity_header_with_backleft_layout("", false);
                top_master_activity_header_with_backright_layout("Pending Orders", true);
                top_master_action_bar_layout(true, "Search Orders");
                showFragment(PendingOrdersFragment.createFor(message));
                break;
            case "Unpaid Orders":
                top_master_activity_header_with_backleft_layout("", false);
                top_master_activity_header_with_backright_layout("Unpaid Orders", true);
                top_master_action_bar_layout(true, "Search Orders");
                showFragment(UnpaidOrdersFragment.createFor(message));
                break;
            default:
                top_master_activity_header_with_backleft_layout("", false);
                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(false, null);
                showFragment(CenteredTextFragment.createFor(message));
                break;

        }

        //Toast.makeText(this, title, Toast.LENGTH_SHORT).show();

        CurrentScreen = title;

        slidingRootNav.closeMenu();
    }


    @Override
    public void onBackPressed() {
        for( Fragment fragment : fragmentManager.getFragments()){
            if(fragment instanceof PendingOrdersFragment){
                //super.onBackPressed();
                finish();
            }else{

                if(CurrentScreen.equalsIgnoreCase("Add Customer") || CurrentScreen.equalsIgnoreCase("Edit Customer")){
                    displayFragment("View Customers", null);
                }else{
                    displayFragment("Pending Orders", null);
                }


                break;

            }
        }

    }


    private FragmentManager fragmentManager;

    private void showFragment(Fragment fragment) {
        //https://stackoverflow.com/questions/4932462/animate-the-transition-between-fragments
       fragmentManager = getSupportFragmentManager();
       fragmentManager.beginTransaction()
        .setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim)
        .replace(R.id.container, fragment)
        .commit();

    }


    public EditText et_search;
    private void top_master_action_bar_layout(boolean showSearchLayout, String searchHintText){

        ImageButton btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setVisibility(View.VISIBLE);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setCancelable(false);
                alert.setTitle("Logout");
                alert.setMessage("Are you sure you want to logout?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        authenticationResponse.delete();

                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();


                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });

        LinearLayout search_layout_ID = findViewById(R.id.search_layout_ID);
        search_layout_ID.setVisibility(showSearchLayout ? View.VISIBLE : View.GONE);
        et_search = findViewById(R.id.et_search);
        if(searchHintText != null)
         et_search.setHint(searchHintText);

        //top_master_action_bar_layout
        ImageButton  btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void top_master_activity_header_with_backleft_layout(String title, boolean show){
        //top_master_activity_header_with_backleft_layout
        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView label_title_page = findViewById(R.id.label_title_page);
        label_title_page.setText(title);
        RelativeLayout top_master_activity_header_with_backleft_layout = findViewById(R.id.top_master_activity_header_with_backleft_layout);
        top_master_activity_header_with_backleft_layout.setVisibility(show ? View.VISIBLE : View.GONE);

    }

    //top_master_activity_header_with_backright_layout
    private ImageButton btn_right;
    private TextView title_left;
    private RelativeLayout top_master_activity_header_with_backright_layout;
    private void top_master_activity_header_with_backright_layout(final String title,boolean show){
        btn_right = findViewById(R.id.btn_right);
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.equals("Pending Orders")){
                    displayFragment("Unpaid Orders",null);
                }else if(title.equals("Unpaid Orders")){
                    displayFragment("Pending Orders",null);
                }
            }
        });
        title_left = findViewById(R.id.title_left);
        title_left.setText(title);
        top_master_activity_header_with_backright_layout = findViewById(R.id.top_master_activity_header_with_backright_layout);
        top_master_activity_header_with_backright_layout.setVisibility(show ? View.VISIBLE : View.GONE);
    }



    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorGreen))
                .withSelectedTextTint(color(R.color.colorGreen));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }






}
