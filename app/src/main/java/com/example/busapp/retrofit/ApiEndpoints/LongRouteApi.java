package com.example.busapp.retrofit.ApiEndpoints;

import com.example.busapp.retrofit.ApiModels.GetBookedSeatsModel;
import com.example.busapp.retrofit.ApiModels.LongRouteBusModel;
import com.example.busapp.retrofit.ApiModels.LongRouteModel;
import com.example.busapp.retrofit.ApiModels.LongRouteSeatModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface LongRouteApi {
    @GET("route/locations/")
    Call<LongRouteModel> getLongRoutes(@Header("Authorization") String token);

    @GET("bus/buses/")
    Call<LongRouteBusModel> getLongRouteBuses(@Header("Authorization") String token);

    @GET("bus/seat_structure/")
    Call<LongRouteSeatModel> getLongRouteSeatStructure(@Header("Authorization") String token, @Query("bus_id") int bus_id);

    @GET("ticket/booked_seats/")
    Call<GetBookedSeatsModel> getBookedSeats(
            @Header("Authorization") String token,
            @Query("bus_id") int busId,
            @Query("date") String date
    );

}
