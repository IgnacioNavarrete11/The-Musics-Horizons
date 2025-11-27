package com.example.themusicshorizons.services.network;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import com.example.themusicshorizons.models.AudDResponse; // El modelo que representa la respuesta de AudD.

import okhttp3.MultipartBody; // Herramienta de la librería OkHttp para enviar archivos (como el audio a reconocer).
import okhttp3.RequestBody; // Herramienta de OkHttp para enviar datos de texto plano (como la API Key).
import retrofit2.Call; // Herramienta de Retrofit que representa una llamada a la API lista para ser ejecutada.
import retrofit2.http.Multipart; // Anotación de Retrofit que indica que esta llamada enviará archivos y datos en formato "multipart/form-data".
import retrofit2.http.POST; // Anotación que indica que esta llamada es de tipo POST (enviar datos al servidor).
import retrofit2.http.Part; // Anotación que define una de las "partes" de la llamada Multipart (ej: la clave, el archivo, etc.).

// Esta interfaz es el "menú" de llamadas que se pueden hacer a la API de AudD usando Retrofit.
// No contiene lógica, solo define la estructura de la petición.
public interface AudDApi {

    // Define la llamada para el reconocimiento de audio.
    @Multipart
    @POST("/") // La llamada se hace a la raíz del servidor (ej: https://api.audd.io/).
    Call<AudDResponse> recognize(
        // Se define cada una de las "partes" que se envían al servidor.
        @Part("api_token") RequestBody apiToken, // Una parte de texto llamada "api_token" que contiene la clave.
        @Part("return") RequestBody returnParam, // Una parte de texto llamada "return" que le pide a la API que incluya datos de Spotify.
        @Part MultipartBody.Part audioFile // Una parte de archivo que contiene el audio a reconocer.
    );
}
