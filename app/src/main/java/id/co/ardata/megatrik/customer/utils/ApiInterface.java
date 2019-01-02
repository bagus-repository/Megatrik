package id.co.ardata.megatrik.customer.utils;

import java.util.HashMap;
import java.util.List;

import id.co.ardata.megatrik.customer.model.Content;
import id.co.ardata.megatrik.customer.model.OAuthSecret;
import id.co.ardata.megatrik.customer.model.OAuthToken;
import id.co.ardata.megatrik.customer.model.Order;
import id.co.ardata.megatrik.customer.model.ServiceCategory;
import id.co.ardata.megatrik.customer.model.Transaction;
import id.co.ardata.megatrik.customer.model.TransactionReviewsItem;
import id.co.ardata.megatrik.customer.model.User;
import id.co.ardata.megatrik.customer.model.UserOperator;
import id.co.ardata.megatrik.customer.model.UserOrder;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    /**
     * List api user dan authnya
     * @return
     */
    @GET(ApiConfig.API_URL+"oauthclients/2")
    Call<OAuthSecret> getOAuthSecret();

    @FormUrlEncoded
    @POST(ApiConfig.BASE_URL+"oauth/token")
    Call<OAuthToken> getAccessToken(
            @FieldMap HashMap<String, String> params
    );

    @GET(ApiConfig.API_URL+"user")
    Call<User> getUser();

    @FormUrlEncoded
    @POST(ApiConfig.API_URL+"register")
    Call<User> storeUser(
            @FieldMap HashMap<String, String> params
    );

    @FormUrlEncoded
    @PATCH(ApiConfig.API_URL+"users/{id}")
    Call<User> updateUser(
            @Path("id") String id,
            @FieldMap HashMap<String, String> params
    );

    @GET(ApiConfig.API_URL+"users/{id}/customer_orders")
    Call<UserOrder> getUserOrder(
            @Path("id") String id
    );

    /**
     * Home Slider
     * @return
     */
    @GET(ApiConfig.API_URL+"contentlists/1")
    Call<Content> getContent();

    /**
     * Service category & servicelist
     * @return
     */
    @GET(ApiConfig.API_URL+"servicecategories")
    Call<List<ServiceCategory>> getServiceCategory();

    @GET(ApiConfig.API_URL+"servicecategories/{id}")
    Call<ServiceCategory> getServiceList(
            @Path("id") String id
    );

    /**
     * Order
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConfig.API_URL+"orders")
    Call<Order> storeOrder(
            @FieldMap HashMap<String, String> params
    );

    @GET(ApiConfig.API_URL+"orders/{id}")
    Call<id.co.ardata.megatrik.customer.model.order.Order> getOrder(
            @Path("id") String id
    );

    @GET(ApiConfig.API_URL+"orders/{id}/{city_name}")
    Call<UserOperator> getOrderStatus(
            @Path("id") String id,
            @Path(value="city_name") String city_name
    );

    /**
     * Transaction
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(ApiConfig.API_URL+"transactions")
    Call<Transaction> storeTransaction(
            @FieldMap HashMap<String, String> params
    );

    @FormUrlEncoded
    @POST(ApiConfig.API_URL+"transactionreviews")
    Call<TransactionReviewsItem> storeTransactionReview(
            @FieldMap HashMap<String, String> params
    );

    @GET(ApiConfig.API_URL+"transactions/{id}")
    Call<Transaction> getTransactionReview(
            @Path("id") String transaction_id
    );
}
