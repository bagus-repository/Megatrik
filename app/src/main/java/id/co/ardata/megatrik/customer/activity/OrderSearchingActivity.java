package id.co.ardata.megatrik.customer.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.model.UserOperator;
import id.co.ardata.megatrik.customer.utils.ApiClient;
import id.co.ardata.megatrik.customer.utils.ApiInterface;
import id.co.ardata.megatrik.customer.utils.SessionManager;
import id.co.ardata.megatrik.customer.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSearchingActivity extends AppCompatActivity {

    private int order_id;
    String city_name;

    @BindView(R.id.txtMessage)
    TextView txtMessage;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.btnSearch)
    Button btnSearch;
    @BindView(R.id.btnRetry)
    Button btnRetry;

    Context mContext;
    private int max_check_request = 5;
    private Handler handler;
    private Runnable runnable;
    private ApiInterface apiInterface;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_searching);
        ButterKnife.bind(this);

        mContext = this;
        sessionManager = new SessionManager(mContext);

        Intent fromIntent = getIntent();
        order_id = fromIntent.getIntExtra("order_id", 0);
        city_name = fromIntent.getStringExtra("city_name");

        txtMessage.setText("Sedang mencari teknisi terdekat...");
        btnSearch.setVisibility(View.GONE);
        btnRetry.setVisibility(View.GONE);

        searching_techinician();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 5000);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtMessage.setText("Sedang mencari teknisi terdekat...");
                btnSearch.setVisibility(View.GONE);
                btnRetry.setVisibility(View.GONE);

                searching_techinician();
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 5000);
            }
        });
    }

    private void searching_techinician() {

        apiInterface = ApiClient.getApiClient(this, true);

        handler = new Handler();
        runnable = new Runnable() {
            int request_count = 1;
            @Override
            public void run() {
                //Call the api here again
                Call<UserOperator> call = apiInterface.getOrderStatus(String.valueOf(order_id), city_name);
//                Tools.Tshort(mContext, call.request().url().toString());
                call.enqueue(new Callback<UserOperator>() {
                    @Override
                    public void onResponse(Call<UserOperator> call, final Response<UserOperator> response) {
                        if (response.isSuccessful()){
                            if (response.body() == null){
                                txtMessage.setText("Yeay... Selamat kami menemukan teknisi untuk anda");
                                btnSearch.setText("OK");
                                btnSearch.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent toMain = new Intent(OrderSearchingActivity.this, MainActivity.class);
                                        toMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(toMain);
                                        finish();
                                    }
                                });
                                btnSearch.setVisibility(View.VISIBLE);
                                handler.removeCallbacks(runnable);
                            }else {
                                if (request_count == max_check_request){
                                    handler.removeCallbacks(runnable);

                                    txtMessage.setText("Maaf... Kami belum menemukan teknisi untuk anda, silahkan menghubungi koordinator lapangan kami");
                                    btnSearch.setText("Hubungi");
                                    btnSearch.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try{
                                                Intent toWa = new Intent();
                                                toWa.setAction(Intent.ACTION_VIEW);
                                                String api_url = "https://api.whatsapp.com/send?phone="+response.body().getNoHp()+"&text=OrderId "+order_id+" tidak menemukan teknisi. Mohon bantuan.";
                                                Tools.Tshort(mContext, api_url);
                                                toWa.setPackage("com.whatsapp");
                                                toWa.setData(Uri.parse(api_url));
                                                toWa.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(toWa);
                                            }catch (Exception e){
                                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                                intent.setData(Uri.parse("tel:"+response.body().getNoHp()));
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                    btnSearch.setVisibility(View.VISIBLE);
                                    btnRetry.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserOperator> call, Throwable t) {
                        t.printStackTrace();
                        Tools.Tshort(mContext, getString(R.string.error_connect_server));
                    }
                });
                request_count++;
                handler.postDelayed(runnable, 5000);
            }
        };
    }
}
