package com.bg.deliveryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;
import com.bg.deliveryapp.leftdrawer.SlidingRootNav;
import com.bg.deliveryapp.leftdrawer.SlidingRootNavBuilder;
import com.bg.deliveryapp.leftdrawer.ui.fragment.CenteredTextFragment;
import com.bg.deliveryapp.leftdrawer.ui.menu.DrawerAdapter;
import com.bg.deliveryapp.leftdrawer.ui.menu.DrawerItem;
import com.bg.deliveryapp.leftdrawer.ui.menu.SimpleItem;
import com.bg.deliveryapp.leftdrawer.ui.menu.SpaceItem;
import com.bg.deliveryapp.sectionedrecyclerview.AdapterSectionRecycler;
import com.bg.deliveryapp.sectionedrecyclerview.Child;
import com.bg.deliveryapp.sectionedrecyclerview.SectionHeader;
import com.bg.deliveryapp.ui.AddCustomerFragment;
import com.bg.deliveryapp.ui.CreateOrderFragment;
import com.bg.deliveryapp.ui.EditCustomerFragment;
import com.bg.deliveryapp.ui.PendingOrdersFragment;
import com.bg.deliveryapp.ui.UnpaidOrdersFragment;
import com.bg.deliveryapp.ui.ViewCustomerFragment;
import com.bg.deliveryapp.ui.ViewIndividualOrdersFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yarolegovich on 25.03.2017.
 */

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_CART = 3;
    private static final int POS_LOGOUT = 5;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    private AuthenticationResponse authenticationResponse = new AuthenticationResponse();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
        if(object!= null)
            if(((List<AuthenticationResponse>) object).size() > 0)
                authenticationResponse = ((List<AuthenticationResponse>) object).get(0);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
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

        displayFragment("Pending Orders");

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
        childList.add(new Child("View Customers",R.drawable.view_customers_icon));
        childList.add(new Child("Add Customer", R.drawable.add_customer));
        childList.add(new Child("Edit Customer",R.drawable.edit_customer));

//        childList.add(new Child("Aakash"));

        //Create a List of SectionHeader DataModel implements SectionHeader
        sectionHeaders = new ArrayList<>();
        sectionHeaders.add(new SectionHeader(childList, "Customers", 6,0));

        childList = new ArrayList<>();
        childList.add(new Child("View Orders",R.drawable.view_orders_icon));
        childList.add(new Child("View Individual Orders",R.drawable.view_individual_orders));
        childList.add(new Child("Add Order", R.drawable.add_order_icon));
        sectionHeaders.add(new SectionHeader(childList, "Orders", 2,0));

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
    
    
    public void displayFragment(String title){
        switch(title){
            case "View Customers":
                top_master_activity_header_with_backleft_layout("", false);
                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(true);
                showFragment(ViewCustomerFragment.createFor(null));
                break;
            case "Edit Customer":
                top_master_activity_header_with_backleft_layout("Edit Customer", true);
                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(false);
                showFragment(EditCustomerFragment.createFor(null));
                break;
            case "Add Customer":
                top_master_activity_header_with_backleft_layout("Add Customer", true);
                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(false);
                showFragment(AddCustomerFragment.createFor(null));
                break;
            case "Add Order":
                top_master_activity_header_with_backleft_layout("New Order", true);
                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(false);
                showFragment(CreateOrderFragment.createFor(null));
                break;
            case "View Orders":
//                top_master_activity_header_with_backleft_layout("", false);
//                top_master_activity_header_with_backright_layout("Pending Orders", true);
//                top_master_action_bar_layout(true);
//                showFragment(ViewOrdersFragment.createFor(null));
                break;
            case "View Individual Orders":
                top_master_activity_header_with_backleft_layout("James Kimani", true);
                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(false);
                showFragment(ViewIndividualOrdersFragment.createFor(null));
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
                top_master_action_bar_layout(true);
                showFragment(PendingOrdersFragment.createFor(null));
                break;
            case "Unpaid Orders":
                top_master_activity_header_with_backleft_layout("", false);
                top_master_activity_header_with_backright_layout("Unpaid Orders", true);
                top_master_action_bar_layout(true);
                showFragment(UnpaidOrdersFragment.createFor(null));
                break;
            default:
                top_master_activity_header_with_backleft_layout("", false);
                top_master_activity_header_with_backright_layout("", false);
                top_master_action_bar_layout(false);
                showFragment(CenteredTextFragment.createFor(null));
                break;

        }

        //Toast.makeText(this, title, Toast.LENGTH_SHORT).show();

        slidingRootNav.closeMenu();
    }


    @Override
    public void onBackPressed() {
        for( Fragment fragment : fragmentManager.getFragments()){
            if(fragment instanceof PendingOrdersFragment){
                super.onBackPressed();
            }else{
                displayFragment("Pending Orders");
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


    //top_master_action_bar_layout
    private ImageButton btn_search,btn_logout;
    private LinearLayout search_layout_ID;
    public EditText et_search;
    private void top_master_action_bar_layout(boolean showSearchLayout){

        btn_logout = findViewById(R.id.btn_logout);
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

        search_layout_ID = findViewById(R.id.search_layout_ID);
        search_layout_ID.setVisibility(showSearchLayout ? View.VISIBLE : View.GONE);
        et_search = findViewById(R.id.et_search);
        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    //top_master_activity_header_with_backleft_layout
    private ImageButton btn_back;
    private TextView label_title_page;
    private RelativeLayout top_master_activity_header_with_backleft_layout;
    private void top_master_activity_header_with_backleft_layout(String title, boolean show){
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for( Fragment fragment : fragmentManager.getFragments()){
                    if(fragment instanceof PendingOrdersFragment){

                    }else{
                        displayFragment("Pending Orders");
                        break;
                    }
                }
            }
        });
        label_title_page = findViewById(R.id.label_title_page);
        label_title_page.setText(title);
        top_master_activity_header_with_backleft_layout = findViewById(R.id.top_master_activity_header_with_backleft_layout);
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
                    displayFragment("Unpaid Orders");
                }else if(title.equals("Unpaid Orders")){
                    displayFragment("Pending Orders");
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
