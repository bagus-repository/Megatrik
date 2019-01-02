package id.co.ardata.megatrik.customer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.model.ErrorApiMsg;
import id.co.ardata.megatrik.customer.model.Transaction;
import id.co.ardata.megatrik.customer.utils.ApiClient;
import id.co.ardata.megatrik.customer.utils.ApiInterface;
import id.co.ardata.megatrik.customer.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements RadioButton.OnCheckedChangeListener {

    @BindView(R.id.rbTransfer)
    RadioButton rbTransfer;
    @BindView(R.id.rbCash)
    RadioButton rbCash;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String payment_method;
    int order_id;
    KProgressHUD progressHUD;
    Context mContext;
    ApiInterface apiInterface;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        toolbar.setTitle("Pilih metode pembayaran");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        gson = new Gson();
        order_id = getIntent().getIntExtra("order_id",0);
        payment_method = "";

        rbTransfer.setOnCheckedChangeListener(this);
        rbCash.setOnCheckedChangeListener(this);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (order_id != 0){
                    payment_confirmation();
                }else{
                    Tools.Tshort(mContext, "Id Order tidak tersedia");
                    finish();
                }
            }
        });
    }

    private void payment_confirmation() {
        switch (payment_method){
            case "transfer":
                Intent intent = new Intent(this, PaymentTransferActivity.class);
                intent.putExtra("order_id",order_id);
                startActivity(intent);
                finish();
                break;
            case "cash" :
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                    .setTitle("Info")
                    .setMessage("Yakin bayar dengan cash ?")
                    .addButton("Ya", getResources().getColor(R.color.white), getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            pay_with_cash();
                        }
                    })
                    .addButton("Tidak", getResources().getColor(R.color.white), getResources().getColor(R.color.cfdialog_button_black_text_color), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                builder.show();
                break;
                default:
                    Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show();
                    break;
        }
    }

    private void pay_with_cash() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        apiInterface = ApiClient.getApiClient(mContext, true);

        HashMap<String, String> params = new HashMap<>();
        params.put("order_id", String.valueOf(order_id));
        params.put("payment_type", "cash");

        Call<Transaction> call = apiInterface.storeTransaction(params);
        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, final Response<Transaction> response) {
                progressHUD.dismiss();
                if (response.isSuccessful()){
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mContext)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                        .setTitle("Info")
                        .setCancelable(false)
                        .setMessage("Pembayaran dengan cash berhasil")
                        .addButton("OK", getResources().getColor(R.color.white), getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent toReview = new Intent(mContext, ReviewActivity.class);
                                toReview.putExtra("transaction_id", response.body().getId());
                                startActivity(toReview);
                                finish();
                            }
                        });
                    builder.show();
                }else {
                    ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                    Tools.Tshort(mContext, errorApiMsg.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                progressHUD.dismiss();
                t.printStackTrace();
                Tools.Tshort(mContext, getString(R.string.error_connect_server));
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            if (buttonView.getId() == rbTransfer.getId()){
                rbCash.setChecked(false);
                payment_method = "transfer";
            }
            if (buttonView.getId() == rbCash.getId()){
                rbTransfer.setChecked(false);
                payment_method = "cash";
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
