package com.example.dat_banh_fpoly.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dat_banh_fpoly.Model.Book;
import com.example.dat_banh_fpoly.Model.IteamsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase; // Khai báo biến để truy cập Firebase Database
    private DatabaseReference mReferenceBooks; // Khai báo biến để truy cập node "books" trong database
    private List<Book> books = new ArrayList<>(); // Khai báo danh sách để lưu trữ các đối tượng Book

    public interface DataStatus {
        void DataIsLoaded(List<Book> books, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() { // Constructor của class FirebaseDatabaseHelper
        mDatabase = FirebaseDatabase.getInstance(); // Khởi tạo FirebaseDatabase
        mReferenceBooks = mDatabase.getReference("Items"); // Lấy reference đến node "books"
    }

    public void readBooks(final DataStatus dataStatus) { // Phương thức để đọc dữ liệu từ node "books"
        mReferenceBooks.addValueEventListener(new ValueEventListener() { // Gắn ValueEventListener để lắng nghe sự thay đổi dữ liệu
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                books.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Book book = keyNode.getValue(Book.class);
                    books.add(book);
                }
                Log.d("FirebaseData", "Books loaded: " + books.size());
                dataStatus.DataIsLoaded(books, keys);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Phương thức được gọi khi có lỗi xảy ra
                // databaseError chứa thông tin về lỗi
                // ... (Code để xử lý lỗi) ...
            }
        });
    }
    public void addBook(Book book , final DataStatus dataStatus) {
        String key = mReferenceBooks.push().getKey();
        mReferenceBooks.child(key).setValue(book)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }
}
