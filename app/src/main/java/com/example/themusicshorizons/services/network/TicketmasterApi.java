package com.example.themusicshorizons.services.network;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import com.example.themusicshorizons.models.ticketmaster.TicketmasterResponse; // Nuestro propio modelo que representa la respuesta completa de Ticketmaster.

import retrofit2.Call; // Herramienta de Retrofit que representa una llamada a la API lista para ser ejecutada.
import retrofit2.http.GET; // Anotación que indica que esta llamada es de tipo GET (solicitar datos de un servidor).
import retrofit2.http.Query; // Anotación para añadir parámetros a la URL (ej: ?apikey=XXXX&city=London).

// Esta interfaz es el "menú" de llamadas que podemos hacerle a la API de Ticketmaster usando Retrofit.
// No contiene lógica, solo define la estructura de las peticiones.
public interface TicketmasterApi {

    // Define la llamada para buscar eventos por ubicación GPS.
    @GET("discovery/v2/events.json") // La llamada se hace a la ruta específica de eventos dentro del servidor.
    Call<TicketmasterResponse> findEventsByLocation(
        // Se definen los parámetros que se añadirán a la URL de la petición.
        @Query("apikey") String apiKey, // La clave de API para autenticarse.
        @Query("latlong") String latLong, // Las coordenadas de latitud y longitud.
        @Query("radius") String radius, // El radio de búsqueda.
        @Query("unit") String unit, // La unidad de medida del radio (ej: "miles").
        @Query("classificationName") String classificationName, // Filtra por tipo de evento (en nuestro caso, "Music").
        @Query("sort") String sort // Ordena los resultados.
    );

    // Define la llamada para buscar eventos por una palabra clave.
    @GET("discovery/v2/events.json")
    Call<TicketmasterResponse> findEventsByKeyword(
        @Query("apikey") String apiKey,
        @Query("keyword") String keyword, // La palabra clave a buscar (ej: "Santiago" o "Metallica").
        @Query("classificationName") String classificationName,
        @Query("sort") String sort
    );
}
