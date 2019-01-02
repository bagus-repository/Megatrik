package id.co.ardata.megatrik.customer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.model.ErrorApiMsg;
import id.co.ardata.megatrik.customer.model.order.Order;
import id.co.ardata.megatrik.customer.utils.ApiClient;
import id.co.ardata.megatrik.customer.utils.ApiInterface;
import id.co.ardata.megatrik.customer.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    int order_id;

    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.tvOrderDescription)
    TextView tvOrderDescription;
    @BindView(R.id.tvOrderAddress)
    TextView tvOrderAddress;
    @BindView(R.id.tvTechnicianName)
    TextView tvTechnicianName;
    @BindView(R.id.tvOrderStatus)
    TextView tvOrderStatus;
    @BindView(R.id.tvOrderMaterials)
    TextView tvOrderMaterials;
    @BindView(R.id.tvOrderServices)
    TextView tvOrderServices;
    @BindView(R.id.tvOrderTotal)
    TextView tvOrderTotal;
    @BindView(R.id.tvOrderPaymentType)
    TextView tvOrderPaymentType;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    KProgressHUD progressHUD;
    ApiInterface apiInterface;
    Context mContext;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

        mContext = this;
        apiInterface = ApiClient.getApiClient(mContext, true);
        gson = new Gson();

        toolbar.setTitle("Order detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        order_id = getIntent().getIntExtra("order_id", 0);
        load_order();
    }

    private void load_order() {
        if (order_id == 0){
            Tools.Tshort(mContext, "Kesalahan, kembali ke halaman sebelumnya");
            finish();
        }

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        Call<Order> call = apiInterface.getOrder(String.valueOf(order_id));
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                progressHUD.dismiss();
                if (response.isSuccessful()){
                    tvOrderId.setText(String.valueOf(response.body().getId()));
                    tvOrderDescription.setText(response.body().getDescription());
                    tvOrderAddress.setText(response.body().getAddress());
                    tvTechnicianName.setText(response.body().getTechnician().getName());
                    tvOrderStatus.setText("Selesai");
                    String materials = "";
                    for (int i = 0; i < response.body().getMaterials().size(); i++) {
                        materials += response.body().getMaterials().get(i).getQuantity()+"x "+
                                response.body().getMaterials().get(i).getMateriallist().getName()+"\n";
                    }
                    tvOrderMaterials.setText(materials);
                    String services = "";
                    for (int i = 0; i < response.body().getServices().size(); i++) {
                        services += response.body().getServices().get(i).getServicelist().getName()+"\n";
                    }
                    tvOrderServices.setText(services);
                    tvOrderTotal.setText(String.valueOf(response.body().getTransaction().getTotal()));
                    tvOrderPaymentType.setText(response.body().getTransaction().getPaymentType().toUpperCase());
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
    }
}
