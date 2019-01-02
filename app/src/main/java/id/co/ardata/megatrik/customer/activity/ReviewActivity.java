package id.co.ardata.megatrik.customer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.model.ErrorApiMsg;
import id.co.ardata.megatrik.customer.model.Transaction;
import id.co.ardata.megatrik.customer.model.TransactionReviewsItem;
import id.co.ardata.megatrik.customer.utils.ApiClient;
import id.co.ardata.megatrik.customer.utils.ApiInterface;
import id.co.ardata.megatrik.customer.utils.SessionManager;
import id.co.ardata.megatrik.customer.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {

    int transaction_id;

    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.rtbReview)
    RatingBar rtbReview;
    @BindView(R.id.btReview)
    Button btReview;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    KProgressHUD progressHUD;
    ApiInterface apiInterface;
    SessionManager sessionManager;
    Context mContext;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        mContext = this;
        gson = new Gson();
        transaction_id = getIntent().getIntExtra("transaction_id", 0);
        boolean load = getIntent().getBooleanExtra("load", false);
        apiInterface = ApiClient.getApiClient(mContext, true);

        toolbar.setTitle("Review order");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (load){
            load_review();
        }
        btReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                do_review();
            }
        });
    }

    private void load_review() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Call<Transaction> call = apiInterface.getTransactionReview(String.valueOf(transaction_id));
        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                progressHUD.dismiss();
                if (response.isSuccessful()){
                    if (response.body().getTransactionreview() != null){
                        etDescription.setText(response.body().getTransactionreview().getDescription());
                        etDescription.setEnabled(false);
                        rtbReview.setRating(response.body().getTransactionreview().getValue());
                        btReview.setVisibility(View.GONE);
                    }
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

    private void do_review() {
        boolean valid = true;

        if (rtbReview.getNumStars() == 0){
            valid = false;
            Tools.Tshort(mContext, "Isi rating dahulu");
        }

        if (transaction_id == 0){
            valid = false;
            Tools.Tshort(mContext, "Kesalahan !, kembali ke halaman sebelumnya");
        }

        if (valid){
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Memuat")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();

            HashMap<String, String> params = new HashMap<>();
            params.put("transaction_id", String.valueOf(transaction_id));
            params.put("description", etDescription.getText().toString());
            params.put("value", String.valueOf((int) rtbReview.getRating()));

            Call<TransactionReviewsItem> call = apiInterface.storeTransactionReview(params);
            call.enqueue(new Callback<TransactionReviewsItem>() {
                @Override
                public void onResponse(Call<TransactionReviewsItem> call, Response<TransactionReviewsItem> response) {
                    progressHUD.dismiss();
                    if (response.isSuccessful()){
                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mContext)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                            .setTitle("Info")
                            .setCancelable(false)
                            .setMessage("Review Berhasil")
                            .addButton("OK", getResources().getColor(R.color.white), getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    finish();
                                }
                            });
                        builder.show();
                    }
                }

                @Override
                public void onFailure(Call<TransactionReviewsItem> call, Throwable t) {
                    progressHUD.dismiss();
                    t.printStackTrace();
                    Tools.Tshort(mContext, getString(R.string.error_connect_server));
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
