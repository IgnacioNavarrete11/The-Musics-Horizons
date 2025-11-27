package com.example.themusicshorizons.models.ticketmaster;

// Esta clase representa la respuesta completa que envía la API de Ticketmaster.
// Su propósito es actuar como el "contenedor" principal para el resto de los datos del evento.
public class TicketmasterResponse {
    // La API de Ticketmaster anida la lista de eventos dentro de un objeto llamado "_embedded".
    public Embedded _embedded;
}
