package com.example.dat_banh_fpoly.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dat_banh_fpoly.Model.Book;
import com.example.dat_banh_fpoly.R;

import java.util.List;

public class RecyclerView_Config {
    private Context mContext;
    private BooksAdapter mBooksAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, List<Book> books, List<String> keys) {
        mContext = context;
        mBooksAdapter = new BooksAdapter(books, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mBooksAdapter);
    }

    class BooksAdapter extends RecyclerView.Adapter<BookItemView> {
        private List<Book> mBookList;
        private List<String> mKeys;

        public BooksAdapter(List<Book> mBookList, List<String> mKeys) {
            this.mBookList = mBookList;
            this.mKeys = mKeys;
        }

        @Override
        public BookItemView onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BookItemView(parent);
        }

        @Override
        public void onBindViewHolder(BookItemView holder, int position) {
            holder.bind(mBookList.get(position), mKeys.get(position));
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, EditBookActivity.class);
                intent.putExtra("book", mBookList.get(position));
                intent.putExtra("key", mKeys.get(position));
                mContext.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return mBookList.size();
        }
    }

    class BookItemView extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;
        private TextView mPrice;
        private TextView mRating;
        private TextView mSellerName;
        private TextView mSellerPhone;
        private TextView mCategoryId;
        private TextView mSizes;
        private ImageView mProductImage;
        private ImageView mSellerImage;

        public BookItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.book_list_item, parent, false));
            mTitle = itemView.findViewById(R.id.tvTitle);
            mDescription = itemView.findViewById(R.id.tvDescription);
            mPrice = itemView.findViewById(R.id.tvPrice);
            mRating = itemView.findViewById(R.id.tvRating);
            mSellerName = itemView.findViewById(R.id.tvSellerName);
            mSellerPhone = itemView.findViewById(R.id.tvSellerPhone);
            mCategoryId = itemView.findViewById(R.id.tvCategoryId);
            mSizes = itemView.findViewById(R.id.tvSize);
            mProductImage = itemView.findViewById(R.id.imgProduct);
            mSellerImage = itemView.findViewById(R.id.imgSeller);
        }

        public void bind(Book book, String key) {
            mTitle.setText(book.getTitle());
            mDescription.setText(book.getDescription());
            mPrice.setText(String.valueOf(book.getPrice()));
            mRating.setText("Rating: " + String.valueOf(book.getRating()));
            mSellerName.setText("Seller: " + book.getSellerName());
            mSellerPhone.setText("Phone: " + book.getSellerTell());
            mCategoryId.setText("Category: " + String.valueOf(book.getCategoryId()));
            mSizes.setText("Sizes: " + String.join(", ", book.getSize()));

            if (book.getPicUrl() != null && !book.getPicUrl().isEmpty()) {
                Glide.with(itemView.getContext()).load(book.getPicUrl().get(0)).placeholder(R.drawable.back).into(mProductImage);
            }

            if (book.getSellerPic() != null && !book.getSellerPic().isEmpty()) {
                Glide.with(itemView.getContext()).load(book.getSellerPic()).placeholder(R.drawable.back).into(mSellerImage);
            }
        }
    }
}