package id.co.ardata.megatrik.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.activity.PaymentActivity;
import id.co.ardata.megatrik.customer.model.CustomerOrdersItem;
import id.co.ardata.megatrik.customer.model.MaterialsItem;
import id.co.ardata.megatrik.customer.utils.Tools;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CustomerOrdersItem> customerOrdersItems;
    private Context ctx;

    private MaterialClickListener materialClickListener;

    public interface MaterialClickListener {
        void onItemClicked(String title, String content);
    }

    public void setMaterialClickListener(MaterialClickListener listener){
        this.materialClickListener = listener;
    }

    public OrderListAdapter(Context context, List<CustomerOrdersItem> orders){
        this.customerOrdersItems = orders;
        this.ctx = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_list, viewGroup, false);
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
            v.tvTechnicianName.setText("Teknisi : "+customerOrdersItem.getTechnician().getName());
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
        @BindView(R.id.tvTechnicianName)
        TextView tvTechnicianName;
        @BindView(R.id.btMaterial)
        ImageButton btMaterial;
        @BindView(R.id.btEstimasi)
        ImageButton btEstimasi;
        @BindView(R.id.btConfirmPayment)
        ImageButton btConfirmPayment;

        public MyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            btMaterial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //To material activity nek ga pakai bottom navigation aja
                    CustomerOrdersItem ordersItem = customerOrdersItems.get(getAdapterPosition());
                    List<MaterialsItem> materials = ordersItem.getMaterials();
                    String materials_text = "";
                    for (int i = 0; i < materials.size(); i++) {
                        materials_text += materials.get(i).getQuantity()+"x "+materials.get(i).getName()+"\n";
                    }

                    if (materialClickListener != null){
                        materialClickListener.onItemClicked("List Material", materials_text);
                    }
                }
            });

            btEstimasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomerOrdersItem ordersItem = customerOrdersItems.get(getAdapterPosition());
                    if (materialClickListener != null){
                        List<MaterialsItem> materials = ordersItem.getMaterials();
                        String materials_text = "";
                        int total = 0;
                        for (int i = 0; i < materials.size(); i++) {
                            int qty = materials.get(i).getQuantity();
                            int harga = materials.get(i).getPrice();
                            total += harga*qty;
                            materials_text += qty+"x "+materials.get(i).getName()+" @Rp."+harga+"\n";
                        }
                        materials_text += "\nTotal : Rp."+total;
                        materialClickListener.onItemClicked("Harga Estimasi", materials_text);
                    }
                }
            });

            btConfirmPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomerOrdersItem ordersItem = customerOrdersItems.get(getAdapterPosition());
                    if (ordersItem.getTransaction() == null){
                        Intent intent = new Intent(ctx, PaymentActivity.class);
                        intent.putExtra("order_id", ordersItem.getId());
                        ctx.startActivity(intent);
                    }else {
                        Tools.Tshort(ctx, "Pembayaran berhasil, menunggu konfirmasi teknisi.");
                    }
                }
            });
        }
    }
}
