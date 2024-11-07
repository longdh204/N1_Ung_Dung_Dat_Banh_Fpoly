package com.example.dat_banh_fpoly.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dat_banh_fpoly.Adapter.CartAdapter;
import com.example.dat_banh_fpoly.Helper.ChangeNumberItemsListener;
import com.example.dat_banh_fpoly.Helper.ManagmentCart;
import com.example.dat_banh_fpoly.Model.OrderModel;
import com.example.dat_banh_fpoly.R;
import com.example.dat_banh_fpoly.databinding.ActivityCardBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.google.firebase.firestore.FirebaseFirestore;


public class CardActivity extends BaseActivity {
    private ActivityCardBinding binding;
    private ManagmentCart managmentCart;
    private double tax = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        setVariable();
        initCartList();
        calculatorCart();
        // Cấu hình lại thanh trạng thái
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // Đặt hệ thống thanh trạng thái sáng để chữ và biểu tượng dễ đọc hơn
        View decor = window.getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initCartList() {
        binding.cardView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.cardView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, this::calculatorCart));
    }

    private void calculatorCart() { // no usages
        double percentTax = 0.02;
        double delivery = 15.0;

        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100) / 100.0;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100.0;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100.0;

        binding.totalFeeTxt.setText("$" + itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> startActivity(new Intent(CardActivity.this, MainActivity.class)));

        binding.payButton.setOnClickListener(view -> {
            // Hiển thị tổng giá tiền và xác nhận thanh toán
            double totalAmount = Math.round((managmentCart.getTotalFee() + tax + 15.0) * 100) / 100.0;
            showPaymentConfirmation(totalAmount);
        });
    }
    private void showPaymentConfirmation(double totalAmount) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thanh toán")
                .setMessage("Tổng số tiền: $" + totalAmount + "\nBạn có chắc chắn muốn thanh toán không?")
                .setPositiveButton("Thanh toán", (dialog, which) -> {
                    // Hiển thị dialog nhập thông tin ngân hàng
                    showBankInfoDialog();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    private void showBankInfoDialog() {
        // Tạo layout cho dialog nhập thông tin ngân hàng
        View bankInfoView = getLayoutInflater().inflate(R.layout.dialog_bank_info, null);
        EditText edtBankName = bankInfoView.findViewById(R.id.edtBankName);
        EditText edtAccountNumber = bankInfoView.findViewById(R.id.edtAccountNumber);
        EditText edtCardExpiry = bankInfoView.findViewById(R.id.edtCardExpiry);
        EditText edtCVV = bankInfoView.findViewById(R.id.edtCVV);

        new AlertDialog.Builder(this)
                .setTitle("Nhập thông tin ngân hàng")
                .setView(bankInfoView)
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    // Kiểm tra thông tin ngân hàng (có thể thêm điều kiện kiểm tra ở đây)
                    String bankName = edtBankName.getText().toString();
                    String accountNumber = edtAccountNumber.getText().toString();
                    String cardExpiry = edtCardExpiry.getText().toString();
                    String cvv = edtCVV.getText().toString();

                    if (bankName.isEmpty() || accountNumber.isEmpty() || cardExpiry.isEmpty() || cvv.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin ngân hàng!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Hiển thị Toast ngay lập tức khi nhấn xác nhận
                        Toast.makeText(this, "Đơn hàng đã được thanh toán thành công!", Toast.LENGTH_SHORT).show();

                        // Sau đó mới tiến hành ghi dữ liệu lên Firestore
                        processPayment();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void processPayment() {
        double totalAmount = Math.round((managmentCart.getTotalFee() + tax + 15.0) * 100) / 100.0;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        OrderModel order = new OrderModel(userId, orderDate, managmentCart.getListCart(), totalAmount);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .add(order)
                .addOnSuccessListener(documentReference -> {
                    // Hiển thị Toast thông báo thanh toán thành công
                    Snackbar.make(binding.getRoot(), "Đơn hàng đã được thanh toán thành công!", Snackbar.LENGTH_LONG).show();

                    // Thêm độ trễ dài hơn trước khi chuyển sang OrderHistoryActivity
                    new Handler().postDelayed(() -> {
                        startActivity(new Intent(CardActivity.this, OrderHistoryActivity.class));
                        finish();
                    }, 2000); // Tăng độ trễ lên 2 giây
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Đã xảy ra lỗi khi thanh toán!", Toast.LENGTH_SHORT).show();
                });
    }






}