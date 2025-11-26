package com.example.themusicshorizons.services;

import com.example.themusicshorizons.models.AudDResponse;
import com.example.themusicshorizons.services.network.AudDApi;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AudDManager {

    private static final String API_BASE_URL = "https://api.audd.io/";
    // TODO: Reemplaza con tu propio API Token de AudD
    private static final String API_TOKEN = "REEMPLAZA_CON_TU_API_TOKEN";

    private AudDApi audDApi;

    public AudDManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        audDApi = retrofit.create(AudDApi.class);
    }

    public void recognize(File audioFile, Callback<AudDResponse> callback) {
        RequestBody apiTokenBody = RequestBody.create(MediaType.parse("text/plain"), API_TOKEN);
        RequestBody audioFileBody = RequestBody.create(MediaType.parse("audio/*"), audioFile);
        MultipartBody.Part audioFilePart = MultipartBody.Part.createFormData("file", audioFile.getName(), audioFileBody);

        Call<AudDResponse> call = audDApi.recognize(apiTokenBody, audioFilePart);
        call.enqueue(callback);
    }
}
