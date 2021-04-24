package com.bg.deliveryapp.api;

import com.bg.deliveryapp.api.models.requests.AuthenticationRequest;
import com.bg.deliveryapp.api.models.requests.ClientRequest;
import com.bg.deliveryapp.api.models.requests.CreateOrderRequest;
import com.bg.deliveryapp.api.models.requests.EditCustomerRequest;
import com.bg.deliveryapp.api.models.requests.MakePaymentRequest;
import com.bg.deliveryapp.api.models.responses.AuthenticationResponse;
import com.bg.deliveryapp.api.models.responses.ComputeTotalResponse;
import com.bg.deliveryapp.api.models.responses.CreateOrderCategoryResponse;
import com.bg.deliveryapp.api.models.responses.CreateOrderDeliveryResponse;
import com.bg.deliveryapp.api.models.responses.CreateOrderProductsResponse;
import com.bg.deliveryapp.api.models.responses.OrderPaymentsListResponse;
import com.bg.deliveryapp.api.models.responses.OrderProductsDetailsResponse;
import com.bg.deliveryapp.api.models.responses.PendingOrdersResponse;
import com.bg.deliveryapp.api.models.responses.ViewCustomerResponse;
import com.bg.deliveryapp.api.models.responses.subResponses.AreaSubResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @POST("/deliverytest/api/v1/clients/updateclient")
    Call<ResponseBody> editCustomer(@Body EditCustomerRequest request);

    @POST("/deliverytest/api/v1/orders/createorder")
    Call<Map<String,String>> createOrder(@Body CreateOrderRequest request);

    @GET("/deliverytest/api/v1/orders/pendingpaymentorders")//?pageNumber=1&size=10
    Call<PendingOrdersResponse> getPendingPaymentOrders(@Query("term") String term, @Query("pageNumber") Integer pageNumber, @Query("size") Integer size);

    @GET("deliverytest/api/v1/orders/pendingorders")//?pageNumber=1&size=10
    Call<PendingOrdersResponse> getPendingOrders(@Query("term") String term, @Query("pageNumber") Integer pageNumber, @Query("size") Integer size);

    @GET("/deliverytest/api/v1/orders/categories")
    Call<List<CreateOrderCategoryResponse>> getCategory();

    @GET("/deliverytest/api/v1/orders/deliverymethods")
    Call<ArrayList<CreateOrderDeliveryResponse>> getDelivery();

    @GET("/deliverytest/api/v1/orders/productsListing")
    Call<List<CreateOrderProductsResponse>> getProducts();

    @POST("/deliverytest/api/v1/orders/createpayment")
    Call<Map<String,String>> createPayment(@Body() MakePaymentRequest request);

    @POST("/deliverytest/api/v1/orders/updateorderstatus")
    Call<Map<String,String>> updateOrderStatus(@Body() Map<String,String> request);

//    @POST("/deliverytest/api/v1/orders/updatepayment")
//    Call<Map<String,String>> updatePayment(@Body() MakePaymentRequest request);


    @GET("/deliverytest/api/v1/orders/conmputeTotal")
    Call<ComputeTotalResponse> computeTotalWith(@Query("prodCode") String prodid, @Query("categoryId") String categoryid, @Query("method") String deliveryid);

    @GET("/deliverytest/api/v1/orders/orderdetails")
    Call<List<OrderProductsDetailsResponse>> getOrderProductsDetails(@Query("orderId")String orderId);

    @GET("/deliverytest/api/v1/orders/orderpayments")
    Call<List<OrderPaymentsListResponse>> getOrderPaymentsDetails(@Query("orderId")String orderId);







}
