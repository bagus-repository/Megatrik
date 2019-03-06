package id.co.ardata.megatrik.customer.utils;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import id.co.ardata.megatrik.customer.activity.LoginActivity;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static Retrofit retrofit;

    public static ApiInterface getApiClient(final Context context, final boolean token){
        final SessionManager sessionManager = new SessionManager(context);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request;
                Response response;

                if (token){
                    request = chain.request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer "+sessionManager.getAccessToken())
                            .addHeader("Accept", "application/json")
                            .build();
                }else{
                    request = chain.request()
                            .newBuilder()
                            .addHeader("Accept", "application/json")
                            .build();
                }
                response = chain.proceed(request);
                if (response.code() == 403 && token){
                    sessionManager.resetProfile();
                    Intent toLogin = new Intent(context, LoginActivity.class);
                    toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(toLogin);
                }
                return response;
            }
        })
                .connectTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(ApiInterface.class);
    }
}
