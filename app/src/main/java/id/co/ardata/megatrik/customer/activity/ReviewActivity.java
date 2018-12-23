package id.co.ardata.megatrik.customer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.utils.ApiInterface;
import id.co.ardata.megatrik.customer.utils.SessionManager;

public class ReviewActivity extends AppCompatActivity {

    @BindView(R.id.inputReview)
    EditText inputReview;
    @BindView(R.id.rating_bar)
    RatingBar inputRating;
    @BindView(R.id.btn_finish)
    Button btnFinish;

    KProgressHUD progressHUD;
    ApiInterface apiInterface;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ButterKnife.bind(this);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
