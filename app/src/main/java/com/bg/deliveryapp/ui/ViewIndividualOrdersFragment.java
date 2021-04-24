package com.bg.deliveryapp.ui;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bg.deliveryapp.MainActivity;
import com.bg.deliveryapp.R;
import com.bg.deliveryapp.adapters.OrderDetailsProductListAdapter;
import com.bg.deliveryapp.adapters.OrderPaymentsListAdapter;
import com.bg.deliveryapp.api.ApiService;
import com.bg.deliveryapp.api.ServiceGenerator;
import com.bg.deliveryapp.api.models.requests.EditCustomerRequest;
import com.bg.deliveryapp.api.models.requests.MakePaymentRequest;
import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;
import com.bg.deliveryapp.api.models.responses.OrderPaymentsListResponse;
import com.bg.deliveryapp.api.models.responses.OrderProductsDetailsResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.PendingOrderContent;
import com.bg.deliveryapp.pickers.DatePickerFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

///https://www.codeproject.com/Articles/1112812/Android-Alert-Dialog-Tutorial-Working-with-Time-Pi
public class ViewIndividualOrdersFragment extends Fragment  implements DatePickerFragment.DateDialogListener{


    private static final String EXTRA_TEXT = "text";

    public static ViewIndividualOrdersFragment createFor(Object message) {
        ViewIndividualOrdersFragment fragment = new ViewIndividualOrdersFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TEXT, (Serializable) message);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_individual_order_activity_layout, container, false);
    }

    private ImageView order_status;
    private TextView order_status_text,txt_amount_to_pay,order_number,order_date,payment_status_text;
    private MainActivity activity;
    private RecyclerView recyclerViewProducts;
    private Button btn_complete_order;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        order_status = view.findViewById(R.id.order_status);
        order_status_text = view.findViewById(R.id.order_status_text);
        payment_status_text = view.findViewById(R.id.payment_status_text);
        txt_amount_to_pay = view.findViewById(R.id.txt_amount_to_pay);
        order_number = view.findViewById(R.id.order_number);
        order_date = view.findViewById(R.id.order_date);
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        recyclerViewPayments = view.findViewById(R.id.recyclerViewPayments);
        btn_complete_order = view.findViewById(R.id.btn_complete_order);


        activity = (MainActivity)getActivity();

        Bundle args = getArguments();
        final PendingOrderContent orderContent = args != null ? (PendingOrderContent) args.getSerializable(EXTRA_TEXT) : null;
        if(orderContent != null){

            if(orderContent.getStatus().equalsIgnoreCase("Unpaid") && orderContent.getOrderStatus().equalsIgnoreCase("Pending")){
                order_status_text.setText("Not Completed;");
                payment_status_text.setText("Not Paid");
                order_status.setImageResource(R.drawable.red_circle);
            }else if(orderContent.getStatus().equalsIgnoreCase("Paid")  && orderContent.getOrderStatus().equalsIgnoreCase("Pending")){
                order_status_text.setText("Not Completed");
                payment_status_text.setText("Paid");
                order_status.setImageResource(R.drawable.red_circle);
            }else if(orderContent.getStatus().equalsIgnoreCase("Partially Paid") && orderContent.getOrderStatus().equalsIgnoreCase("Pending")){
                order_status_text.setText("Not Completed");
                payment_status_text.setText("Partially Paid");
                order_status.setImageResource(R.drawable.red_circle);
            }else{
                order_status_text.setText("Completed");
                payment_status_text.setText("Paid");
                order_status.setImageResource(R.drawable.green_circle);
            }


            txt_amount_to_pay.setText(new DecimalFormat("#,###.00").format(Integer.valueOf(orderContent.getTotalAmount()))+ "/=");

            order_number.setText(orderContent.getOrderNo().toString());
            order_date.setText(orderContent.getOrderDate());

            populateOrderProducts(orderContent.getOrderNo());
           populatePaymentDetailsList(String.valueOf(orderContent.getOrderNo()));

            btn_complete_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postUpdateCompleteOrder(new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
                }
            });
        }

        CardView btn_receive_payment = view.findViewById(R.id.btn_receive_payment);
        btn_receive_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentDialog(null,null,null,null, null);
            }
        });



    }




    private static final String DIALOG_DATE = "MainActivity.DateDialog";
   private  EditText payment_date;
    @Override
    public void onFinishDialog(Date date) {
        if(payment_date != null){
            payment_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(date.getTime()));
        }
    }

    public void showPaymentDialog(String method,String amount, String reference, String date, String paymentId){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(activity).inflate(R.layout.capture_payment_details_layout_dialog, (CardView) getView().findViewById(R.id.capture_payment_details));


        payment_date = ((EditText) view.findViewById(R.id.payment_date));
        if(date != null){
            payment_date.setText(date);
        }

        payment_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment(ViewIndividualOrdersFragment.this);
                dialog.show(activity.getSupportFragmentManager(), DIALOG_DATE);
            }
        });


        builder.setView(view);

