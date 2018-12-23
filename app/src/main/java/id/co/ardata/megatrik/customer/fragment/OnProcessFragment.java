package id.co.ardata.megatrik.customer.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.adapter.OrderListAdapter;
import id.co.ardata.megatrik.customer.model.CustomerOrdersItem;
import id.co.ardata.megatrik.customer.model.ErrorApiMsg;
import id.co.ardata.megatrik.customer.model.UserOrder;
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
 * {@link OnProcessFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OnProcessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnProcessFragment extends Fragment implements OrderListAdapter.MaterialClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.llNullOrder)
    LinearLayout llNullOrder;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.bottom_sheet)
    View bottom_sheet;

    BottomSheetBehavior mBehavior;
    BottomSheetDialog mBottomSheetDialog;

    KProgressHUD progressHUD;
    OrderListAdapter mAdapter;
    ApiInterface apiInterface;
    SessionManager sessionManager;
    Context mContext;
    Gson gson;

    public OnProcessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnProcessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnProcessFragment newInstance(String param1, String param2) {
        OnProcessFragment fragment = new OnProcessFragment();
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
        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_on_process, container, false);
        ButterKnife.bind(this, rootView);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        progressHUD = KProgressHUD.create(mContext)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Memuat")
                .setDetailsLabel("Mengambil data...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        generate_order_data();
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                generate_order_data();
            }
        });

        return rootView;
    }

    public void showBottomSheetDialog(String title, String content){
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        View view = getLayoutInflater().inflate(R.layout.basic_sheet, null);
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.content)).setText(content);
//        ((Button) view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mBottomSheetDialog.dismiss();
//            }
//        });

        mBottomSheetDialog = new BottomSheetDialog(mContext);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mBottomSheetDialog = null;
            }
        });
    }

    private void generate_order_data() {

        progressHUD.show();

        apiInterface = ApiClient.getApiClient(mContext, true);

        Call<UserOrder> call = apiInterface.getUserOrder(sessionManager.getUserId());
//        Log.d("OnProcess", call.request().url().toString());
        call.enqueue(new Callback<UserOrder>() {
            @Override
            public void onResponse(Call<UserOrder> call, Response<UserOrder> response) {
                progressHUD.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()){
//                    Log.d("OnProcess", response.body().toString());
                    List<CustomerOrdersItem> customerOrdersItems = response.body().getCustomerOrders();
                    List<CustomerOrdersItem> itemsAdapter = new ArrayList<>();

                    if (customerOrdersItems == null){
                        llNullOrder.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                    }else {
                        for (int i = 0; i < customerOrdersItems.size(); i++) {
                            if (customerOrdersItems.get(i).getOrderStatus().getIsAccepted() == 1 &&
                                    customerOrdersItems.get(i).getOrderStatus().getIsCompleted() == 0){
                                itemsAdapter.add(customerOrdersItems.get(i));
                            }
                        }
                        if (itemsAdapter.size() > 0){
                            mAdapter = new OrderListAdapter(mContext, itemsAdapter);
                            mAdapter.setMaterialClickListener(OnProcessFragment.this);
                            recyclerView.setAdapter(mAdapter);
                        }else {
                            llNullOrder.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }
                    }
                }else {
                    ErrorApiMsg errorApiMsg = gson.fromJson(response.errorBody().charStream(), ErrorApiMsg.class);
                    Tools.Tshort(mContext, errorApiMsg.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserOrder> call, Throwable t) {
                progressHUD.dismiss();
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
                Tools.Tshort(mContext, getString(R.string.error_connect_server));
            }
        });
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

    @Override
    public void onItemClicked(String title, String content) {
        showBottomSheetDialog(title, content);
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

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        generate_order_data();
    }
}
