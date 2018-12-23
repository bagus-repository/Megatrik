package id.co.ardata.megatrik.customer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.model.ErrorApiMsg;
import id.co.ardata.megatrik.customer.model.Order;
import id.co.ardata.megatrik.customer.model.ServiceCategory;
import id.co.ardata.megatrik.customer.model.ServicelistsItem;
import id.co.ardata.megatrik.customer.utils.ApiClient;
import id.co.ardata.megatrik.customer.utils.ApiInterface;
import id.co.ardata.megatrik.customer.utils.SessionManager;
import id.co.ardata.megatrik.customer.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private int service_cat_id;
    private static final int PICK_ADDRESS_REQUEST_CODE = 1;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.etService)
    EditText etService;
    @BindView(R.id.etDeskripsi)
    EditText etDeskripsi;
    @BindView(R.id.etAlamat)
    EditText etAlamat;
    @BindView(R.id.etKeteranganAlamat)
    EditText etKeterangan;
    @BindView(R.id.btOrder)
    Button btOrder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Context mContext;
    Gson gson;
    KProgressHUD progressHUD;
    AwesomeValidation awesomeValidation;
    double latitude, longitude;
    String city_name;
    ApiInterface apiInterface;
    SessionManager sessionManager;
    List<ServicelistsItem> services;
    String services_option[];
    int selected_service;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        Intent fromIntent = getIntent();
        service_cat_id = fromIntent.getIntExtra("service_category_id", 0);
        tvTitle.setText(fromIntent.getStringExtra("service_category_name"));

        toolbar.setTitle("Formulir Order");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        sessionManager = new SessionManager(mContext);
        gson = new Gson();

        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        AwesomeValidation.disableAutoFocusOnFirstFailure();

        btOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_order();
            }
        });

        get_services(service_cat_id);

        etService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_dialog_service(view);
            }
        });

        etAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPickAddress = new Intent(OrderActivity.this, PickAddressActivity.class);
                startActivityForResult(toPickAddress, PICK_ADDRESS_REQUEST_CODE);
            }
        });

    }

    private void show_dialog_service(final View view) {
        if (services_option.length > 0){
            builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Layanan");
            builder.setSingleChoiceItems(services_option, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((EditText) view).setText(services_option[i]);
                    selected_service = services.get(i).getId();
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }else {
            Tools.Tshort(mContext, "Gagal load service");
        }
    }

    private void get_services(int service_cat_id) {

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        apiInterface = ApiClient.getApiClient(mContext, true);

        Call<ServiceCategory> call = apiInterface.getServiceList(String.valueOf(service_cat_id));
        call.enqueue(new Callback<ServiceCategory>() {
            @Override
            public void onResponse(Call<ServiceCategory> call, Response<ServiceCategory> response) {
                progressHUD.dismiss();
                if (response.isSuccessful()){
                    services = response.body().getServicelists();
                    services_option = new String[services.size()];
                    for (int i = 0; i < services.size(); i++) {
                        services_option[i] = services.get(i).getName();
                    }
                }else {
                    ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                    Tools.Tshort(mContext, errorApiMsg.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ServiceCategory> call, Throwable t) {
                progressHUD.dismiss();
                t.printStackTrace();
                Tools.Tshort(mContext, getString(R.string.error_connect_server));
            }
        });
    }

    private void submit_order() {
        awesomeValidation.clear();

        awesomeValidation.addValidation(etService, RegexTemplate.NOT_EMPTY, "Jenis layanan tidak valid");
        awesomeValidation.addValidation(etAlamat, RegexTemplate.NOT_EMPTY, "Alamat tidak valid");
        awesomeValidation.addValidation(etDeskripsi, RegexTemplate.NOT_EMPTY, "Deskripsi tidak valid");
        awesomeValidation.addValidation(etKeterangan, RegexTemplate.NOT_EMPTY, "Keterangan alamat tidak valid");

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        if (awesomeValidation.validate()){
            apiInterface = ApiClient.getApiClient(this, true);

            HashMap<String, String> params = new HashMap<>();
            params.put("customer_id", sessionManager.getUserId());
            params.put("latitude", String.valueOf(latitude));
            params.put("longitude", String.valueOf(longitude));
            params.put("address", etAlamat.getText().toString()+"("+etKeterangan.getText().toString()+")");
            params.put("city_name", city_name);
            params.put("description", etDeskripsi.getText().toString());
            params.put("service_list_id", String.valueOf(selected_service));

            Call<Order> call = apiInterface.storeOrder(params);
            call.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    progressHUD.dismiss();
                    if (response.isSuccessful()){
                        int order_id = response.body().getId();

                        Intent toOrderSeraching = new Intent(OrderActivity.this, OrderSearchingActivity.class);
                        toOrderSeraching.putExtra("order_id", order_id);
                        toOrderSeraching.putExtra("city_name", city_name);
                        startActivity(toOrderSeraching);
                    }else {
                        ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                        Tools.Tshort(mContext, errorApiMsg.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    progressHUD.dismiss();
                    t.printStackTrace();
                    Tools.Tshort(mContext, getString(R.string.error_connect_server));
                }
            });
        }else{
            progressHUD.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_ADDRESS_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                if (data != null) {
                    etAlamat.setText(data.getStringExtra("alamat"));
                    city_name = data.getStringExtra("city_name");
                    latitude = data.getDoubleExtra("lat", 0.0);
                    longitude = data.getDoubleExtra("lng", 0.0);
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
