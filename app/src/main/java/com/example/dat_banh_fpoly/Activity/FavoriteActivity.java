package com.example.dat_banh_fpoly.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dat_banh_fpoly.Adapter.BestSellerAdapter;
import com.example.dat_banh_fpoly.Helper.ManagmentCart;
import com.example.dat_banh_fpoly.Model.IteamsModel;
import com.example.dat_banh_fpoly.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView favoriteRecyclerView;
    private BestSellerAdapter favoriteAdapter;
    private ArrayList<IteamsModel> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite); // Tạo một layout cho FavoriteActivity

        favoriteRecyclerView = findViewById(R.id.favoriteRecyclerView);
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        favoriteAdapter = BestSellerAdapter.forFavorite(favoriteList);

        ManagmentCart managmentCart = new ManagmentCart(this);
        favoriteList = managmentCart.getFavoriteList();

        favoriteAdapter = new BestSellerAdapter(favoriteList);
        favoriteRecyclerView.setAdapter(favoriteAdapter);

        favoriteAdapter = new BestSellerAdapter(favoriteList, true); // Màn hình Favorite, hiển thị nút xóa
        favoriteRecyclerView.setAdapter(favoriteAdapter);
        // hiển thị recycler dưới dạng 2 hàng
        RecyclerView recyclerView = findViewById(R.id.favoriteRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(favoriteAdapter);
        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra tài khoản đang sử dụng
                String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                // Nếu tài khoản là admin, quay về màn hình Admin
                if ("admin123@gmail.com".equals(currentUserEmail)) {
                    Intent intent = new Intent(FavoriteActivity.this, AdminMainActivity.class); // Màn hình Admin
                    startActivity(intent);
                } else {
                    // Nếu không phải admin, quay về màn hình chính
                    Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                finish(); // Đảm bảo quay lại đúng màn hình mà không lưu lại trong back stack
            }
        });

    }
}

