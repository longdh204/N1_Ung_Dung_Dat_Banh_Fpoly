package com.example.dat_banh_fpoly.Activity;

import android.content.Context;
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

        private String key;

        public BookItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
            inflate(R.layout.book_list_item, parent, false));

            // Ánh xạ các trường từ layout
            mTitle = itemView.findViewById(R.id.tvTitle); // Tiêu đề sản phẩm
            mDescription = itemView.findViewById(R.id.tvDescription); // Mô tả sản phẩm
            mPrice = itemView.findViewById(R.id.tvPrice); // Giá sản phẩm
            mRating = itemView.findViewById(R.id.tvRating); // Đánh giá sản phẩm
            mSellerName = itemView.findViewById(R.id.tvSellerName); // Tên người bán
            mSellerPhone = itemView.findViewById(R.id.tvSellerPhone); // Số điện thoại người bán
            mCategoryId = itemView.findViewById(R.id.tvCategoryId); // Danh mục sản phẩm
            mSizes = itemView.findViewById(R.id.tvSize); // Kích thước sản phẩm
            mProductImage = itemView.findViewById(R.id.imgProduct); // Ảnh sản phẩm
            mSellerImage = itemView.findViewById(R.id.imgSeller); // Ảnh người bán
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

            // Tải ảnh sản phẩm từ URL nếu có
            if (book.getPicUrl() != null && !book.getPicUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(book.getPicUrl().get(0)) // Load ảnh đầu tiên từ danh sách URL
                        .placeholder(R.drawable.back)
                        .into(mProductImage);
            }

            // Tải ảnh người bán từ URL nếu có
            if (book.getSellerPic() != null && !book.getSellerPic().isEmpty()) { // Kiểm tra URL ảnh người bán
                Glide.with(itemView.getContext())
                        .load(book.getSellerPic())
                        .placeholder(R.drawable.back)
                        .into(mSellerImage);
            }
        }

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
            return new BookItemView(parent); // Cần thay đổi dòng này
        }

        @Override
        public void onBindViewHolder(BookItemView holder, int position) {
            holder.bind(mBookList.get(position),mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mBookList.size(); // Cần thay đổi dòng này
        }
    }
}
