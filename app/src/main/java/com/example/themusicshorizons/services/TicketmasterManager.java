package com.example.themusicshorizons.services;

import com.example.themusicshorizons.models.ticketmaster.TicketmasterResponse;
import com.example.themusicshorizons.services.network.TicketmasterApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Esta clase actúa como un intermediario o "manager" para comunicarse con la API de Ticketmaster.
// Su propósito es simplificar la forma en que el resto de la app interactúa con la API.
public class TicketmasterManager {

    // La URL base del servidor de la API de Ticketmaster.
    private static final String API_BASE_URL = "https://app.ticketmaster.com/";
    // La clave pública para autenticarse con la API. ¡Esta clave es segura para estar en el código!
    private static final String API_KEY = "";

    private TicketmasterApi ticketmasterApi;

    public TicketmasterManager() {
        // Configura Retrofit, la librería que facilita las llamadas a la API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // Le dice que use Gson para convertir las respuestas JSON a objetos Java.
                .build();

        ticketmasterApi = retrofit.create(TicketmasterApi.class);
    }

    // Este método se encarga de buscar eventos musicales cerca de una ubicación GPS específica.
    public void findMusicEventsByLocation(double latitude, double longitude, Callback<TicketmasterResponse> callback) {
        String latLong = latitude + "," + longitude;
        String radius = "500"; // Define un radio de búsqueda de 500 millas.
        String unit = "miles";
        String classificationName = "Music"; // Filtra para que solo devuelva eventos musicales.
        String sort = "date,asc"; // Ordena los resultados por fecha, del más cercano al más lejano.

        // Realiza la llamada a la API de forma asíncrona y notifica el resultado a través del callback.
        Call<TicketmasterResponse> call = ticketmasterApi.findEventsByLocation(API_KEY, latLong, radius, unit, classificationName, sort);
        call.enqueue(callback);
    }

    // Este método se encarga de buscar eventos musicales usando una palabra clave (ej: ciudad o artista).
    public void findMusicEventsByKeyword(String keyword, Callback<TicketmasterResponse> callback) {
        String classificationName = "Music"; // Filtra para que solo devuelva eventos musicales.
        String sort = "date,asc"; // Ordena los resultados por fecha.

        // Realiza la llamada a la API de forma asíncrona.
        Call<TicketmasterResponse> call = ticketmasterApi.findEventsByKeyword(API_KEY, keyword, classificationName, sort);
        call.enqueue(callback);
    }
}
