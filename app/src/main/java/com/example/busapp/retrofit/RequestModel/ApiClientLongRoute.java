package com.example.busapp.retrofit.RequestModel;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientLongRoute {

    private static Retrofit retrofit;

//Define the base URL//

    private static final String BASE_URL = "http://10.0.2.2:8000/";

//Create the Retrofit instance//

    public Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)



                    .addConverterFactory(GsonConverterFactory.create())

//Build the Retrofit instance//

                    .build();
        }
        return retrofit;
    }
}
