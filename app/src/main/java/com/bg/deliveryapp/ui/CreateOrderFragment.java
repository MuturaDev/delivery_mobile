package com.bg.deliveryapp.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.bg.deliveryapp.MainActivity;
import com.bg.deliveryapp.R;
import com.bg.deliveryapp.adapters.CreateOrderCategoryAdapter;
import com.bg.deliveryapp.adapters.CreateOrderDeliveryAdapter;
import com.bg.deliveryapp.adapters.CreateOrderProductAdapter;
import com.bg.deliveryapp.api.ApiService;
import com.bg.deliveryapp.api.ServiceGenerator;
import com.bg.deliveryapp.api.models.requests.CreateOrderRequest;
import com.bg.deliveryapp.api.models.requests.MakePaymentRequest;
import com.bg.deliveryapp.api.models.requests.subRequest.OrderProduct;
import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;
import com.bg.deliveryapp.api.models.responses.CreateOrderCategoryResponse;
import com.bg.deliveryapp.api.models.responses.CreateOrderDeliveryResponse;
import com.bg.deliveryapp.api.models.responses.CreateOrderProductsResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.ClientSubResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.PendingOrderContent;
import com.bg.deliveryapp.pickers.DatePickerFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrderFragment extends Fragment  implements DatePickerFragment.DateDialogListener{


    public static List<CreateOrderProductsResponse> dataProductsList = new ArrayList<>();
    public static List<CreateOrderCategoryResponse> dataCategoryList = new ArrayList<>();
    public static ArrayList<CreateOrderDeliveryResponse> dataDeliveryList = new ArrayList<>();

    private static final String EXTRA_TEXT = "text";

    public static CreateOrderFragment createFor(Object message) {
        CreateOrderFragment fragment = new CreateOrderFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TEXT, (Serializable) message);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_order_fragment_layout, container, false);
    }

    private TextView txt_customer_type;

    private MainActivity activity;

    public CreateOrderCategoryAdapter categoryAdapter;
    public CreateOrderDeliveryAdapter deliveryAdapter;
    private CreateOrderProductAdapter productAdapter;
    private ImageView btn_add;
    private MultiSpinnerSearch multipleItemSelectionSpinner;

    private RecyclerView recyclerViewCategory;
    private RecyclerView recyclerViewDelivery;
    private RecyclerView recyclerViewProduct;

    private EditText et_location, txt_order_date;


    @Override
    public void onFinishDialog(Date date) {
        if(txt_order_date != null){
            txt_order_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(date.getTime()));
        }
    }

    private Button btn_saveorder;
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {



        txt_customer_type = view.findViewById(R.id.txt_customer_type);
        btn_add = view.findViewById(R.id.btn_add);
        txt_order_date = view.findViewById(R.id.txt_order_date);
        txt_order_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment(CreateOrderFragment.this);
                dialog.show(activity.getSupportFragmentManager(), "MainActivity.DateDialog");
            }
        });
        et_location = view.findViewById(R.id.et_location);

        multipleItemSelectionSpinner = view.findViewById(R.id.multipleItemSelectionSpinner);

        Bundle args = getArguments();
        final Object message = args != null ? args.getSerializable(EXTRA_TEXT) : "";
        if(message instanceof ClientSubResponse){
            txt_customer_type.setText(((ClientSubResponse) message).getFirstname() + " " + ((ClientSubResponse) message).getLastname());
            et_location.setText(((ClientSubResponse) message).getLocation());

        }

        activity = (MainActivity) getActivity();


        txt_customer_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.displayFragment("View Customers", "Select Customer");
            }
        });

        recyclerViewCategory = view.findViewById(R.id.recyclerView_category);
        progress_circular_category = view.findViewById(R.id.progress_circular_category);
        recyclerViewProduct = view.findViewById(R.id.recyclerView_product);
        progress_circular_delivery = view.findViewById(R.id.progress_circular_delivery);
        recyclerViewDelivery = view.findViewById(R.id.recyclerView_delivery);

        populateCategory();

        populateDelivery();

        populateProduct();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // populateMultipleSelectSpinner(dataProductsList);
                if(multipleItemSelectionSpinner != null)
                multipleItemSelectionSpinner.performClick();
            }
        });

        btn_saveorder = view.findViewById(R.id.btn_saveorder);
        btn_saveorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dataProductsList.size() > 0){

                    if(TextUtils.isEmpty(txt_order_date.getText().toString())){
                        txt_order_date.setError("Field cannot be left empty");
                        return;
                    }else{
                        txt_order_date.setError(null);
                    }

                    createOrder();
                }else{
                    Toast.makeText(activity, "Select products to continue", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    private void createOrder(){
        if(authenticationResponse.isUserLogededIn()) {
            ApiService apiService =
                    ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

            Bundle args = getArguments();
            final ClientSubResponse orderContent = args != null ? (ClientSubResponse) args.getSerializable(EXTRA_TEXT) : null;
            if(orderContent != null){

                Integer deliveryMethod = 0;
                for(CreateOrderDeliveryResponse item : dataDeliveryList){
                    if(item.getSelected().equalsIgnoreCase("Y")){
                        deliveryMethod = item.getId();
                        break;
                    }
                }

                Integer categorySelected = 0;
                for(CreateOrderCategoryResponse item : dataCategoryList){
                    if(item.getSelected().equalsIgnoreCase("Y")){
                        categorySelected = item.getId();
                        break;
                    }
                }
                List<OrderProduct> productList = new ArrayList<>();
                for(CreateOrderProductsResponse item : dataProductsList){
                    OrderProduct product = new OrderProduct(
                            String.valueOf(item.getId()),
                            String.valueOf(item.getQuantity())
                    );
                    productList.add(product);
                }

                CreateOrderRequest request1 = new CreateOrderRequest(
                        "",
                        Integer.valueOf(orderContent.getClientId()),
                        Integer.valueOf(deliveryMethod),
                        orderContent.getLocation(),
                        txt_order_date.getText().toString(),
                        "",
                        Integer.valueOf(categorySelected),
                        productList
                );

                final ProgressDialog pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Uploading data captured...");
                pDialog.setCancelable(false);
                pDialog.show();



                Call<Map<String,String>> call = apiService.createOrder(request1);
                call.enqueue(new Callback<Map<String,String>>() {
                    @Override
                    public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                        if(response.isSuccessful()){
                            if(response.code() == 201 || response.code() == 200){

                                activity.showSuccessDialog("Success!", "Order has been submitted", "continue", true, "Pending Orders");


                                //clearFields();

                            }else if(response.code() == 500){
                                try{
                                    String title = response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONObject("message").toString() : "Oops";

                                    String message = response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONObject("exceptionMessage").toString() : "exceptionMessage";

                                    activity.showSuccessDialog(title, message, "try again", false,null);


                                }catch (Exception e){

                                    activity.showSuccessDialog("Failure", "Failure to upload", "try again", false,null);
                                }

                            }else{
                                ResponseBody error = response.errorBody();
                                int i = 0;
                            }

                        }else{

                        /*{
                            "code": "e.xx.fw.9001",
                                "message": "System error occurred!",
                                "exceptionMessage": null,
                                "details": [
                            {
                                "code": "Email",
                                    "message": "must be a well-formed email address",
                                    "exceptionMessage": "email"
                            }
                        ]
                        }*/

                            try{
                                JSONObject errorJson = new JSONObject(response.errorBody().string());

                                if(errorJson.has("details")){

                                    JSONArray errorArray = (JSONArray) errorJson.get("details");
                                    if(errorArray.length() > 0) {
                                        JSONObject jsonDetails = (JSONObject) errorArray.get(0);
                                        String title = (errorJson.has("message") ? errorJson.get("message").toString() : "");
                                        String message = "";

                                        if (jsonDetails.has("code") && jsonDetails.has("message"))
                                            message = (jsonDetails.get("code").toString() + " " + jsonDetails.get("message").toString());
                                        else {
                                            message = (errorJson.get("message").toString());
                                            title = ("System error occurred!");
                                        }

                                        activity.showSuccessDialog(title, message, "try again", false,null);
                                    }

                                }else{
                                    String title = ("System error occurred!");
                                    String message = (errorJson.has("message") ? errorJson.get("message").toString() : "");
                                    activity.showSuccessDialog(title, message, "try again", false,null);
                                }

                            }catch (Exception ex){
                                String title = ("Failure!");
                                String message = ex.getMessage();
                                activity.showSuccessDialog(title, message, "try again", false,null);

                            }


                        }

                        pDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Map<String,String>> call, Throwable t) {
                        String title = ("Failure!");
                        String message = t.getMessage();
                        activity.showSuccessDialog(title, message, "try again", false,null);
                    }
                });




            }
        }
    }


    private ProgressBar progress_circular_category;
    private AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    private void populateCategory(){
        if(((MainActivity)getActivity()) != null){
            progress_circular_category.setVisibility(View.VISIBLE);


            Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
            if(object!= null)
                if(((List<AuthenticationResponse>) object).size() > 0)
                    authenticationResponse = ((List<AuthenticationResponse>) object).get(0);

            if(authenticationResponse != null){
                ApiService apiService =
                        ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

                Call<List<CreateOrderCategoryResponse>> call = apiService.getCategory();
                call.enqueue(new Callback<List<CreateOrderCategoryResponse>>() {
                    @Override
                    public void onResponse(Call<List<CreateOrderCategoryResponse>> call, Response<List<CreateOrderCategoryResponse>> response) {

                        if(response.isSuccessful()){
                            if(response.code() == 200){

                                dataCategoryList.clear();
                                dataCategoryList = response.body();

                                LinearLayoutManager horizontalLayoutManager
                                        = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                                recyclerViewCategory.setLayoutManager(horizontalLayoutManager);
                                categoryAdapter = new CreateOrderCategoryAdapter(CreateOrderFragment.this);
                                //categoryAdapter.setClickListener(this);
                                recyclerViewCategory.setAdapter(categoryAdapter);
                                categoryAdapter.notifyDataSetChanged();
                            }
                        }

                        progress_circular_category.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<CreateOrderCategoryResponse>> call, Throwable t) {
                        progress_circular_category.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    private ProgressBar progress_circular_delivery;
    private void populateDelivery(){
        if(((MainActivity)getActivity()) != null){
            progress_circular_delivery.setVisibility(View.VISIBLE);

            Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
            if(object!= null)
                if(((List<AuthenticationResponse>) object).size() > 0)
                    authenticationResponse = ((List<AuthenticationResponse>) object).get(0);

            if(authenticationResponse != null){
                ApiService apiService =
                        ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

                Call<ArrayList<CreateOrderDeliveryResponse>> call = apiService.getDelivery();
                call.enqueue(new Callback<ArrayList<CreateOrderDeliveryResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<CreateOrderDeliveryResponse>> call, Response<ArrayList<CreateOrderDeliveryResponse>> response) {

                        if(response.isSuccessful()){
                            if(response.code() == 200){



                                dataDeliveryList.clear();
                                for(CreateOrderDeliveryResponse item : response.body()){
                                    if(item.getDesc().contains("Factory Pickup")){
                                        item.setDesc("Pickup");
                                    }
                                    if(item.getDesc().equalsIgnoreCase("Drop Ship"))
                                        response.body().remove(item);
                                }



                                dataDeliveryList = response.body();
                                Collections.reverse(dataDeliveryList);

                                LinearLayoutManager horizontalLayoutManagerDelivery
                                        = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                                recyclerViewDelivery.setLayoutManager(horizontalLayoutManagerDelivery);
                                deliveryAdapter = new CreateOrderDeliveryAdapter(CreateOrderFragment.this);
                                //categoryAdapter.setClickListener(this);
                                recyclerViewDelivery.setAdapter(deliveryAdapter);
                                deliveryAdapter.notifyDataSetChanged();

                            }
                        }

                        progress_circular_delivery.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<CreateOrderDeliveryResponse>> call, Throwable t) {
                        progress_circular_delivery.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
    
    
    
    private void populateProduct(){

        if(((MainActivity)getActivity()) != null){
           // progress_circular_delivery.setVisibility(View.VISIBLE);

            Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
            if(object!= null)
                if(((List<AuthenticationResponse>) object).size() > 0)
                    authenticationResponse = ((List<AuthenticationResponse>) object).get(0);

            if(authenticationResponse != null){
                ApiService apiService =
                        ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

                Call<List<CreateOrderProductsResponse>> call = apiService.getProducts();
                call.enqueue(new Callback<List<CreateOrderProductsResponse>>() {
                    @Override
                    public void onResponse(Call<List<CreateOrderProductsResponse>> call, Response<List<CreateOrderProductsResponse>> response) {

                        if(response.isSuccessful()){
                            if(response.code() == 200){
                                populateMultipleSelectSpinner(response.body());
                            }
                        }

                        progress_circular_delivery.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<CreateOrderProductsResponse>> call, Throwable t) {
                        progress_circular_delivery.setVisibility(View.GONE);
                    }
                });
            }
        }

    }



    private void populateMultipleSelectSpinner(List<CreateOrderProductsResponse> dropDownList){
        final List<KeyPairBoolData> listArray0 = new ArrayList<>();
        listArray0.clear();
        for (int ii = 0; ii < dropDownList.size(); ii++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(Integer.valueOf(dropDownList.get(ii).getId()));
            h.setName(dropDownList.get(ii).getDesc());
            h.setSelected(false);
            listArray0.add(h);
        }

        /**
         * Search MultiSelection Spinner (With Search/Filter Functionality)
         *
         *  Using MultiSpinnerSearch class
         */
        multipleItemSelectionSpinner = getView().findViewById(R.id.multipleItemSelectionSpinner);

        // Pass true If you want searchView above the list. Otherwise false. default = true.
        multipleItemSelectionSpinner.setSearchEnabled(true);

        // A text that will display in search hint.
        multipleItemSelectionSpinner.setSearchHint("Select one/multiple");

        // Set text that will display when search result not found...
        multipleItemSelectionSpinner.setEmptyTitle("Not Data Found!");

        // If you will set the limit, this button will not display automatically.
        multipleItemSelectionSpinner.setShowSelectAllButton(true);

        //A text that will display in clear text button
        multipleItemSelectionSpinner.setClearText("Close & Clear");

        // Removed second parameter, position. Its not required now..
        // If you want to pass preselected items, you can do it while making listArray,
        // pass true in setSelected of any item that you want to preselect
        multipleItemSelectionSpinner.setItems(listArray0, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                List<CreateOrderProductsResponse> checkItems = new ArrayList<>();
                //add checked item
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        //Log.i(TAG, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                        String productName = items.get(i).getName();
                        Long productID = items.get(i).getId();

                        CreateOrderProductsResponse checkProduct = new CreateOrderProductsResponse(String.valueOf(productID), productName);
                        checkItems.add(checkProduct);
                    }
                }

                dataProductsList = (checkItems);

                LinearLayoutManager horizontalLayoutManagerProduct
                        = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                recyclerViewProduct.setLayoutManager(horizontalLayoutManagerProduct);
                productAdapter = new CreateOrderProductAdapter(CreateOrderFragment.this);
                //categoryAdapter.setClickListener(this);
                recyclerViewProduct.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
            }
        });

        /**
         * If you want to set limit as maximum item should be selected is 2.
         * For No limit -1 or do not call this method.
         *
         */
        multipleItemSelectionSpinner.setLimit(1000, new MultiSpinnerSearch.LimitExceedListener() {
            @Override
            public void onLimitListener(KeyPairBoolData data) {
//                Toast.makeText(getApplicationContext(),
//                        "Limit exceed ", Toast.LENGTH_LONG).show();
            }
        });

    }




}