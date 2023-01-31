package com.samant.hapidtest.retrofit;

public class ApiUtils {

        private static final String Base_url="https://reqres.in";
        public static ApiInterface getData(){

            return RetrofitClient.getClient(Base_url).create(ApiInterface.class);
        }

}
