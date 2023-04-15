package com.example.busapp.retrofit.ApiEndpoints;

import com.example.busapp.retrofit.ApiModels.ShortRouteModel;
import com.example.busapp.retrofit.ApiModels.ShortRoutePointModel;
import com.example.busapp.retrofit.ApiModels.ShortRouteTicketModel;
import com.example.busapp.retrofit.ApiModels.TicketRequestBody;
import com.example.busapp.retrofit.ApiModels.TicketRequestBodyMultiple;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ShortRouteApi {
    @GET("routes/routes/")
    Call<ShortRouteModel> getShortRoutes(@Header("Authorization") String token);

    @GET("routes/points/")
    Call<ShortRoutePointModel> getShortRoutePoints(@Header("Authorization") String token);

    @POST("ticket/ticket/")
    Call<ShortRouteTicketModel> setSoldTicket(
            @Header("Authorization") String token,
            @Body TicketRequestBody requestBody
    );

    @POST("ticket/ticket_multiple/")
    Call<TicketRequestBodyMultiple> postMultipleTickets(
            @Header("Authorization") String token,
            @Body TicketRequestBodyMultiple ticketRequestModel
    );

}
