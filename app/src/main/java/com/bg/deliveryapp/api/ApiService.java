package com.bg.deliveryapp.api;

import com.bg.deliveryapp.api.models.requests.AuthenticationRequest;
import com.bg.deliveryapp.api.models.requests.ClientRequest;
import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;
import com.bg.deliveryapp.api.models.responses.ViewCustomerResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.AreaSubResponse;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    //Test url is http://34.72.8.114:8080/deliverytest/login
    @POST("/deliverytest/login")
    Call<AuthenticationResponse> postAuthentication(@Body AuthenticationRequest request);

    @GET("/deliverytest/api/v1/clients/searchcustomers")//searchcustomers?term=peter&pageNumber=1&size=10
    Call<ViewCustomerResponse>  getCustomers(@Query("term") String term, @Query("pageNumber") Integer pageNumber, @Query("size") Integer size);

    @GET("/deliverytest/api/v1/clients/listareas")
    Call<ArrayList<AreaSubResponse>> getAreas();

    @POST("/deliverytest/api/v1/clients/createclient")
    Call<ResponseBody> createCustomer(@Body ClientRequest request);





}
