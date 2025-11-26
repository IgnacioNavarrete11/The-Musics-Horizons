package com.example.themusicshorizons.services.network;

import com.example.themusicshorizons.models.AudDResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AudDApi {

    @Multipart
    @POST("/")
    Call<AudDResponse> recognize(
        @Part("api_token") RequestBody apiToken,
        @Part MultipartBody.Part audioFile
    );
}
