package com.samant.hapidtest.retrofit;

public class ApiUtils {

        private static final String Base_url="paste_base_url";
        public static ApiInterface getData(){

            return RetrofitClient.getClient(Base_url).create(ApiInterface.class);
        }

}
