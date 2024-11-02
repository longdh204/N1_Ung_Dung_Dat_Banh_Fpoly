package com.example.dat_banh_fpoly.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dat_banh_fpoly.Model.CategoryModel;
import com.example.dat_banh_fpoly.Model.IteamsModel;
import com.example.dat_banh_fpoly.Model.SliderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private MutableLiveData<List<SliderModel>> _slider = new MutableLiveData<>();
    private MutableLiveData<List<CategoryModel>> _category = new MutableLiveData<>();
    private MutableLiveData<List<IteamsModel>> _bestSeller = new MutableLiveData<>();

    public LiveData<List<CategoryModel>> getCategory() {
        return _category;
    }
    public LiveData<List<IteamsModel>> getBestSeller() {
        return _bestSeller;
    }

    public LiveData<List<SliderModel>> getSlider() {
        return _slider;
    }

    public void loadSlider() {
        DatabaseReference ref = firebaseDatabase.getReference("Banner");
        ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<SliderModel> lists = new ArrayList<>();
                for(DataSnapshot childSnapshot:snapshot.getChildren()){
                    SliderModel list = childSnapshot.getValue(SliderModel.class);
                    if(list != null){
                        lists.add(list);
                    }
                    _slider.setValue(lists);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void loadCategory() { // no usages
        DatabaseReference ref = firebaseDatabase.getReference("Category");

        ref.addValueEventListener(new ValueEventListener() {
            @Override // 2 usages
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CategoryModel> lists = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    CategoryModel list = childSnapshot.getValue(CategoryModel.class);
                    if (list != null) {
                        lists.add(list);
                    }
                }
                // Có thể bạn muốn làm gì đó với danh sách "lists" ở đây,
                // ví dụ: cập nhật LiveData hoặc hiển thị lên RecyclerView.
                _category.setValue(lists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (ví dụ: log lỗi ra console)
            }
        });
    }
    public void loadBestSeller() { // no usages
        DatabaseReference ref = firebaseDatabase.getReference("Items");

        ref.addValueEventListener(new ValueEventListener() {
            @Override // 2 usages
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<IteamsModel> lists = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    IteamsModel list = childSnapshot.getValue(IteamsModel.class);
                    if (list != null) {
                        lists.add(list);
                    }
                }
                // Có thể bạn muốn làm gì đó với danh sách "lists" ở đây,
                // ví dụ: cập nhật LiveData hoặc hiển thị lên RecyclerView.
                _bestSeller.setValue(lists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (ví dụ: log lỗi ra console)
            }
        });
    }
}
