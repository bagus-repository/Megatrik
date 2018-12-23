package id.co.ardata.megatrik.customer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.fragment.HomeFragment;
import id.co.ardata.megatrik.customer.fragment.RiwayatFragment;
import id.co.ardata.megatrik.customer.fragment.SettingFragment;
import id.co.ardata.megatrik.customer.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBack = false;
    private Fragment lastFragment;
    SessionManager sessionManager;
    @BindView(R.id.main_navigation)
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemReselectedListener onNavigationItemReselectedListener = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    lastFragment = fragment;
                    return true;
                case R.id.navigation_order:
                    fragment = new RiwayatFragment();
                    loadFragment(fragment);
                    lastFragment = fragment;
                    return true;
                case R.id.navigation_profil:
                    fragment = new SettingFragment();
                    loadFragment(fragment);
                    lastFragment = fragment;
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Fragment fragment = new HomeFragment();
        loadFragment(fragment);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener);
    }

    @Override
    public void onBackPressed() {
        if(doubleBack){
            finish();
        }else{
            this.doubleBack = true;
            Toast.makeText(this, "Tekan back sekali lagi untuk keluar aplikasi!", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBack = false;
            }
        }, 2500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lastFragment != null)
            loadFragment(lastFragment);
    }
}
