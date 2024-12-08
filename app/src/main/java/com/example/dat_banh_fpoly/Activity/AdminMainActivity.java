package com.example.dat_banh_fpoly.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.dat_banh_fpoly.Adapter.BestSellerAdapter;
import com.example.dat_banh_fpoly.Adapter.CategoryAdapter;
import com.example.dat_banh_fpoly.Adapter.SliderAdapter;
import com.example.dat_banh_fpoly.Helper.ManagmentCart;
import com.example.dat_banh_fpoly.Model.IteamsModel;
import com.example.dat_banh_fpoly.Model.SliderModel;
import com.example.dat_banh_fpoly.R;
import com.example.dat_banh_fpoly.ViewModel.MainViewModel;
import com.example.dat_banh_fpoly.databinding.ActivityAdminMainBinding;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends BaseActivity {
    private ActivityAdminMainBinding binding;
    private MainViewModel viewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBanners();
        initCategory();
        initBestSeller();
        bottomNavigation();

        // Thiết lập giao diện
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        View decor = window.getDecorView();
        decor.setSystemUiVisibility(0);

        // Xử lý tìm kiếm
        binding.TimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchItemsByName(s.toString()); // Tìm kiếm sản phẩm
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        binding.buttonAddProduct.setOnClickListener(v -> {
            Toast.makeText(this, "Thêm sản phẩm mới", Toast.LENGTH_SHORT).show();
            // Chuyển đến màn hình thêm sản phẩm (nếu có)
            Intent intent = new Intent(AdminMainActivity.this, NewBookActivity.class);
            startActivity(intent);
        });
        binding.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, BookListActivity.class);
                startActivity(intent);
            }
        });
//        binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AdminMainActivity.this,BookListActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void searchItemsByName(String query) {
        viewModel.getBestSeller().observe(this, items -> {
            List<IteamsModel> filteredItems = new ArrayList<>();
            for (IteamsModel item : items) {
                if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredItems.add(item);
                }
            }

            // Cập nhật RecyclerView với danh sách đã lọc
            binding.recyclerBestSeller.setAdapter(new BestSellerAdapter(filteredItems));
        });
    }

    private void bottomNavigation() {
        binding.cartBtn.setOnClickListener(view -> {
            ManagmentCart managmentCart = new ManagmentCart(AdminMainActivity.this);
            if (!managmentCart.getListCart().isEmpty()) {
                startActivity(new Intent(AdminMainActivity.this, CardActivity.class));
            } else {
                Toast.makeText(AdminMainActivity.this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
            }
        });
        binding.profileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        binding.myorderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });
        binding.favoriteBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });
    }

    private void initBestSeller() {
        binding.progressBarBestSeller.setVisibility(View.VISIBLE);

        viewModel.getBestSeller().observe(this, items -> {
            binding.recyclerBestSeller.setLayoutManager(new GridLayoutManager(this, 2));
            binding.recyclerBestSeller.setAdapter(new BestSellerAdapter(items));
            binding.progressBarBestSeller.setVisibility(View.GONE);
        });
        viewModel.loadBestSeller();
    }

    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);

        viewModel.getCategory().observe(this, items -> {
            binding.recyclerViewCategory.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            );
            binding.recyclerViewCategory.setAdapter(new CategoryAdapter(items));
            binding.progressBarCategory.setVisibility(View.GONE);
        });

        viewModel.loadCategory();
    }

    private void initBanners() {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.getSlider().observe(this, banners -> {
            setupBanners(banners);
            binding.progressBar.setVisibility(View.GONE);
        });
        viewModel.loadSlider();
    }

    private void setupBanners(List<SliderModel> images) {
        binding.viewPager2.setAdapter(new SliderAdapter(images, binding.viewPager2));
        binding.viewPager2.setClipToPadding(false);
        binding.viewPager2.setClipChildren(false);
        binding.viewPager2.setOffscreenPageLimit(3);
        binding.viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(0));
        binding.viewPager2.setPageTransformer(transformer);

        if (images.size() > 1) {
            binding.dotIndicator.setVisibility(View.VISIBLE);
            binding.dotIndicator.attachTo(binding.viewPager2);
        }
    }

}
