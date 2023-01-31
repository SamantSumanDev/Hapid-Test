package com.samant.hapidtest.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.samant.hapidtest.BaseActivity;
import com.samant.hapidtest.profile.CreateProfileActivity;
import com.samant.hapidtest.R;
import com.samant.hapidtest.sessionManagement.SessionManagement;

import java.util.Objects;

public class OtpActivity extends BaseActivity {

    AppCompatTextView txtViewReceiveOtp;
    AppCompatTextView txtotp1, txtotp2, txtotp3, txtotp4;
    private String otp, otp1, otp2, otp3, otp4;
    AppCompatButton btnSubmit;
    AppCompatTextView txtShowMobileNo;
    AppCompatImageView imgEdtMobileNo;
    SessionManagement sessionManagement;
    StringBuffer sb = new StringBuffer();
    ProgressBar pbr;
    AppCompatImageView back_arrow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Objects.requireNonNull(getSupportActionBar()).hide();
        sessionManagement = new SessionManagement(getApplicationContext());
        init();
        checkdOtp();


        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManagement.removeMobileNo();
                sessionManagement.removeOtp();
                Intent i = new Intent(OtpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        txtShowMobileNo.setText("+91 " + sessionManagement.getMobileNo());
        txtresendOtp();
        imgEdtMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManagement.removeMobileNo();
                sessionManagement.removeOtp();
                Intent i = new Intent(OtpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit.setEnabled(false);
                pbr.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.GONE);
                try {
                    otp1 = txtotp1.getText().toString();
                    otp2 = txtotp2.getText().toString();
                    otp3 = txtotp3.getText().toString();
                    otp4 = txtotp4.getText().toString();
                    sb.append(otp1);
                    sb.append(otp2);
                    sb.append(otp3);
                    sb.append(otp4);
                    otp = sb.toString();
                    int count = 0;
                    if (otp != null && Objects.equals(otp, sessionManagement.getOtp())) {
                        sessionManagement.removeOtp();

                        otp = null;
                        otp1 = null;
                        otp2 = null;
                        otp3 = null;
                        otp4 = null;
                        sb.delete(0, sb.length());
                        txtotp1.setText(null);
                        txtotp2.setText(null);
                        txtotp3.setText(null);
                        txtotp4.setText(null);
                        sessionManagement.removeOtp();
                        sessionManagement.removeMobileNo();
                        String splash = "splash";
                        try {
                            sessionManagement.setSplash(splash);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(OtpActivity.this, "Login Succesfully", Toast.LENGTH_SHORT).show();

                        Intent mainIntent = new Intent(OtpActivity.this, CreateProfileActivity.class);
                        startActivity(mainIntent);
                        finish();
                        btnSubmit.setEnabled(true);
                        pbr.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(OtpActivity.this, "wrong otp", Toast.LENGTH_SHORT).show();
                        btnSubmit.setEnabled(true);
                        count++;
                        if (count >= 3) {
                            btnSubmit.setEnabled(false);
                            Toast.makeText(OtpActivity.this, "Your Try Over!", Toast.LENGTH_SHORT).show();
                        }
                        pbr.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.VISIBLE);
                        otp = null;
                        otp1 = null;
                        otp2 = null;
                        otp3 = null;
                        otp4 = null;
                        sb.delete(0, sb.length());
                        txtotp1.setText(null);
                        txtotp2.setText(null);
                        txtotp3.setText(null);
                        txtotp4.setText(null);
                        txtotp1.setFocusable(true);

                    }
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }


            }
        });
    }

    public void init() {
        back_arrow = findViewById(R.id.back_arrow);
        txtViewReceiveOtp = findViewById(R.id.txtViewReceiveOtp);
        txtotp1 = findViewById(R.id.otp1);
        txtotp2 = findViewById(R.id.otp2);
        txtotp3 = findViewById(R.id.otp3);
        txtotp4 = findViewById(R.id.otp4);
        btnSubmit = findViewById(R.id.btnSubmit);
        txtShowMobileNo = findViewById(R.id.txtShowMobileNo);
        imgEdtMobileNo = findViewById(R.id.imgEdtMobileNo);
        pbr = findViewById(R.id.pbr);
    }

    private void txtresendOtp() {
        String text_otp_title = getResources().getString(R.string.otp_dont_receive_otp);
        SpannableString spannableString = new SpannableString(text_otp_title);
        ForegroundColorSpan txt_privacy_polivy_color_light_black = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.light_black));
        ForegroundColorSpan txt_privacy_polivy_color_pink = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.pink));
        spannableString.setSpan(txt_privacy_polivy_color_light_black, 0, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(txt_privacy_polivy_color_pink, 18, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtViewReceiveOtp.setText(spannableString);

    }

    private void checkdOtp() {
        txtotp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    txtotp2.requestFocus();
                }
            }
        });
        txtotp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    txtotp3.requestFocus();
                }
            }
        });
        txtotp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1) {
                    txtotp4.requestFocus();
                }
            }
        });
        txtotp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {

                }
            }
        });
    }


}