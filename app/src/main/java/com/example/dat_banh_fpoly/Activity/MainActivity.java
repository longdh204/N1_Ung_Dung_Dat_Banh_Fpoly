package com.example.dat_banh_fpoly.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.dat_banh_fpoly.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel = new MainViewModel();
    // Khởi tạo danh sách items
    List<IteamsModel> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBanners();
        //6
        initCategory();
        //7
        initBestSeller();

        bottomNavigation();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        View decor = window.getDecorView();
        decor.setSystemUiVisibility(0);
        binding.recyclerBestSeller.setAdapter(new BestSellerAdapter(items, false)); // Màn hình Main, ẩn nút xóa
        binding.TimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchItemsByName(s.toString()); // Gọi hàm tìm kiếm khi có thay đổi
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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



    // chuyen man hinh sang trang cart gio hang
    private void bottomNavigation() {
        binding.cartBtn.setOnClickListener(view -> {
            ManagmentCart managmentCart = new ManagmentCart(MainActivity.this);
            if(!managmentCart.getListCart().isEmpty()){
                startActivity(new Intent(MainActivity.this,CardActivity.class));
            }else{
                Toast.makeText(MainActivity.this,"Your Card is Empty",Toast.LENGTH_SHORT).show();
            }
        });
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        binding.myorderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            }
        });
        binding.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initBestSeller() {
        binding.progressBarBestSeller.setVisibility(View.VISIBLE);

        viewModel.getBestSeller().observe(this, items -> {
            binding.recyclerBestSeller.setLayoutManager(new GridLayoutManager(this,2));
            binding.recyclerBestSeller.setAdapter(new BestSellerAdapter(items));
            binding.progressBarBestSeller.setVisibility(View.GONE);
        });
        viewModel.loadBestSeller();
    }

    private void initCategory() {
        //1:02:26
        binding.progressBarCategory.setVisibility(View.VISIBLE);

        viewModel.getCategory().observe(this, items -> {
            binding.recyclerViewCategory.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
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