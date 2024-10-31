package com.example.dat_banh_fpoly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestOptions;
import com.example.dat_banh_fpoly.Model.SliderModel;
import com.example.dat_banh_fpoly.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewholder> {
    private List<SliderModel> sliderModels; // 1 usage
    private ViewPager2 viewPager2; // 1 usage
    private Context context; // no usages

    public SliderAdapter(List<SliderModel> sliderModels, ViewPager2 viewPager2) {
        this.sliderModels = sliderModels;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderAdapter.SliderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.slider_image_container,parent,false);
        return new SliderViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.SliderViewholder holder, int position) {
        holder.setImage(sliderModels.get(position),context);
        if(position == sliderModels.size()-1){
            viewPager2.post(()->notifyDataSetChanged());

        }
    }

    @Override
    public int getItemCount() {
        return sliderModels.size();
    }

    public class SliderViewholder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public SliderViewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageSlide);
        }
        public void setImage(SliderModel sliderModel, Context context) {
            RequestOptions requestOptions = new RequestOptions().transform(new CenterInside());
            Glide.with(context)
                    .load(sliderModel.getUrl())
                    .apply(requestOptions)
                    .into(imageView);
        }

    }
}
