package com.example.dat_banh_fpoly.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
        // Inflate layout dialog từ XML
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_payment_confirmation, null);

        // Tạo AlertDialog với layout tùy chỉnh
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.CustomAlertDialog) // Đảm bảo sử dụng style nếu cần
                .setView(dialogView)
                .create();

        // Thiết lập dữ liệu cho dialog
        TextView tvMessage = dialogView.findViewById(R.id.tvMessage);
        tvMessage.setText("Tổng số tiền: $" + totalAmount + "\nBạn có chắc chắn muốn thanh toán không?");

        // Thiết lập sự kiện cho các nút trong dialog
        dialogView.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            // Xử lý khi nhấn nút Thanh toán
            dialog.dismiss();
            showBankInfoDialog();
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            // Đóng dialog khi nhấn nút Hủy
            dialog.dismiss();
        });

        // Hiển thị dialog
        dialog.show();
    }

    private void showBankInfoDialog() {
        // Tạo layout cho dialog nhập thông tin ngân hàng từ file XML
        View bankInfoView = getLayoutInflater().inflate(R.layout.dialog_bank_info, null);

        // Ánh xạ các view trong layout
        EditText edtBankName = bankInfoView.findViewById(R.id.edtBankName);
        EditText edtAccountNumber = bankInfoView.findViewById(R.id.edtAccountNumber);
        EditText edtCardExpiry = bankInfoView.findViewById(R.id.edtCardExpiry);
        EditText edtCVV = bankInfoView.findViewById(R.id.edtCVV);
        EditText edtZipCode = bankInfoView.findViewById(R.id.edtZipCode);
        Spinner spnCountry = bankInfoView.findViewById(R.id.spnCountry);
        CheckBox chkSavePayment = bankInfoView.findViewById(R.id.chkSavePayment);
        Button btnPay = bankInfoView.findViewById(R.id.btnPay);
        Button btnHuy = bankInfoView.findViewById(R.id.btnHuy);

        // Tạo AlertDialog với layout tùy chỉnh
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(bankInfoView)
                .setCancelable(false) // Tùy chọn: không cho phép tắt dialog khi nhấn ra ngoài
                .create();

        // Thiết lập sự kiện cho nút Pay
        btnPay.setOnClickListener(v -> {
            // Lấy dữ liệu từ các trường nhập liệu
            String bankName = edtBankName.getText().toString();
            String accountNumber = edtAccountNumber.getText().toString();
            String cardExpiry = edtCardExpiry.getText().toString();
            String cvv = edtCVV.getText().toString();
            String zipCode = edtZipCode.getText().toString();

            if (bankName.isEmpty() || accountNumber.isEmpty() || cardExpiry.isEmpty() || cvv.isEmpty() || zipCode.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin thanh toán!", Toast.LENGTH_SHORT).show();
            } else {
                // Thông báo thanh toán thành công
                Toast.makeText(this, "Đơn hàng đã được thanh toán thành công!", Toast.LENGTH_SHORT).show();
                // Đóng dialog
                dialog.dismiss();
                // Gọi hàm xử lý thanh toán (nếu có)
                processPayment();
            }
        });

        // Thiết lập sự kiện cho nút Cancel
        btnHuy.setOnClickListener(v -> dialog.dismiss());

        // Hiển thị dialog
        dialog.show();
        // Chỉnh chiều rộng của dialog thành toàn màn hình
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent); // Loại bỏ nền mặc định của dialog
            window.setGravity(Gravity.BOTTOM); // Căn dialog từ dưới lên (nếu cần)
        }

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