package id.co.ardata.megatrik.customer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.utils.SessionManager;
import id.co.ardata.megatrik.customer.utils.Tools;

public class WelcomeActivity extends AppCompatActivity {

    private static final int MAX_STEP = 4;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.layoutDots)
    LinearLayout dotsLayout;

    private MyViewPagerAdapter myViewPagerAdapter;

    private String welcome_title_array[] = {
            "Judul 1",
            "Judul 2",
            "Judul 3",
            "Judul 4"
    };
    private String welcome_desc_array[] = {
            "Desc 1",
            "Desc 2",
            "Desc 3",
            "Desc 4"
    };
    private int welcome_images_array[] = {
            R.drawable.img_wizard_1,
            R.drawable.img_wizard_2,
            R.drawable.img_wizard_3,
            R.drawable.img_wizard_4
    };
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        if (!sessionManager.getIsFirstTime()){
            launchMainActivity();
        }
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        init_component();
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }

    private void launchMainActivity() {
        sessionManager.setIsFirstTime(false);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void init_component() {
        // adding bottom dots
        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = viewPager.getCurrentItem() + 1;
                if (current < MAX_STEP){
                    //move to next screen
                    viewPager.setCurrentItem(current);
                }else {
                    launchMainActivity();
                }
            }
        });
        
        ((ImageButton) findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMainActivity();
            }
        });
    }

    private void bottomProgressDots(int current_index) {
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0;i<dots.length;i++){
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10,10,10,10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0){
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.orange_400), PorterDuff.Mode.SRC_IN);
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            bottomProgressDots(position);

            if (position == welcome_title_array.length - 1){
                btnNext.setText(getString(R.string.GOT_IT));
                btnNext.setBackgroundColor(getResources().getColor(R.color.orange_400));
                btnNext.setTextColor(Color.WHITE);
            }else {
                btnNext.setText(getString(R.string.NEXT));
                btnNext.setBackgroundColor(getResources().getColor(R.color.grey_10));
                btnNext.setTextColor(getResources().getColor(R.color.grey_90));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * View Pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter(){

        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_stepper_wizard, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(welcome_title_array[position]);
            ((TextView) view.findViewById(R.id.description)).setText(welcome_desc_array[position]);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(welcome_images_array[position]);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return welcome_title_array.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
