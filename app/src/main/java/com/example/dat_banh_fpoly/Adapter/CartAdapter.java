package com.example.dat_banh_fpoly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dat_banh_fpoly.Helper.ChangeNumberItemsListener;
import com.example.dat_banh_fpoly.Helper.ManagmentCart;
import com.example.dat_banh_fpoly.Model.IteamsModel;
import com.example.dat_banh_fpoly.databinding.ViewholderCartBinding;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<IteamsModel> listItemSelected; // 1 usage
    private ManagmentCart managmentCart; // no usages
    private ChangeNumberItemsListener changeNumberItemsListener; // 1 usage

    public CartAdapter(ArrayList<IteamsModel> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.managmentCart = new ManagmentCart(context);
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(
        LayoutInflater.from(parent.getContext()), parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        IteamsModel item = listItemSelected.get(position);

        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeEachItem.setText("$" + item.getPrice());
        holder.binding.totalEachItem.setText("$" + Math.round(item.getNumberInCart() * item.getPrice()));
        holder.binding.numberItemTxt.setText(String.valueOf(item.getNumberInCart()));

        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl().get(0))
                .apply(new RequestOptions().centerCrop())
                .into(holder.binding.picCart);

        holder.binding.plusCartBtn.setOnClickListener(v -> {
            // Chưa có xử lý gì khi click vào nút "plusCartBtn"
            managmentCart.plusItem(listItemSelected, position, () -> {
                notifyDataSetChanged();
                if(changeNumberItemsListener != null){
                    changeNumberItemsListener.onChanged();
                }
            });
        });
        holder.binding.minusCartBtn.setOnClickListener(view -> managmentCart.minusItem(listItemSelected, position, () -> {
            notifyDataSetChanged();
            if(changeNumberItemsListener != null){
                changeNumberItemsListener.onChanged();
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;
        public ViewHolder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
