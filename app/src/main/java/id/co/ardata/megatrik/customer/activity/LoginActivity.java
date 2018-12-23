package id.co.ardata.megatrik.customer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.onesignal.OneSignal;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.model.ErrorApiMsg;
import id.co.ardata.megatrik.customer.model.OAuthSecret;
import id.co.ardata.megatrik.customer.model.OAuthToken;
import id.co.ardata.megatrik.customer.model.User;
import id.co.ardata.megatrik.customer.utils.ApiClient;
import id.co.ardata.megatrik.customer.utils.ApiInterface;
import id.co.ardata.megatrik.customer.utils.SessionManager;
import id.co.ardata.megatrik.customer.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.tietEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.tietPassword)
    TextInputEditText tietPassword;
    @BindView(R.id.btSignIn)
    Button btSignIn;
    @OnClick(R.id.tvSignUp)
    public void gotTo(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.tvReset)
    public void goTo(){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    String secret_token;
    ApiInterface apiInterface;
    Context mContext;
    AwesomeValidation awesomeValidation;
    KProgressHUD progressHUD;
    SessionManager sessionManager;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mContext = this;
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        sessionManager = new SessionManager(this);
        gson = new Gson();

        get_secret_token();
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
    }

    private void get_secret_token() {
        apiInterface = ApiClient.getApiClient(this, false);

        Call<OAuthSecret> call = apiInterface.getOAuthSecret();
        call.enqueue(new Callback<OAuthSecret>() {
            @Override
            public void onResponse(Call<OAuthSecret> call, Response<OAuthSecret> response) {
                if (response.isSuccessful()){
                    secret_token = response.body().getSecret();
                }else {
                    ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                    Tools.Tshort(mContext, errorApiMsg.getMessage());
                }
            }

            @Override
            public void onFailure(Call<OAuthSecret> call, Throwable t) {
                t.printStackTrace();
                Tools.Tshort(mContext, getString(R.string.error_connect_server));
                call.clone();
            }
        });
    }

    private void doLogin() {
        awesomeValidation.clear();

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        awesomeValidation.addValidation(tietEmail, Patterns.EMAIL_ADDRESS, "Email tidak valid");
        awesomeValidation.addValidation(tietPassword, RegexTemplate.NOT_EMPTY, "Password tidak valid");

        if (awesomeValidation.validate() && secret_token != null){
            apiInterface = ApiClient.getApiClient(this, false);

            HashMap<String, String> params = new HashMap<>();
            params.put("grant_type", "password");
            params.put("client_id", "4");
            params.put("client_secret", secret_token);
            params.put("username", tietEmail.getText().toString());
            params.put("password", tietPassword.getText().toString());

            Call<OAuthToken> call = apiInterface.getAccessToken(params);
            call.enqueue(new Callback<OAuthToken>() {
                @Override
                public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
                    progressHUD.dismiss();
                    if (response.isSuccessful()){
                        sessionManager.setAccessToken(response.body().getAccessToken());
                        apiInterface = ApiClient.getApiClient(mContext, true);

                        Call<User> userCall = apiInterface.getUser();
                        userCall.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()){
                                    sessionManager.setIsLogin(true);
                                    sessionManager.setUserName(response.body().getName());
                                    sessionManager.setUserEmail(response.body().getEmail());
                                    sessionManager.setUserId(String.valueOf(response.body().getId()));
                                    OneSignal.setSubscription(true);
                                    update_os_player_id();
                                    Tools.Tshort(mContext, "Yeaay!");

                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                                    Tools.Tshort(mContext, errorApiMsg.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                t.printStackTrace();
                                Tools.Tshort(mContext, getString(R.string.error_connect_server));
                            }
                        });
                    }else {
                        ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                        Tools.Tshort(mContext, errorApiMsg.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<OAuthToken> call, Throwable t) {
                    progressHUD.dismiss();
                    t.printStackTrace();
                    Tools.Tshort(mContext, getString(R.string.error_connect_server));
                }
            });
        }else {
            progressHUD.dismiss();
            Tools.Tshort(mContext, "Secret gagal didapatkan");
        }
    }

    private void update_os_player_id() {
        apiInterface = ApiClient.getApiClient(mContext, true);

        HashMap<String, String> params = new HashMap<>();
        params.put("os_player_id", sessionManager.getUserOsPlayerId());

        Call<User> call = apiInterface.updateUser(sessionManager.getUserId(), params);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    //sukses
                }else{
                    ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                    Tools.Tshort(mContext, errorApiMsg.getMessage());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                Tools.Tshort(mContext, getString(R.string.error_connect_server));
            }
        });
    }
}