//        ((EditText) view.findViewById(R.id.payment_date)).setText(title);
      Spinner paymentMethod = ((Spinner) view.findViewById(R.id.sp_payment_method));
      if(method != null){
          String[] paymentMethods = getActivity().getResources().getStringArray(R.array.sp_payment_method);
          for(int i=0; i<paymentMethods.length; i++){
              String item = paymentMethods[i];
              if(item.equalsIgnoreCase(method)){
                  paymentMethod.setSelection(i);
                  break;
              }

          }
      }

//
        EditText txt_amount =  ((EditText) view.findViewById(R.id.txt_amount));
        if(amount != null){
            txt_amount.setText(amount);
        }

//
        EditText txt_reference =  ((EditText) view.findViewById(R.id.txt_reference));
        if(reference != null){
            txt_reference.setText(reference);
        }
//        ((ImageView) view.findViewById(R.id.imageIcon)).setBackground(getResources().getDrawable(status ? R.drawable.title_success_background : R.drawable.title_error_background ));


        final android.app.AlertDialog alertDialog = builder.create();
        //Alvin asked to be removed
        //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        view.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //initTimePicker();
            }
        });

        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(payment_date.getText().toString())) {
                    payment_date.setError("This field cannot be left empty");
                    return;
                } else {
                    payment_date.setError(null);
                }

                if (TextUtils.isEmpty(paymentMethod.getSelectedItem().toString())) {
                    Toast.makeText(activity, "This field cannot be left empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(txt_amount.getText().toString())) {
                    txt_amount.setError("This field cannot be left empty");
                    return;
                } else {

                    try {
                        Integer.valueOf(txt_amount.getText().toString());
                    } catch (Exception ex) {
                        txt_amount.setError("Enter a number for amount!");
                        return;
                    }
                    txt_amount.setError(null);
                }

                alertDialog.dismiss();

                if (method != null && amount != null && reference != null && date != null && paymentId != null){
                    if(!TextUtils.isEmpty(paymentId))
                        updatePayment(payment_date.getText().toString(), paymentMethod.getSelectedItem().toString(), txt_reference.getText().toString(), txt_amount.getText().toString(), paymentId);
                }else{
                    postPayment(payment_date.getText().toString(), paymentMethod.getSelectedItem().toString(), txt_reference.getText().toString(), txt_amount.getText().toString());
                }
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }


    private void postUpdateCompleteOrder(String paymentdate){
        if(authenticationResponse.isUserLogededIn()) {
            ApiService apiService =
                    ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

            Bundle args = getArguments();
            final PendingOrderContent orderContent = args != null ? (PendingOrderContent) args.getSerializable(EXTRA_TEXT) : null;
            if(orderContent != null){
                Map<String, String> request = new HashMap<>();
                request.put("orderStatus", "Completed");
                request.put("orderId", orderContent.getOrderNo().toString());
                request.put("statusDate", paymentdate);

                final ProgressDialog pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Uploading data captured...");
                pDialog.setCancelable(false);
                pDialog.show();



                Call<Map<String,String>> call = apiService.updateOrderStatus(request);
                call.enqueue(new Callback<Map<String,String>>() {
                    @Override
                    public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                        if(response.isSuccessful()){
                            if(response.code() == 201 || response.code() == 200){

                                activity.showSuccessDialog("Success!", "Complete Order status has been submitted", "continue", true, "Pending Orders");
                                populatePaymentDetailsList(String.valueOf(orderContent.getOrderNo()));

                                //clearFields();

                            }else if(response.code() == 500){
                                try{
                                    String title = response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONObject("message").toString() : "Oops";

                                    String message = response.errorBody() != null ?
                                            new JSONObject(response.errorBody().string()).getJSONObject("exceptionMessage").toString() : "exceptionMessage";

                                    activity.showSuccessDialog(title, message, "try again", false, null);


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


    private void updatePayment(String paymentdate, String paymentMethod, String reference, String amount, String paymentId){
        if(authenticationResponse.isUserLogededIn()) {
            ApiService apiService =
                    ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

            Bundle args = getArguments();
            final PendingOrderContent orderContent = args != null ? (PendingOrderContent) args.getSerializable(EXTRA_TEXT) : null;
            if(orderContent != null){



                MakePaymentRequest request1 = new MakePaymentRequest(
                        paymentdate,
                        paymentMethod,
                        reference,
                        Integer.valueOf(amount),
                        orderContent.getOrderNo(),
                        paymentId
                );

                final ProgressDialog pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Uploading data captured...");
                pDialog.setCancelable(false);
                pDialog.show();



                Call<Map<String,String>> call = apiService.createPayment(request1);
                call.enqueue(new Callback<Map<String,String>>() {
                    @Override
                    public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                        if(response.isSuccessful()){
                            if(response.code() == 201 || response.code() == 200){

                                activity.showSuccessDialog("Success!", "Payment edit details have been submitted", "continue", true,  null);
                                populatePaymentDetailsList(String.valueOf(orderContent.getOrderNo()));

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


    private void postPayment(String paymentdate, String paymentMethod, String reference, String amount){
        if(authenticationResponse.isUserLogededIn()) {
            ApiService apiService =
                    ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

            Bundle args = getArguments();
            final PendingOrderContent orderContent = args != null ? (PendingOrderContent) args.getSerializable(EXTRA_TEXT) : null;
            if(orderContent != null){
                MakePaymentRequest request1 = new MakePaymentRequest(
                        paymentdate,
                        paymentMethod,
                        reference,
                        Integer.valueOf(amount),
                        orderContent.getOrderNo(),
                        null
                        );

                final ProgressDialog pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Uploading data captured...");
                pDialog.setCancelable(false);
                pDialog.show();



                Call<Map<String,String>> call = apiService.createPayment(request1);
                call.enqueue(new Callback<Map<String,String>>() {
                    @Override
                    public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                        if(response.isSuccessful()){
                            if(response.code() == 201 || response.code() == 200){

                                activity.showSuccessDialog("Success!", "Payment has been submitted", "continue", true, null);
                                populatePaymentDetailsList(String.valueOf(orderContent.getOrderNo()));

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

    private AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    private void  populateOrderProducts(Integer orderNumber){
        if(((MainActivity)getActivity()) != null){
           // progress_circular_category.setVisibility(View.VISIBLE);
            Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
            if(object!= null)
                if(((List<AuthenticationResponse>) object).size() > 0)
                    authenticationResponse = ((List<AuthenticationResponse>) object).get(0);

            if(authenticationResponse != null){
                ApiService apiService =
                        ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

                Call<List<OrderProductsDetailsResponse>> call = apiService.getOrderProductsDetails(String.valueOf(orderNumber));
                call.enqueue(new Callback<List<OrderProductsDetailsResponse>>() {
                    @Override
                    public void onResponse(Call<List<OrderProductsDetailsResponse>> call, Response<List<OrderProductsDetailsResponse>> response) {

                        if(response.isSuccessful()){
                            if(response.code() == 200){

//                                dataCategoryList.clear();
//                                dataCategoryList = response.body();

                                LinearLayoutManager horizontalLayoutManager
                                        = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                                recyclerViewProducts.setLayoutManager(horizontalLayoutManager);
                                OrderDetailsProductListAdapter  adapter = new OrderDetailsProductListAdapter(getActivity(), response.body());
                                //categoryAdapter.setClickListener(this);
                                recyclerViewProducts.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        //progress_circular_category.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<OrderProductsDetailsResponse>> call, Throwable t) {
                       // progress_circular_category.setVisibility(View.GONE);
                    }
                });
            }
        }
    }


    private RecyclerView recyclerViewPayments;

    private void populatePaymentDetailsList(String orderNumber){
        if(((MainActivity)getActivity()) != null){
            //progress_circular_category.setVisibility(View.VISIBLE);


            Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
            if(object!= null)
                if(((List<AuthenticationResponse>) object).size() > 0)
                    authenticationResponse = ((List<AuthenticationResponse>) object).get(0);

            if(authenticationResponse != null){
                ApiService apiService =
                        ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

                Call<List<OrderPaymentsListResponse>> call = apiService.getOrderPaymentsDetails(orderNumber);
                call.enqueue(new Callback<List<OrderPaymentsListResponse>>() {
                    @Override
                    public void onResponse(Call<List<OrderPaymentsListResponse>> call, Response<List<OrderPaymentsListResponse>> response) {

                        if(response.isSuccessful()){
                            if(response.code() == 200){

                                LinearLayoutManager horizontalLayoutManager
                                        = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                                recyclerViewPayments.setLayoutManager(horizontalLayoutManager);
                                OrderPaymentsListAdapter  adapter = new OrderPaymentsListAdapter(ViewIndividualOrdersFragment.this, response.body());
                                //categoryAdapter.setClickListener(this);
                                recyclerViewPayments.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }

                       // progress_circular_category.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<OrderPaymentsListResponse>> call, Throwable t) {
                       // progress_circular_category.setVisibility(View.GONE);
                    }
                });
            }
        }
    }


}
