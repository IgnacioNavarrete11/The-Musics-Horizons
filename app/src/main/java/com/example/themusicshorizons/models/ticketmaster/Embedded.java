package com.example.themusicshorizons.models.ticketmaster;

import java.util.List;

// Esta clase representa el objeto "_embedded" dentro de la respuesta de Ticketmaster.
// Su única misión es contener la lista de eventos.
public class Embedded {
    public List<Event> events;
}
