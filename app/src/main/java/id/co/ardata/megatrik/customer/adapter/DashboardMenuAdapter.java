package id.co.ardata.megatrik.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.activity.OrderActivity;
import id.co.ardata.megatrik.customer.model.ServiceCategory;
import id.co.ardata.megatrik.customer.utils.Tools;

public class DashboardMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ServiceCategory> serviceList;
    private Context ctx;

    public DashboardMenuAdapter(Context context, List<ServiceCategory> serviceList){
        this.serviceList = serviceList;
        this.ctx = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu_dashboard, viewGroup, false);
        vh = new DashboardMenuViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        ServiceCategory service = serviceList.get(pos);

        if (viewHolder instanceof DashboardMenuViewHolder){
            DashboardMenuViewHolder holder = (DashboardMenuViewHolder) viewHolder;

            holder.txtServiceName.setText(service.getName());
            Tools.displayImageOriginal(ctx, holder.imgService, service.getImgUrl());
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    class DashboardMenuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtService)
        TextView txtServiceName;
        @BindView(R.id.imgService)
        ImageView imgService;

        DashboardMenuViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                ServiceCategory service = serviceList.get(getAdapterPosition());

                Intent toOrder = new Intent(ctx, OrderActivity.class);
                toOrder.putExtra("service_category_id", service.getId());
                toOrder.putExtra("service_category_name", service.getName());
                ctx.startActivity(toOrder);
                }
            });

        }
    }
}
