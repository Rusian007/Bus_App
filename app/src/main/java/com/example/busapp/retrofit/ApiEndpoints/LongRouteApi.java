package com.example.busapp.retrofit.ApiEndpoints;

import com.example.busapp.retrofit.ApiModels.CreateTicketRequest;
import com.example.busapp.retrofit.ApiModels.DiscountLimitResponse;
import com.example.busapp.retrofit.ApiModels.GetBookedSeatsModel;
import com.example.busapp.retrofit.ApiModels.GetFairModel;
import com.example.busapp.retrofit.ApiModels.GetTicketBody;
import com.example.busapp.retrofit.ApiModels.LongRouteBusModel;
import com.example.busapp.retrofit.ApiModels.LongRouteModel;
import com.example.busapp.retrofit.ApiModels.LongRouteSeatModel;
import com.example.busapp.retrofit.ApiModels.PhoneNumberResponse;
import com.example.busapp.retrofit.ApiModels.RouteRequestModel;
import com.example.busapp.retrofit.ApiModels.TicketResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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
            @Query("date") String date,
            @Query("route_id") int route_id
    );

    @FormUrlEncoded
    @POST("ticket/get_fair/")
    Call<GetFairModel> getFair(
            @Header("Authorization") String token,
            @Field("starting_location_id") int startingLocationId,
            @Field("ending_location_id") int endingLocationId,
            @Field("bus_id") int busId,
            @Field("seats") String seats
    );

    @POST("ticket/create_ticket/")
    Call<TicketResponse> makeTicket(
            @Header("Authorization") String token,
            @Body CreateTicketRequest request
    );

    @GET("ticket/get_ticket")
    Call<GetTicketBody> getTicket(@Query("ticket_id") int ticketId, @Header("Authorization") String token);

    @GET("route/get_route/")
    Call<RouteRequestModel>GetRouteID(
            @Header("Authorization") String token,
            @Query("starting_location_id") int starting_location_id,
            @Query("ending_location_id") int ending_location_id
    );

    @GET("ticket/phone-number/")
    Call<PhoneNumberResponse>getPhone();

    @GET("ticket/discount-max-limit")
    Call<DiscountLimitResponse>getDiscount();
}
