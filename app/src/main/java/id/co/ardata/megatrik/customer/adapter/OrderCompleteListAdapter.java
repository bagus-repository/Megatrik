package id.co.ardata.megatrik.customer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.model.CustomerOrdersItem;

public class OrderCompleteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CustomerOrdersItem> customerOrdersItems;
    private Context ctx;

    public OrderCompleteListAdapter(Context context, List<CustomerOrdersItem> orders){
        this.customerOrdersItems = orders;
        this.ctx = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_complete_list, viewGroup, false);
        vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        CustomerOrdersItem customerOrdersItem = customerOrdersItems.get(pos);
        if (holder instanceof MyViewHolder){
            MyViewHolder v = (MyViewHolder) holder;

            v.tvOrderId.setText("Order ID "+customerOrdersItem.getId());
            v.tvOrderDescription.setText(customerOrdersItem.getDescription());
            v.tvOrderCreatedAt.setText("Tanggal : "+customerOrdersItem.getCreatedAt());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date order_start = format.parse(customerOrdersItem.getOrderStart());
                Date order_end = format.parse(customerOrdersItem.getOrderEnd());

                v.tvDurasi.setText(getTimeDifference(order_end, order_start));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return customerOrdersItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOrderId)
        TextView tvOrderId;
        @BindView(R.id.tvOrderDescription)
        TextView tvOrderDescription;
        @BindView(R.id.tvOrderCreatedAt)
        TextView tvOrderCreatedAt;
        @BindView(R.id.tvDurasi)
        TextView tvDurasi;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public String getTimeDifference(Date endDate, Date startDate) {
        long diff = endDate.getTime() - startDate.getTime();

        return "Durasi : "+TimeUnit.MILLISECONDS.toMinutes(diff) +" menit";
    }
}
