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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bg.deliveryapp.R;
import com.bg.deliveryapp.api.ApiService;
import com.bg.deliveryapp.api.ServiceGenerator;
import com.bg.deliveryapp.api.models.requests.ClientRequest;
import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.AreaSubResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
//import com.bg.deliveryapp.dialog.CustomAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomerFragment extends Fragment {


    private EditText et_customer_name,et_contact_person,et_telephone_number,et_email_address,et_location;
    private Spinner sp_customer_type,sp_area;
    private ProgressBar pb_area;
    private Button btn_save;
    
    private boolean validateFields(EditText editText){
        if(TextUtils.isEmpty(editText.getText().toString().trim())){
            editText.setError("This field cannot be blank");
            return true;
        }else{
            editText.setError(null);
            return false;
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
                    Integer.valueOf(String.valueOf(sp_customer_type.getSelectedItemId()))
            );

           final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Uploading data captured...");
            pDialog.setCancelable(false);
            pDialog.show();

           final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

            Call<ResponseBody> call = apiService.createCustomer(request);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        if(response.code() == 201 || response.code() == 200){

                            alert.setMessage("Upload successful");
                            //alert.setMessage("")
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert.show();
                        }else if(response.code() == 500){
                            try{
                                String title = response.errorBody() != null ?
                                        new JSONObject(response.errorBody().string()).getJSONObject("message").toString() : "Oops";

                                String message = response.errorBody() != null ?
                                        new JSONObject(response.errorBody().string()).getJSONObject("exceptionMessage").toString() : "exceptionMessage";

                                alert.setTitle(title);
                                alert.setMessage(message);
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();

                            }catch (Exception e){
                                alert.setTitle("Failure to upload");
                                alert.setMessage(e.getMessage());
                                alert.show();
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
                                    alert.setTitle(errorJson.has("message") ? errorJson.get("message").toString() : "");
                                    if (jsonDetails.has("code") && jsonDetails.has("message"))
                                        alert.setMessage(jsonDetails.get("code").toString() + " " + jsonDetails.get("message").toString());
                                    else {
                                        alert.setMessage(errorJson.get("message").toString());
                                        alert.setTitle("System error occurred!");
                                    }

                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alert.show();
                                }

                            }else{
                                alert.setTitle("System error occurred!");
                                alert.setMessage(errorJson.has("message") ? errorJson.get("message").toString() : "");
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                            }

                        }catch (Exception ex){
                            alert.setMessage(ex.getMessage());
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert.show();
                        }


                    }

                    pDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    alert.setTitle("Failure to upload");
                    alert.setMessage(t.getMessage());
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                    pDialog.dismiss();
                }
            });
        }

    }


    private static final String EXTRA_TEXT = "text";

    public static AddCustomerFragment createFor(String text) {
        AddCustomerFragment fragment = new AddCustomerFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_customer_fragment_layout, container, false);
    }


    AuthenticationResponse authenticationResponse;
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
//        final String text = args != null ? args.getString(EXTRA_TEXT) : "";
//        TextView textView = view.view.findViewById(R.id.text);
//        textView.setText(text);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
//            }
//        });


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
        sp_area = view.findViewById(R.id.sp_area);
        pb_area = view.findViewById(R.id.pb_area);

        btn_save =view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                if(validateFields(et_customer_name) || validateFields(et_contact_person) ||validateFields(et_telephone_number) ||validateFields(et_email_address) ||validateFields(et_location)){
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




                postCustomer();

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
}
