package id.co.ardata.megatrik.customer.activity;

import android.content.Context;
import android.content.DialogInterface;
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
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.model.User;
import id.co.ardata.megatrik.customer.utils.ApiClient;
import id.co.ardata.megatrik.customer.utils.ApiInterface;
import id.co.ardata.megatrik.customer.utils.SessionManager;
import id.co.ardata.megatrik.customer.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.inputFullname)
    EditText inputFullname;
    @BindView(R.id.inputEmail)
    EditText inputEmail;
    @BindView(R.id.inputTelepon)
    EditText inputTelepon;
    @BindView(R.id.btn_ubah)
    Button btnUbah;

    AwesomeValidation awesomeValidation;
    KProgressHUD progressHUD;
    SessionManager sessionManager;
    ApiInterface apiInterface;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        ButterKnife.bind(this);

        mContext = this;

        toolbar.setTitle("Ubah Profil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        sessionManager = new SessionManager(this);

        inputFullname.setText(sessionManager.getUserName());
        inputEmail.setText(sessionManager.getUserEmail());
        inputTelepon.setText(sessionManager.getUserPhone());

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_profile();
            }
        });
    }

    private void update_profile() {
        awesomeValidation.clear();

        awesomeValidation.addValidation(inputFullname, RegexTemplate.NOT_EMPTY, "Nama tidak valid");
        awesomeValidation.addValidation(inputTelepon, RegexTemplate.TELEPHONE, "Telepon tidak valid");

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        if (awesomeValidation.validate()){

            apiInterface = ApiClient.getApiClient(mContext, true);

            HashMap<String, String> params = new HashMap<>();
            params.put("name", inputFullname.getText().toString());
            params.put("no_hp", inputTelepon.getText().toString());

            Call<User> call = apiInterface.updateUser(sessionManager.getUserId(), params);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    progressHUD.dismiss();
                    if (response.isSuccessful()){
                        sessionManager.setUserName(inputFullname.getText().toString());
                        sessionManager.setUserPhone(inputTelepon.getText().toString());

                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mContext)
                                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                .setTitle("Konfirmasi")
                                .setMessage("Berhasil ubah profil !")
                                .addButton("Ya", getResources().getColor(R.color.white), getResources().getColor(R.color.red_A700), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        builder.show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
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
