package com.example.dat_banh_fpoly.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dat_banh_fpoly.Adapter.OrderAdapter;
import com.example.dat_banh_fpoly.Model.OrderModel;
import com.example.dat_banh_fpoly.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<OrderModel> orderList;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.recyclerViewOrderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadOrderHistory();

        // Xử lý nút "Back to Main"
        findViewById(R.id.backhistory).setOnClickListener(v -> {
            Intent intent = new Intent(OrderHistoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadOrderHistory() {
        db.collection("orders")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        orderList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            OrderModel order = document.toObject(OrderModel.class);

                            // Kiểm tra dữ liệu sản phẩm trong listCart
                            if (order.getListCart() != null && !order.getListCart().isEmpty()) {
                                orderList.add(order);
                            } else {
                                Toast.makeText(this, "Đơn hàng không có sản phẩm.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        orderAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Không thể tải lịch sử đơn hàng.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
