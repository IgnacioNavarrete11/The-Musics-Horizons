package com.example.themusicshorizons.models.ticketmaster;

// --- IMPORTACIONES DE HERRAMIENTAS ---
// No necesita ninguna importación porque solo usa tipos de datos básicos de Java (String).

// Esta clase representa un objeto "image" individual dentro de la respuesta de Ticketmaster.
// La API devuelve una lista de estas imágenes, generalmente en diferentes tamaños o proporciones.
public class Image {
    // La URL (dirección web) completa de la imagen.
    // La librería Glide usará esta URL para descargar y mostrar la imagen.
    public String url;
}
