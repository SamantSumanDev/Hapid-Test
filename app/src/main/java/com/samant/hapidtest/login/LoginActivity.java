package com.samant.hapidtest.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.samant.hapidtest.BaseActivity;
import com.samant.hapidtest.R;
import com.samant.hapidtest.sessionManagement.SessionManagement;
import com.samant.hapidtest.splash.SplashActivity;

import java.util.Objects;

public class LoginActivity extends BaseActivity {
    AppCompatButton btnRequestOtp;
    AppCompatTextView txtPrivactyPolicy;
    MaterialCardView CrdGoogle;
    AppCompatEditText edtMobileNo;
    SessionManagement sessionManagement;
    AppCompatImageView back_arrow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        sessionManagement = new SessionManagement(getApplicationContext());

        init();

        //method for generate otp.
        popOtp();
        privacyPolicy();

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SplashActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void init(){
        back_arrow = findViewById(R.id.back_arrow);
        btnRequestOtp = findViewById(R.id.btnRequestOtp);
        txtPrivactyPolicy = findViewById(R.id.txtPrivactyPolicy);
        CrdGoogle = findViewById(R.id.CrdGoogle);
        edtMobileNo = findViewById(R.id.edtMobileNo);
    }
    public void popOtp(){
        btnRequestOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRequestOtp.setEnabled(false);
                String mobileNumber = Objects.requireNonNull(edtMobileNo.getText()).toString().trim();
                if (mobileNumber != null && mobileNumber.length() == 10){
                   // btnRequestOtp.setVisibility(View.GONE);
                    String otp = mobileNumber.substring(0, 2) + mobileNumber.substring(mobileNumber.length() - 2);
                    try {
                        sessionManagement.setMobieNo(mobileNumber);
                        sessionManagement.setOtp(otp);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    showDialog();
                }else{
                    Toast.makeText(LoginActivity.this, "Please Enter Correct No.", Toast.LENGTH_SHORT).show();
                    btnRequestOtp.setVisibility(View.VISIBLE);
                    btnRequestOtp.setEnabled(true);
                }
            }
        });
    }
    private void privacyPolicy() {
        String text_privacy_title = getResources().getString(R.string.login_term_privacy);
        SpannableString spannableString = new SpannableString(text_privacy_title);

        ForegroundColorSpan txt_privacy_polivy_color_light_black = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.light_black));
        ForegroundColorSpan txt_privacy_polivy_color_light_black_1 = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.light_black));
        ForegroundColorSpan txt_privacy_polivy_color_pink = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.pink));
        ForegroundColorSpan txt_privacy_polivy_color_pink_1 = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.pink));

        spannableString.setSpan(txt_privacy_polivy_color_light_black, 0, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(txt_privacy_polivy_color_pink, 40, 58, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(txt_privacy_polivy_color_light_black_1, 59, 61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(txt_privacy_polivy_color_pink_1, 62, 77, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtPrivactyPolicy.setText(spannableString);
    }

    public void showDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.confirmation_code_view);
        dialog.setTitle("Custom Dialog");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        AppCompatTextView txtOtp = dialog.findViewById(R.id.txtOtp);
        txtOtp.setText(sessionManagement.getOtp());

        // Set the size and position of the dialog
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        // Show the dialog
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        if (dialog.isShowing()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    Intent i = new Intent(LoginActivity.this,OtpActivity.class);
                    startActivity(i);
                    finish();
                    btnRequestOtp.setEnabled(true);
                }
            }, 2000);
        }

    }

}