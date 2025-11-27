package com.example.themusicshorizons.models.ticketmaster;

import java.util.List;

// Esta clase es el corazón de la respuesta de Ticketmaster. Representa un único evento.
public class Event {
    // El ID único del evento, crucial para guardarlo y reconocerlo en nuestra base de datos.
    public String id;
    // El nombre del evento (ej: "Metallica World Tour").
    public String name;
    // La URL a la página del evento en la web de Ticketmaster.
    public String url;
    // Una lista de imágenes asociadas al evento, en diferentes tamaños.
    public List<Image> images;
    // Un objeto que contiene las fechas del evento.
    public Dates dates;
    // Un objeto anidado que contiene información sobre el lugar del evento.
    public EmbeddedVenues _embedded;
}
