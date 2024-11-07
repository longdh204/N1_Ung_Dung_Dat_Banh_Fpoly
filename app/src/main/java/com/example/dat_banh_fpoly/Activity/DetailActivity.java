package com.example.dat_banh_fpoly.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.dat_banh_fpoly.Adapter.PicListAdapter;
import com.example.dat_banh_fpoly.Adapter.WeightAdapter;
import com.example.dat_banh_fpoly.Helper.ManagmentCart;
import com.example.dat_banh_fpoly.Model.IteamsModel;
import com.example.dat_banh_fpoly.R;
import com.example.dat_banh_fpoly.databinding.ActivityDetailBinding;

import java.util.ArrayList;

public class DetailActivity extends BaseActivity {
    private ActivityDetailBinding binding;
    private IteamsModel item; // no usages
    private int numberOrder = 1; // no usages
    private ManagmentCart managmentCart; // 1 usage
    // Tạo biến để lưu trạng thái yêu thích
    private boolean isFavorited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);

        getBundleExtra();
        initLists();
        // Cấu hình lại thanh trạng thái
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // Đặt hệ thống thanh trạng thái sáng để chữ và biểu tượng dễ đọc hơn
        View decor = window.getDecorView();
        decor.setSystemUiVisibility(0);

        // Thiết lập sự kiện nhấn cho favBtn
        binding.favBtn.setOnClickListener(v -> {
            isFavorited = !isFavorited; // Thay đổi trạng thái
            updateFavIcon(); // Cập nhật icon
        });
    }
    // Hàm cập nhật icon
    private void updateFavIcon() {
        if (isFavorited) {
            binding.favBtn.setImageResource(R.drawable.fav_icon3); // Icon khi đã yêu thích
            binding.favBtn.getLayoutParams().width = 130;
            binding.favBtn.getLayoutParams().height = 130;

            // Thêm mục vào danh sách yêu thích
            ManagmentCart managmentCart = new ManagmentCart(this);
            managmentCart.addFavoriteItem(item);
        } else {
            binding.favBtn.setImageResource(R.drawable.fav_icon); // Icon mặc định

            // Xóa mục khỏi danh sách yêu thích
            ManagmentCart managmentCart = new ManagmentCart(this);
            managmentCart.removeFavoriteItem(item);
        }
        binding.favBtn.requestLayout(); // Yêu cầu cập nhật lại bố cục
    }

    private void initLists() {
        ArrayList<String> weightList = new ArrayList<>(item.getSize());

        binding.weightList.setAdapter(new WeightAdapter(weightList));

        binding.weightList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        ArrayList<String> picList = new ArrayList<>(item.getPicUrl());

        Glide.with(this)
                .load(picList.get(0))
                .into(binding.picMain);

        binding.picList.setAdapter(new PicListAdapter(picList, binding.picMain));
        binding.picList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void getBundleExtra() {
        item = (IteamsModel) getIntent().getSerializableExtra("object");

        binding.titleTxt.setText(item.getTitle());
        binding.descriptionTxt.setText(item.getDescription());
        binding.priceTxt.setText("$" + item.getPrice());
        binding.ratingTxt.setText(item.getRating() + " ");
        binding.sellerNameTxt.setText(item.getSellerName());

        binding.addToCardBtn.setOnClickListener(v -> {
            item.setNumberInCart(numberOrder);
            managmentCart.insertItems(item);
        });
        binding.backBtn.setOnClickListener(v -> startActivity(new Intent(DetailActivity.this, MainActivity.class)));
        binding.cartBtn.setOnClickListener(v -> {
            if(!managmentCart.getListCart().isEmpty()){
                startActivity(new Intent(DetailActivity.this,CardActivity.class));
            }else{
                Toast.makeText(DetailActivity.this,"Your Card is Empty",Toast.LENGTH_SHORT).show();
            }

        });

        Glide.with(this)
                .load(item.getSellerPic())
                .apply(new RequestOptions().transform(new CenterCrop()))
                .into(binding.picSeller);

        binding.msgToSellerBtn.setOnClickListener(view -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:" + item.getSellerTell()));
            sendIntent.putExtra("sms_body", "type your Message");
            startActivity(sendIntent);
        });
        //15844
        binding.callToSellerBtn.setOnClickListener(v -> {
            String phone = String.valueOf(item.getSellerTell());
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone,null));
            startActivity(intent);
        });
    }
}