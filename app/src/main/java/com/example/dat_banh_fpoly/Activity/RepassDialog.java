package com.example.dat_banh_fpoly.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dat_banh_fpoly.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RepassDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Tạo AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Inflate giao diện từ layout XML
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.activity_repass_dialog, null);
            builder.setView(dialogView);

            // Lấy các trường nhập mật khẩu
            EditText oldPasswordInput = dialogView.findViewById(R.id.old_password_input);
            EditText newPasswordInput = dialogView.findViewById(R.id.new_password_input);
            EditText confirmNewPasswordInput = dialogView.findViewById(R.id.confirm_new_password_input);

            builder.setPositiveButton("", (dialog, which) -> {
                String oldPassword = oldPasswordInput.getText().toString().trim();
                String newPassword = newPasswordInput.getText().toString().trim();
                String confirmNewPassword = confirmNewPasswordInput.getText().toString().trim();

                // Kiểm tra các trường nhập mật khẩu
                if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmNewPassword)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (newPassword.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
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
                        // Sau khi xác thực thành công, đổi mật khẩu
                        user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Đổi mật khẩu thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
            builder.show();
        } else {
            Toast.makeText(getApplicationContext(), "Không có người dùng đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }
}
