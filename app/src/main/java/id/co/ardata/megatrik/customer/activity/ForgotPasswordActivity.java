package id.co.ardata.megatrik.customer.activity;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.utils.ApiClient;
import id.co.ardata.megatrik.customer.utils.ApiInterface;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.tietEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.btForgot)
    Button btForgot;

    KProgressHUD progressHUD;
    ApiInterface apiInterface;
    AwesomeValidation awesomeValidation;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        mContext = this;
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        btForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                do_forgot();
            }
        });
    }

    private void do_forgot() {
        awesomeValidation.clear();
        awesomeValidation.addValidation(tietEmail, Patterns.EMAIL_ADDRESS, "Email tidak valid");

        if (awesomeValidation.validate()){
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Memuat")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            apiInterface = ApiClient.getApiClient(mContext, false);

            //tembak api disini
        }
    }
}
