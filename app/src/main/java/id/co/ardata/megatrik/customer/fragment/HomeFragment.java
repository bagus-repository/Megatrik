package id.co.ardata.megatrik.customer.fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.adapter.DashboardMenuAdapter;
import id.co.ardata.megatrik.customer.model.Content;
import id.co.ardata.megatrik.customer.model.ContentsItem;
import id.co.ardata.megatrik.customer.model.ServiceCategory;
import id.co.ardata.megatrik.customer.utils.ApiClient;
import id.co.ardata.megatrik.customer.utils.ApiInterface;
import id.co.ardata.megatrik.customer.utils.SessionManager;
import id.co.ardata.megatrik.customer.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    SessionManager sessionManager;
    Context mContext;
    @BindView(R.id.layout_dots)
    LinearLayout layout_dots;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_menu)
    RecyclerView recyclerView;

    AdapterImageSlider adapterImageSlider;
    DashboardMenuAdapter dashboardMenuAdapter;
    ApiInterface apiInterface;
    Runnable runnable;
    Handler handler = new Handler();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mContext = getContext();
        sessionManager = new SessionManager(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        toolbar.setTitle("Hi, "+sessionManager.getUserName());
        toolbar.setSubtitle("Customer");

        recyclerView = rootView.findViewById(R.id.recycler_menu);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        load_sliders();
        load_menus();
    }

    private void load_menus() {
        apiInterface = ApiClient.getApiClient(mContext, true);

        Call<List<ServiceCategory>> call = apiInterface.getServiceCategory();
        call.enqueue(new Callback<List<ServiceCategory>>() {
            @Override
            public void onResponse(Call<List<ServiceCategory>> call, Response<List<ServiceCategory>> response) {
                if (response.isSuccessful()){
                    dashboardMenuAdapter = new DashboardMenuAdapter(mContext, response.body());
                    dashboardMenuAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(dashboardMenuAdapter);
                }else {
                    Tools.Tshort(mContext, "Gagal load menu");
                }
            }

            @Override
            public void onFailure(Call<List<ServiceCategory>> call, Throwable t) {
                t.printStackTrace();
                Tools.Tshort(mContext, getString(R.string.error_connect_server));
            }
        });
    }

    private void load_sliders() {
        adapterImageSlider = new AdapterImageSlider(this, new ArrayList<ContentsItem>());

        apiInterface = ApiClient.getApiClient(mContext, true);

        Call<Content> call = apiInterface.getContent();
        call.enqueue(new Callback<Content>() {
            @Override
            public void onResponse(Call<Content> call, Response<Content> response) {
                if (response.isSuccessful()){
                    List<ContentsItem> sliders = response.body().getContents();
                    adapterImageSlider.setItems(sliders);
                    adapterImageSlider.notifyDataSetChanged();
                    viewPager.setAdapter(adapterImageSlider);
                    viewPager.setCurrentItem(0);

                    addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);

                    //here assign value to per image slider

                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {

                        }

                        @Override
                        public void onPageSelected(int i) {
                            //assigned value handling here
                        }

                        @Override
                        public void onPageScrollStateChanged(int i) {

                        }
                    });

                    startAutoSlider(adapterImageSlider.getCount());
                }else {
                    Tools.Tshort(mContext, "Gagal load slider");
                }
            }

            @Override
            public void onFailure(Call<Content> call, Throwable t) {
                t.printStackTrace();
                Tools.Tshort(mContext, getString(R.string.error_connect_server));
            }
        });
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i=0;i<dots.length;i++){
            dots[i] = new ImageView(getContext());
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10,0,10,0);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            dots[i].setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_40), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[current].setImageResource(R.drawable.shape_circle);
            dots[current].setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_40), PorterDuff.Mode.SRC_ATOP);
        }

    }

    private void startAutoSlider(final int count){
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos += 1;
                if(pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 10000);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class AdapterImageSlider extends PagerAdapter {

        private Fragment frag;
        private List<ContentsItem> items;

        public AdapterImageSlider(Fragment fragment, List<ContentsItem> items) {
            this.frag = fragment;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public ContentsItem getItem (int pos){
            return items.get(pos);
        }

        public void setItems(List<ContentsItem> items){
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((RelativeLayout) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ContentsItem item = items.get(position);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView imageView = v.findViewById(R.id.image);

            Tools.displayImageOriginal(mContext, imageView, item.getImage());

            ((ViewPager) container).addView(v);
            return v;
        }
        
    }

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
