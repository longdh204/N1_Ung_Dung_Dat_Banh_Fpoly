package com.example.dat_banh_fpoly.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dat_banh_fpoly.Model.Book;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceBooks;
    private List<Book> books = new ArrayList<>();

    public interface DataStatus {
        void DataIsLoaded(List<Book> books, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceBooks = mDatabase.getReference("Items");
    }

    public void readBooks(final DataStatus dataStatus) {
        mReferenceBooks.addValueEventListener(new ValueEventListener() {
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
                // Handle errors
            }
        });
    }

    public void addBook(Book book, final DataStatus dataStatus) {
        String key = mReferenceBooks.push().getKey();
        mReferenceBooks.child(key).setValue(book)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
    }

    public void updateBook(String key, Book book, final DataStatus dataStatus) {
        mReferenceBooks.child(key).setValue(book)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }
}