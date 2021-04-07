package com.bg.deliveryapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bg.deliveryapp.api.ApiService;
import com.bg.deliveryapp.api.ServiceGenerator;
import com.bg.deliveryapp.api.models.requests.AuthenticationRequest;
import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private EditText et_username;
    private EditText et_password;
    private RelativeLayout btn_login;
    private ProgressBar pb_sign_in_progress;
    private TextView btn_label_status;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity_layout);

        if(checkIfUserLoggedIn()){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                }
//            },2000);
        }




        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_label_status = findViewById(R.id.btn_label_status);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( validateFields(et_password) || validateFields(et_username)){
                    return;
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                loginUser(et_username.getText().toString().trim(), et_password.getText().toString().trim());

            }
        });

        pb_sign_in_progress = findViewById(R.id.pb_sign_in_progress);


    }

    private boolean checkIfUserLoggedIn(){
        AuthenticationResponse book  = new AuthenticationResponse();
        Object object = AuthenticationResponse.listAll(AuthenticationResponse.class);
        if(object!= null)
            if(((List<AuthenticationResponse>) object).size() > 0)
                book = ((List<AuthenticationResponse>) object).get(0);


        if(book != null)
            if(TextUtils.isEmpty(book.getBase64EncodedAuthenticationKey()) || TextUtils.isEmpty(book.getUsername()) || TextUtils.isEmpty(book.getPassword()) || TextUtils.isEmpty(book.getName()))
                return false;
            else
                return true;
        else
            return false;
    }


    private boolean validateFields(EditText editText){
        if(TextUtils.isEmpty(editText.getText().toString().trim())){
            editText.setError("This field cannot be blank");
            return true;
        }else{
            editText.setError(null);
            return false;
        }

    }


    private void loginUser(String username, String password){



        ApiService apiService =
                ServiceGenerator.createService(ApiService.class, username, password);
        final AuthenticationRequest request = new AuthenticationRequest(username,password);

        //CustomAlertDialog.showProgressDialog(LoginActivity.this,"Login confirmation...").show();
        pb_sign_in_progress.setVisibility(View.VISIBLE);
        btn_login.setEnabled(false);
        btn_login.setClickable(false);
        btn_label_status.setText("Loading...");

        Call<AuthenticationResponse> call = apiService.postAuthentication(request);
        call.enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                if(response.isSuccessful()){
                    if(response.code() == 201 || response.code() == 200){
                        //CustomAlertDialog.showSuccessDialog(LoginActivity.this,"Login confirmation","Login successful").show();

                        List<AuthenticationResponse> authenticationResponse1 = AuthenticationResponse.listAll(AuthenticationResponse.class);
                        if(authenticationResponse1 != null)
                            if(authenticationResponse1.size() > 0)
                                for(AuthenticationResponse authenticationResponse2 : authenticationResponse1)
                                    authenticationResponse2.delete();

                        AuthenticationResponse authenticationResponse = response.body();
                        authenticationResponse.setUsername(request.getUsername());
                        authenticationResponse.setPassword(request.getPassword());
                        authenticationResponse.save();

                        //FIXME: REMOVE THIS, DONE FOR TESTING
                        List<AuthenticationResponse> authenticationResponse3 = AuthenticationResponse.listAll(AuthenticationResponse.class);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    }else if(response.code() == 500){
                        try{
                            String title = response.errorBody() != null ?
                                    new JSONObject(response.errorBody().string()).getJSONObject("message").toString() : "Oops";

                            String message = response.errorBody() != null ?
                                    new JSONObject(response.errorBody().string()).getJSONObject("exceptionMessage").toString() : "exceptionMessage";
                            //CustomAlertDialog.showErrorDialog(title, message, LoginActivity.this).show();
                        }catch (Exception e){
                            //CustomAlertDialog.showErrorDialog("Oops", e.getMessage(), LoginActivity.this).show();
                        }

                    }

                    pb_sign_in_progress.setVisibility(View.GONE);
                    btn_login.setEnabled(true);
                    btn_login.setClickable(true);
                    btn_label_status.setText("Login successful");

                   // CustomAlertDialog.showProgressDialog(LoginActivity.this,"Login confirmation...").dismiss();
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                btn_login.setEnabled(true);
                btn_login.setClickable(true);
                btn_label_status.setText("SIGN IN");
                pb_sign_in_progress.setVisibility(View.GONE);
                t.printStackTrace();
                //CustomAlertDialog.showProgressDialog(LoginActivity.this,"Login confirmation...").dismiss();
                //CustomAlertDialog.showErrorDialog("Oops", t.getMessage(), LoginActivity.this);
            }
        });

    }


}
