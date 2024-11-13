package com.example.dat_banh_fpoly.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dat_banh_fpoly.Model.OrderModel;
import com.example.dat_banh_fpoly.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderModel> orderList;

    public OrderAdapter(List<OrderModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);
        holder.orderDate.setText("Order Date : " + order.getOrderDate());
        holder.totalAmount.setText("Total Amount : $" + order.getTotalAmount());

        // Thiết lập ProductAdapter cho danh sách sản phẩm của đơn hàng này
        ProductAdapter productAdapter = new ProductAdapter(order.getListCart());
        holder.recyclerViewProductList.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerViewProductList.setAdapter(productAdapter);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate, totalAmount;
        RecyclerView recyclerViewProductList;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.orderDate);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            recyclerViewProductList = itemView.findViewById(R.id.recyclerViewProductList);
        }
    }
}
