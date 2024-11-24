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

public class NewBookActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private EditText ratingEditText;
    private EditText sellerNameEditText;
    private EditText sellerPhoneEditText;
    private EditText categoryIdEditText;
    private EditText sizesEditText;
    private EditText picUrlEditText;
    private EditText sellerPicEditText;
    private Button saveButton,cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book); // Thay thế bằng ID layout của bạn

        // Ánh xạ các View
        titleEditText = findViewById(R.id.title_editText);
        descriptionEditText = findViewById(R.id.description_editText);
        priceEditText = findViewById(R.id.price_editText);
        ratingEditText = findViewById(R.id.rating_editText);
        sellerNameEditText = findViewById(R.id.sellerName_editText);
        sellerPhoneEditText = findViewById(R.id.sellerPhone_editText);
        sizesEditText = findViewById(R.id.sizes_editText);
        picUrlEditText = findViewById(R.id.picUrl_editText);
        sellerPicEditText = findViewById(R.id.sellerPic_editText);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.mBack_Btn);
        categoryIdEditText = findViewById(R.id.categoryId_editText);
        // ... (Code xử lý sự kiện cho các View, ví dụ: lưu dữ liệu khi click nút "Save") ...

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = new Book();
                book.setTitle(titleEditText.getText().toString());
                book.setDescription(descriptionEditText.getText().toString());
                book.setPrice(Double.parseDouble(priceEditText.getText().toString()));
                book.setRating(Double.parseDouble(ratingEditText.getText().toString()));
                book.setSellerName(sellerNameEditText.getText().toString());
                book.setSellerTell(Integer.parseInt(sellerPhoneEditText.getText().toString()));
                book.setCategoryId(Integer.parseInt(categoryIdEditText.getText().toString()));


                ArrayList<String> picUrl = new ArrayList<>(Arrays.asList(picUrlEditText.getText().toString().split(",")));
                book.setPicUrl(picUrl);

                ArrayList<String> size = new ArrayList<>(Arrays.asList(sizesEditText.getText().toString().split(",")));
                book.setSize(size);

                book.setSellerPic(sellerPicEditText.getText().toString());

                new FirebaseDatabaseHelper().addBook(book, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Book> books, List<String> keys) {
                        // Handle loaded data if necessary
                    }

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(NewBookActivity.this, "The book record has been inserted successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void DataIsUpdated() {
                        // Handle data update if necessary
                    }

                    @Override
                    public void DataIsDeleted() {
                        // Handle data deletion if necessary
                    }
                });


            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); return;
            }
        });
    }
}