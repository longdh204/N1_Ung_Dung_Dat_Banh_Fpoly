package com.example.dat_banh_fpoly.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.dat_banh_fpoly.Activity.DetailActivity;
import com.example.dat_banh_fpoly.Model.IteamsModel;
import com.example.dat_banh_fpoly.databinding.ViewholderBestSellerBinding;

import java.util.List;

public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerAdapter.ViewHolder> {
    private List<IteamsModel> items;
    private Context context;
    private boolean isFavorite; // Cờ để xác định màn hình

    // Constructor khi truyền isFavorite
    public BestSellerAdapter(List<IteamsModel> items, boolean isFavorite) {
        this.items = items;
        this.isFavorite = isFavorite; // Lưu giá trị để quyết định hiển thị nút xóa hay không
    }

    // Constructor mặc định, isFavorite mặc định là false
    public BestSellerAdapter(List<IteamsModel> items) {
        this(items, false); // Gọi constructor chính, mặc định 'isFavorite' là false
    }

    @NonNull
    @Override
    public BestSellerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderBestSellerBinding binding = ViewholderBestSellerBinding.inflate(
                LayoutInflater.from(context), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerAdapter.ViewHolder holder, int position) {
        IteamsModel item = items.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.priceTxt.setText("$" + item.getPrice());
        holder.binding.ratingTxt.setText(String.valueOf(item.getRating()));

        Glide.with(context)
                .load(item.getPicUrl().get(0))
                .apply(new RequestOptions().transform(new CenterCrop()))
                .into(holder.binding.picBestSeller);

        // Chuyển hướng sang màn hình chi tiết khi nhấn vào item
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", item);
            context.startActivity(intent);
        });

        // Xử lý hiển thị nút xóa khi ở màn hình Favorite
        if (isFavorite) {
            holder.binding.deleteBtn.setVisibility(View.VISIBLE); // Hiển thị nút xóa khi là màn hình Favorite
            holder.binding.deleteBtn.setOnClickListener(v -> {
                removeItem(position); // Gọi phương thức xóa item
            });
        } else {
            holder.binding.deleteBtn.setVisibility(View.GONE); // Ẩn nút xóa khi không phải màn hình Favorite
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Phương thức xóa item khỏi danh sách hiển thị
    public void removeItem(int position) {
        items.remove(position); // Xóa mục khỏi danh sách
        notifyItemRemoved(position); // Cập nhật giao diện
        notifyItemRangeChanged(position, items.size()); // Điều chỉnh các item còn lại
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderBestSellerBinding binding;

        public ViewHolder(ViewholderBestSellerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
