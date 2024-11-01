package com.example.dat_banh_fpoly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dat_banh_fpoly.Model.CategoryModel;
import com.example.dat_banh_fpoly.databinding.ViewholderCategoryBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {
    private List<CategoryModel> items; // 1 usage
    private Context context; // no usages

    public CategoryAdapter(List<CategoryModel> items) { // no usages
        this.items = items;
    }
    @NonNull
    @Override
    public CategoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Viewholder holder, int position) {
        CategoryModel item = items.get(position);

        holder.binding.titleCat.setText(item.getTitle());

        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl())
                .into(holder.binding.picCat);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderCategoryBinding binding;
        public Viewholder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
