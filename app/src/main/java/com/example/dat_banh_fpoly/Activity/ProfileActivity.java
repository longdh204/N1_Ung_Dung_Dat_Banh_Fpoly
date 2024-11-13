package com.example.dat_banh_fpoly.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dat_banh_fpoly.Login.Login;
import com.example.dat_banh_fpoly.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ TextView từ layout
        profileEmail = findViewById(R.id.profile_email);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Lấy người dùng hiện tại
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            profileEmail.setText(currentUser.getEmail());
        } else {
            Intent intent = new Intent(ProfileActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        // Nút đổi mật khẩu
        findViewById(R.id.change_Button).setOnClickListener(v -> showChangePasswordDialog());

        // Xử lý nút "Back to Main"
        findViewById(R.id.back_to_main_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay về Activity chính
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý nút "Logout"
        LinearLayout logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(ProfileActivity.this, Login.class);
            startActivity(intent);
            finish();
        });
    }

    private void showChangePasswordDialog() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Tạo AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Đổi mật khẩu");

            // Inflate layout dialog từ XML
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_repass_dialog, null);
            builder.setView(dialogView);

            AlertDialog dialog = builder.create(); // Tạo AlertDialog từ builder

            EditText oldPasswordInput = dialogView.findViewById(R.id.old_password_input);
            EditText newPasswordInput = dialogView.findViewById(R.id.new_password_input);
            EditText confirmNewPasswordInput = dialogView.findViewById(R.id.confirm_new_password_input);

            // Xử lý nút "Repass" (Đổi mật khẩu)
            dialogView.findViewById(R.id.Repass).setOnClickListener(v -> {
                String oldPassword = oldPasswordInput.getText().toString();
                String newPassword = newPasswordInput.getText().toString();
                String confirmNewPassword = confirmNewPasswordInput.getText().toString();

                if (newPassword.isEmpty() || confirmNewPassword.isEmpty() || oldPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmNewPassword)) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu mới không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Xác thực người dùng với mật khẩu hiện tại
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
                user.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss(); // Đóng dialog sau khi đổi mật khẩu thành công
                            } else {
                                Toast.makeText(getApplicationContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            // Xử lý nút "Cancel" (Hủy)
            dialogView.findViewById(R.id.cancel).setOnClickListener(v -> dialog.dismiss());

            dialog.show(); // Hiển thị dialog
        } else {
            Toast.makeText(getApplicationContext(), "Không có người dùng đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

}
