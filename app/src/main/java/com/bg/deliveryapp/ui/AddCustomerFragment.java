package com.bg.deliveryapp.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bg.deliveryapp.MainActivity;
import com.bg.deliveryapp.R;
import com.bg.deliveryapp.api.ApiService;
import com.bg.deliveryapp.api.ServiceGenerator;
import com.bg.deliveryapp.api.models.requests.ClientRequest;
import com.bg.deliveryapp.api.models.requests.EditCustomerRequest;
import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.AreaSubResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.ClientSubResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.PendingOrderContent;
import com.google.android.material.snackbar.Snackbar;
//import com.bg.deliveryapp.dialog.CustomAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomerFragment extends Fragment {


    private EditText et_customer_name,et_contact_person,et_telephone_number,et_email_address,et_location;
    private Spinner sp_customer_type,sp_area;
    private ProgressBar pb_area;
    private Button btn_save;
    
    private boolean validateFields(EditText editText, boolean isEmail, boolean isPhoneNumber, boolean mandatory){
        if(TextUtils.isEmpty(editText.getText().toString().trim())){
            if(mandatory){
                editText.setError("This field cannot be blank");
                return true;
            }else{
                editText.setError(null);
                return false;
            }

        }else{

            if(isPhoneNumber){
                 if(isPhonenumberValid(editText.getText().toString())){
                     editText.setError(null);
                     return false;

                 }else{
                     editText.setError("Enter a valid Phone number");
                     return true;
                 }
            }

            if(isEmail){
                 if(isEmailValid(editText.getText().toString())){
                     editText.setError(null);
                     return false;
                 }else{
                     editText.setError("Enter a valid Email address");
                     return true;
                 }
            }

            editText.setError(null);
            return false;

        }

    }

     boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    boolean isPhonenumberValid(String phonenumber){
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(phonenumber);
        return (matcher.matches());
    }


    private void postEditCustomer(){
        if(authenticationResponse.isUserLogededIn()) {
            ApiService apiService =
                    ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

            final String[] values = getResources().getStringArray(R.array.sp_add_customer_type);
            Map<String, String> typesMap = new HashMap<>();
            typesMap.put(values[0], "T");
            typesMap.put(values[1], "H");
            typesMap.put(values[2], "I");
            typesMap.put(values[3], "R");
            typesMap.put(values[4], "S");
            typesMap.put(values[5], "C");
            typesMap.put(values[6], "In");


            EditCustomerRequest request1 = new EditCustomerRequest(
                    customerResponse.getClientId(),
                    et_customer_name.getText().toString().split(" ")[0],
                    et_customer_name.getText().toString().split(" ")[1],
                    et_contact_person.getText().toString(),
                    typesMap.get(sp_customer_type.getSelectedItem().toString()),
                    et_email_address.getText().toString(),
                    et_telephone_number.getText().toString(),
                    "",
                    "",
                    et_location.getText().toString(),
                    Integer.valueOf(String.valueOf(sp_area.getSelectedItemId()))
            );

            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Uploading data captured...");
            pDialog.setCancelable(false);
            pDialog.show();



            Call<ResponseBody> call = apiService.editCustomer(request1);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        if(response.code() == 201 || response.code() == 200){


                            activity.showSuccessDialog("Success!", "Customer edit details have been submitted", "continue", true, "View Customers");

                            clearFields();

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
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    String title = ("Failure!");
                    String message = t.getMessage();
                    activity.showSuccessDialog(title, message, "try again", false,null);
                }
            });




        }
    }


    
    private void postCustomer(){
        if(authenticationResponse.isUserLogededIn()){
            ApiService apiService =
                    ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

            final String[] values = getResources().getStringArray(R.array.sp_add_customer_type);
            Map<String,String> typesMap = new HashMap<>();
            typesMap.put(values[0], "T");
            typesMap.put(values[1], "H");
            typesMap.put(values[2], "I");
            typesMap.put(values[3], "R");
            typesMap.put(values[4], "S");
            typesMap.put(values[5], "C");
            typesMap.put(values[6], "In");


            ClientRequest request = new ClientRequest(null,
                    et_customer_name.getText().toString().split(" ")[0],
                    et_customer_name.getText().toString().split(" ")[1],
                    et_contact_person.getText().toString(),
                    typesMap.get(sp_customer_type.getSelectedItem().toString()),
                    et_email_address.getText().toString(),
                    et_telephone_number.getText().toString(),
                    "",
                    "",
                    et_location.getText().toString(),
                    Integer.valueOf(String.valueOf(sp_area.getSelectedItemId()))
            );

           final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Uploading data captured...");
            pDialog.setCancelable(false);
            pDialog.show();



            Call<ResponseBody> call = apiService.createCustomer(request);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        if(response.code() == 201 || response.code() == 200){


                            activity.showSuccessDialog("Success!", "Customer has been submitted", "continue", true, "View Customers");

                            clearFields();

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
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    String title = ("Failure!");
                    String message = t.getMessage();
                    activity.showSuccessDialog(title, message, "try again", false,null);
                }
            });
        }

    }


    private static final String EXTRA_TEXT = "text";

    public static AddCustomerFragment createFor(Object message) {
        AddCustomerFragment fragment = new AddCustomerFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TEXT, (Serializable) message);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_customer_fragment_layout, container, false);
    }

    private MainActivity activity;


    AuthenticationResponse authenticationResponse;

    private ClientSubResponse customerResponse;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {



        activity = (MainActivity) getActivity();

         authenticationResponse  = new AuthenticationResponse();
        Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
        if(object!= null)
            if(((List<AuthenticationResponse>) object).size() > 0)
                authenticationResponse = ((List<AuthenticationResponse>) object).get(0);


        et_customer_name = view.findViewById(R.id.et_customer_name);
        et_contact_person = view.findViewById(R.id.et_contact_person);
        et_telephone_number = view.findViewById(R.id.et_telephone_number);
        et_email_address = view.findViewById(R.id.et_email_address);
        et_location = view.findViewById(R.id.et_location);

        sp_customer_type = view.findViewById(R.id.sp_customer_type);
        sp_customer_type.setSelection(2);
        sp_customer_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getSelectedItem().toString();
                if(item.equalsIgnoreCase("Individual")){
                    et_contact_person.setVisibility(View.GONE);
                }
                else if(item.equalsIgnoreCase("Type")){
                    et_contact_person.setVisibility(View.GONE);
                }else{
                    et_contact_person.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                et_contact_person.setVisibility(View.GONE);
            }
        });
        sp_area = view.findViewById(R.id.sp_area);
        pb_area = view.findViewById(R.id.pb_area);

        btn_save =view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                if(validateFields(et_customer_name, false,false,true) || validateFields(et_contact_person, false, true, false) ||validateFields(et_telephone_number, false,true,true) ||validateFields(et_email_address, true, false, false) ||validateFields(et_location, false, false, true)){
                    return;
                }else{
                        if(et_customer_name.getText().toString().split(" ").length == 1){
                            et_customer_name.setError("Provide your First Name and Last Name");
                            Snackbar.make(view.findViewById(R.id.add_customer_fragment_layout_ID), "Check the errors above", Snackbar.LENGTH_SHORT).show();
                            return;
                        }else{
                            et_customer_name.setError(null);
                        }
                }



                Bundle args = getArguments();
                customerResponse = args != null ? (ClientSubResponse) args.getSerializable(EXTRA_TEXT) : null;
                if(customerResponse != null){
                    postEditCustomer();
                }else{
                    postCustomer();
                }
            }
        });



        if(authenticationResponse.isUserLogededIn()){
            ApiService apiService =
                    ServiceGenerator.createService(ApiService.class, authenticationResponse.getUsername(), authenticationResponse.getPassword());

            pb_area.setVisibility(View.VISIBLE);
            Call<ArrayList<AreaSubResponse>> call = apiService.getAreas();
            call.enqueue(new Callback<ArrayList<AreaSubResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<AreaSubResponse>> call, Response<ArrayList<AreaSubResponse>> response) {
                    if(response.isSuccessful()){
                        if(response.code() == 200 || response.code() == 201){

                            sp_area.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, response.body()));
                            pb_area.setVisibility(View.GONE);


                            //populate data for edit
                            Bundle args = getArguments();
                            customerResponse = args != null ? (ClientSubResponse) args.getSerializable(EXTRA_TEXT) : null;
                            if(customerResponse != null){
                                et_customer_name.setText(customerResponse.getFirstname() + " " + customerResponse.getLastname());
                                if(!TextUtils.isEmpty(customerResponse.getContactPerson())){
                                    et_contact_person.setText(customerResponse.getContactPerson());
                                    et_contact_person.setVisibility(View.VISIBLE);
                                }
                                et_telephone_number.setText(customerResponse.getPhoneNumber());
                                et_email_address.setText(customerResponse.getEmail());
                                et_location.setText(customerResponse.getLocation());
                                try{
                                    sp_area.setSelection(customerResponse.getArea().getId());
                                }catch (Exception ex){

                                }
                                String[] customertype = getResources().getStringArray(R.array.sp_add_customer_type);

                                for(int i=0; i < customertype.length; i++){
                                    String item = customertype[i];
                                    if(item.equalsIgnoreCase(customerResponse.getType())){
                                        sp_customer_type.setSelection(i);
                                        break;
                                    }
                                }


                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<AreaSubResponse>> call, Throwable t) {
                    pb_area.setVisibility(View.GONE);
                }
            });
        }


    }



    private void clearFields() {
        et_customer_name.setText("");
        et_contact_person.setText("");
        et_telephone_number.setText("");
        et_email_address.setText("");
        et_location.setText("");
        //sp_area.setSelection(0);
        sp_customer_type.setSelection(2);
    }
}
