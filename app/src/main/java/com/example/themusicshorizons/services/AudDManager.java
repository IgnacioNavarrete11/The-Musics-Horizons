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

// Esta clase actúa como un intermediario o "manager" para comunicarse con la API de AudD.
// Su propósito es simplificar la forma en que el resto de la app interactúa con la API.
public class AudDManager {

    // La URL base del servidor de la API de AudD.
    private static final String API_BASE_URL = "https://api.audd.io/";
    // La clave secreta para autenticarse con la API. ¡Esta clave nunca debe ser pública!
    private static final String API_TOKEN = "";

    // Una instancia de la interfaz que define las llamadas a la API.
    private AudDApi audDApi;

    // El constructor de la clase. Se ejecuta cuando se crea un nuevo AudDManager.
    public AudDManager() {
        // Configura Retrofit, la librería que facilita las llamadas a la API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL) // Le dice a Retrofit cuál es el servidor al que debe apuntar.
                .addConverterFactory(GsonConverterFactory.create()) // Le dice que use la librería Gson para convertir las respuestas JSON a objetos Java.
                .build();

        // Crea una implementación concreta de la interfaz AudDApi.
        audDApi = retrofit.create(AudDApi.class);
    }

    // Este método se encarga de reconocer una canción a partir de un archivo de audio.
    public void recognize(File audioFile, Callback<AudDResponse> callback) {
        // Empaqueta la clave de la API y el archivo de audio en el formato que la API espera (Multipart).
        RequestBody apiTokenBody = RequestBody.create(MediaType.parse("text/plain"), API_TOKEN);
        RequestBody audioFileBody = RequestBody.create(MediaType.parse("audio/*"), audioFile);
        MultipartBody.Part audioFilePart = MultipartBody.Part.createFormData("file", audioFile.getName(), audioFileBody);
        // Le pide explícitamente a la API que incluya los datos de Spotify en la respuesta.
        RequestBody returnParam = RequestBody.create(MediaType.parse("text/plain"), "spotify");

        // Realiza la llamada a la API de forma asíncrona (en segundo plano) y notifica el resultado a través del callback.
        Call<AudDResponse> call = audDApi.recognize(apiTokenBody, returnParam, audioFilePart);
        call.enqueue(callback);
    }
}
