package com.example.dat_banh_fpoly.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dat_banh_fpoly.Model.Book;
import com.example.dat_banh_fpoly.R;
import com.example.dat_banh_fpoly.ViewModel.FirebaseDatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditBookActivity extends AppCompatActivity {
    private EditText titleEditText, descriptionEditText, priceEditText, ratingEditText, sellerNameEditText, sellerPhoneEditText, categoryIdEditText, sizesEditText, picUrlEditText, sellerPicEditText;
    private Button updateButton;
    private String bookKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        titleEditText = findViewById(R.id.title_editText);
        descriptionEditText = findViewById(R.id.description_editText);
        priceEditText = findViewById(R.id.price_editText);
        ratingEditText = findViewById(R.id.rating_editText);
        sellerNameEditText = findViewById(R.id.sellerName_editText);
        sellerPhoneEditText = findViewById(R.id.sellerPhone_editText);
        categoryIdEditText = findViewById(R.id.categoryId_editText);
        sizesEditText = findViewById(R.id.sizes_editText);
        picUrlEditText = findViewById(R.id.picUrl_editText);
        sellerPicEditText = findViewById(R.id.sellerPic_editText);
        updateButton = findViewById(R.id.save_button);

        Book book = (Book) getIntent().getSerializableExtra("book");
        bookKey = getIntent().getStringExtra("key");

        titleEditText.setText(book.getTitle());
        descriptionEditText.setText(book.getDescription());
        priceEditText.setText(String.valueOf(book.getPrice()));
        ratingEditText.setText(String.valueOf(book.getRating()));
        sellerNameEditText.setText(book.getSellerName());
        sellerPhoneEditText.setText(String.valueOf(book.getSellerTell()));
        categoryIdEditText.setText(String.valueOf(book.getCategoryId()));
        sizesEditText.setText(String.join(", ", book.getSize()));
        picUrlEditText.setText(String.join(", ", book.getPicUrl()));
        sellerPicEditText.setText(book.getSellerPic());

        updateButton.setOnClickListener(view -> updateBook());
        // Xử lý sự kiện quay lại bằng nút back
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // Quay lại Activity trước đó mà không tạo mới Activity
            }
        });

        // Xử lý sự kiện quay lại bằng nút mBack_Btn
        findViewById(R.id.mBack_Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();  // Quay lại Activity trước đó mà không tạo mới Activity
            }
        });
    }

    private void updateBook() {
        Book updatedBook = new Book();
        updatedBook.setTitle(titleEditText.getText().toString());
        updatedBook.setDescription(descriptionEditText.getText().toString());
        updatedBook.setPrice(Double.parseDouble(priceEditText.getText().toString()));
        updatedBook.setRating(Double.parseDouble(ratingEditText.getText().toString()));
        updatedBook.setSellerName(sellerNameEditText.getText().toString());
        updatedBook.setSellerTell(Integer.parseInt(sellerPhoneEditText.getText().toString()));
        updatedBook.setCategoryId(Integer.parseInt(categoryIdEditText.getText().toString()));
        updatedBook.setSize(new ArrayList<>(Arrays.asList(sizesEditText.getText().toString().split(","))));
        updatedBook.setPicUrl(new ArrayList<>(Arrays.asList(picUrlEditText.getText().toString().split(","))));
        updatedBook.setSellerPic(sellerPicEditText.getText().toString());

        new FirebaseDatabaseHelper().updateBook(bookKey, updatedBook, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Book> books, List<String> keys) {}

            @Override
            public void DataIsInserted() {}

            @Override
            public void DataIsUpdated() {
                Toast.makeText(EditBookActivity.this, "Book updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void DataIsDeleted() {}
        });
    }
}