package id.co.ardata.megatrik.customer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
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

public class PaymentTransferActivity extends AppCompatActivity {

    int order_id;

    @BindView(R.id.etSenderName)
    EditText etSenderName;
    @BindView(R.id.etBankName)
    EditText etBankName;
    @BindView(R.id.etSenderBankAccount)
    EditText etSenderBankAccount;
    @BindView(R.id.etNominal)
    EditText etNominal;
    @BindView(R.id.btConfirm)
    Button btConfirm;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Context mContext;
    ApiInterface apiInterface;
    KProgressHUD progressHUD;
    AwesomeValidation awesomeValidation;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_transfer);
        ButterKnife.bind(this);

        toolbar.setTitle("Detail transfer");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = this;
        gson = new Gson();
        order_id = getIntent().getIntExtra("order_id", 0);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        if (order_id == 0){
            Tools.Tshort(mContext, "Order Id gagal didapatkan");
            finish();
        }

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay_with_transfer();
            }
        });
    }

    private void pay_with_transfer() {
        awesomeValidation.clear();

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        awesomeValidation.addValidation(etSenderName, RegexTemplate.NOT_EMPTY, "Nama pengirim tidak valid");
        awesomeValidation.addValidation(etBankName, RegexTemplate.NOT_EMPTY, "Bank tujuan tidak valid");
        awesomeValidation.addValidation(etSenderBankAccount, RegexTemplate.NOT_EMPTY, "Rekening pengirim tidak valid");
        awesomeValidation.addValidation(etNominal, RegexTemplate.NOT_EMPTY, "Nominal tidak valid");

        if (awesomeValidation.validate()){
            apiInterface = ApiClient.getApiClient(mContext, true);

            HashMap<String, String> params = new HashMap<>();
            params.put("order_id", String.valueOf(order_id));
            params.put("payment_type", "transfer");
            params.put("name", etSenderName.getText().toString());
            params.put("bank_name", etBankName.getText().toString());
            params.put("bank_account", etSenderBankAccount.getText().toString());
            params.put("payment_value", etNominal.getText().toString());

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
                                .setMessage("Pembayaran dengan transfer berhasil")
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
        }else {
            progressHUD.dismiss();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
