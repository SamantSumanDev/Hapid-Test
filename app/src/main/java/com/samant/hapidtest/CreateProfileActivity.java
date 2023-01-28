package com.samant.hapidtest;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.location.aravind.getlocation.GeoLocator;
import com.samant.hapidtest.retrofit.ApiInterface;
import com.samant.hapidtest.retrofit.ApiUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfileActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    AppCompatEditText edtFirstName, edtLastName, edtPhone, edtPostCode;
    MaterialCardView crdLocation;
    AppCompatTextView txtLocation;
    AppCompatButton btnSubmit;
    AppCompatImageView imgProfile;
    MaterialCardView crdProfile;
    AppCompatImageView back_arrow;
    private static final int PICK_IMAGE = 100;
    SessionManagement sessionManagement;

    //  List<profileModel> itemList = new ArrayList<>();
    ProgressBar pbr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        sessionManagement = new SessionManagement(getApplicationContext());
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();

        if (sessionManagement.getFirstName() != null && sessionManagement.getLastName() != null && sessionManagement.getPhone() != null && sessionManagement.getCity() != null && sessionManagement.getPostCode() != null) {
            btnSubmit.setVisibility(View.GONE);
            pbr.setVisibility(View.GONE);
            try {
                edtFirstName.setText(sessionManagement.getFirstName());
                edtLastName.setText(sessionManagement.getLastName());
                edtPhone.setText(sessionManagement.getPhone());
                txtLocation.setText(sessionManagement.getCity());
                edtPostCode.setText(sessionManagement.getPostCode());

            } catch (Exception e) {
                Log.e(" Created Profile", e.getMessage());
            }

        }

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManagement.removeMobileNo();
                sessionManagement.removeOtp();
                Intent i = new Intent(CreateProfileActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        try {
            String uploadedPic = sessionManagement.getProfilePic();
            if (!uploadedPic.equalsIgnoreCase("")) {
                byte[] b = Base64.decode(uploadedPic, Base64.DEFAULT);
                Bitmap photo = BitmapFactory.decodeByteArray(b, 0, b.length);
                imgProfile.setImageBitmap(photo);
                imgProfile.setImageTintList(null);
                ViewGroup.LayoutParams params = imgProfile.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                imgProfile.setLayoutParams(params);
                imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } else {
            }

            String uloadedCity = sessionManagement.getCity();
            if (!uloadedCity.equalsIgnoreCase("")) {
                txtLocation.setText(uloadedCity);
            } else {
            }
        } catch (Exception e) {
            Log.e("uploaded: ", e.getMessage());
        }
        crdProfile.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              sessionManagement.removeProfilePic();
                                              if (checkPermission()) {
                                                  crdProfile.setEnabled(false);
                                                  Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                  startActivityForResult(intent, PICK_IMAGE);
                                                  crdProfile.setEnabled(true);
                                              } else {
                                                  requestPermission();
                                              }
                                          }
                                      }
        );

        crdLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isWifiConnected() || isMobileDataConnected()) {
                    if (checkPermission()) {
                        sessionManagement.removeCity();
                        GeoLocator geoLocator = new GeoLocator(getApplicationContext(), CreateProfileActivity.this);
                        // String city = geoLocator.getAddress();
                        String city = geoLocator.getCity();
                        try {
                            sessionManagement.setCity(city);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(CreateProfileActivity.this, "location Picked", Toast.LENGTH_SHORT).show();
                        txtLocation.setText(sessionManagement.getCity());

                    } else {
                        requestPermission();
                    }
                } else {
                    Toast.makeText(CreateProfileActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit.setEnabled(false);
                btnSubmit.setVisibility(View.GONE);
                pbr.setVisibility(View.VISIBLE);
                String firstName = Objects.requireNonNull(edtFirstName.getText()).toString().trim();
                String lastName = edtLastName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String city = sessionManagement.getCity().trim();
                String postCode = edtPostCode.getText().toString().trim();

                if (firstName != null && lastName != null && phone != null && city != null && postCode != null && firstName.length() != 0 && lastName.length() != 0 && phone.length() != 0 && city.length() != 0 && postCode.length() != 0) {
                    btnSubmit.setVisibility(View.GONE);
                    pbr.setVisibility(View.GONE);
                    try {
                        sessionManagement.setFirstName(firstName);
                        sessionManagement.setLastName(lastName);
                        sessionManagement.setPhone(phone);
                        sessionManagement.setPostCode(postCode);
                        sessionManagement.setCity(city);
                        Toast.makeText(CreateProfileActivity.this, "Submited Sccessfully", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Log.e("onClick: Create Profile", e.getMessage());
                    }

                } else {
                    Toast.makeText(CreateProfileActivity.this, "Please Enter Full Form", Toast.LENGTH_SHORT).show();
                    btnSubmit.setEnabled(true);
                    btnSubmit.setVisibility(View.VISIBLE);
                    pbr.setVisibility(View.GONE);
                }


                // Api UnAvailable. Methods are created
                //createProfile(firstName,lastName,phone,city,postCode);
            }
        });
    }

/*

    private void createProfile(String firstName, String lastName, String phone, String city, String postCode) {
        ApiInterface apiInterface = ApiUtils.getData();
        JsonObject postJson = new JsonObject();
        postJson.addProperty("firstName", firstName);
        postJson.addProperty("lastName", lastName);
        postJson.addProperty("phone", phone);
        postJson.addProperty("city", city);
        postJson.addProperty("postCode", postCode);
       // Call<List<profileModel>> call = apiInterface.getCreateprofile(postJson);
        btnSubmit.setVisibility(View.GONE);
        pbr.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<profileModel>>() {
            @Override
            public void onResponse(Call<List<profileModel>> call, Response<List<profileModel>> response) {
                if (response.isSuccessful()) {
                    try {
                        itemList = response.body();
                        if (itemList != null && itemList.size() > 0 && itemList.get(0).condition) {
                            Toast.makeText(CreateProfileActivity.this, "Submited SuccessFully", Toast.LENGTH_SHORT).show();
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                      //  sessionManagement.setUserName(itemList.get(0).user);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            btnSubmit.setVisibility(View.VISIBLE);
                            pbr.setVisibility(View.GONE);
                            Toast.makeText(CreateProfileActivity.this, itemList.get(0).message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(CreateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    btnSubmit.setEnabled(true);
                }
            }
            @Override
            public void onFailure(Call<List<profileModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error :", Toast.LENGTH_SHORT).show();
                btnSubmit.setVisibility(View.GONE);
                pbr.setVisibility(View.VISIBLE);
                btnSubmit.setEnabled(true);
            }
        });
    }
*/


    public void init() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPostCode = findViewById(R.id.edtPostCode);
        crdLocation = findViewById(R.id.crdLocation);
        txtLocation = findViewById(R.id.txtLocation);
        btnSubmit = findViewById(R.id.btnSubmit);
        imgProfile = findViewById(R.id.imgProfile);
        crdProfile = findViewById(R.id.crdProfile);
        back_arrow = findViewById(R.id.back_arrow);
        pbr = findViewById(R.id.pbr);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                imgProfile.setImageBitmap(bitmap);
                imgProfile.setImageTintList(null);
                ViewGroup.LayoutParams params = imgProfile.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                imgProfile.setLayoutParams(params);
                imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);

                //compress and encode
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                try {
                    sessionManagement.setProfilePic(encodedImage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(this, "Pic Set Successfully", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    private boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnected();
    }

    private boolean isMobileDataConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE && activeNetwork.isConnected();
    }


}