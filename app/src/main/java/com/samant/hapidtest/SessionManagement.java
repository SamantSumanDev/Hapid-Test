package com.samant.hapidtest;

import android.content.Context;
import android.content.SharedPreferences;


public class SessionManagement {

    SharedPreferences sp;

    public SessionManagement(Context context) {
        sp = context.getSharedPreferences("Hapid TEst", Context.MODE_PRIVATE);
    }


    public void setOtp(String otp) throws Exception {
        setSharedPreferences("otp", otp);
    }

    public String getOtp() {
        return getDataFromSharedPreferences("otp");
    }

    public String removeOtp() {
        return removeDataFromSharedPreferences("otp");
    }

    public void setLastName(String lastName) throws Exception {
        setSharedPreferences("lastName", lastName);
    }

    public String getLastName() {
        return getDataFromSharedPreferences("lastName");
    }

    public String removeLastName() {
        return removeDataFromSharedPreferences("lastName");
    }

    public void setFirstName(String lastName) throws Exception {
        setSharedPreferences("firstName", lastName);
    }

    public String getFirstName() {
        return getDataFromSharedPreferences("firstName");
    }

    public String removeFirstName() {
        return removeDataFromSharedPreferences("firstName");
    }

    public void setPhone(String phone) throws Exception {
        setSharedPreferences("phone", phone);
    }

    public String getPhone() {
        return getDataFromSharedPreferences("phone");
    }

    public String removePhone() {
        return removeDataFromSharedPreferences("phone");
    }

    public void setPostCode(String postCode) throws Exception {
        setSharedPreferences("postCode", postCode);
    }

    public String getPostCode() {
        return getDataFromSharedPreferences("postCode");
    }

    public String removePostCode() {
        return removeDataFromSharedPreferences("postCode");
    }

    public void setMobieNo(String mobile_no) throws Exception {
        setSharedPreferences("mobile_no", mobile_no);
    }

    public String getMobileNo() {
        return getDataFromSharedPreferences("mobile_no");
    }

    public String removeMobileNo() {
        return removeDataFromSharedPreferences("mobile_no");
    }

    public void setProfilePic(String profilePic) throws Exception {
        setSharedPreferences("profilePic", profilePic);
    }
    public String getProfilePic() {
        return getDataFromSharedPreferences("profilePic");
    }

    public String removeProfilePic() {
        return removeDataFromSharedPreferences("profilePic");
    }


    public void setCity(String city) throws Exception {
        setSharedPreferences("city", city);
    }
    public String getCity() {
        return getDataFromSharedPreferences("city");
    }

    public String removeCity() {
        return removeDataFromSharedPreferences("city");
    }

    public void setSplash(String SplashActivity) throws Exception {
        setSharedPreferences("SplashActivity", SplashActivity);
    }
    public String getSplash(){
        return getDataFromSharedPreferences("SplashActivity");
    }

    public String removeSplash() {
        return removeDataFromSharedPreferences("SplashActivity");
    }


    private String getDataFromSharedPreferences(String Key) {
        try {
            String returnString = sp.getString(Key, null);
            return (returnString);
        } catch (Exception e) {
            return "";
        }
    }

    private void setSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void ClearSession() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    private String removeDataFromSharedPreferences(String Key) {
        try {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("key");
            editor.apply();
            return Key;
        } catch (Exception e) {
            return "";
        }


    }


}
