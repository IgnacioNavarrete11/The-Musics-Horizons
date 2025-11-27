package com.example.themusicshorizons.models;

// Esta clase representa la respuesta completa que envía la API de AudD.
// Su propósito es actuar como un "contenedor" principal para el resultado del reconocimiento.
public class AudDResponse {
    // El estado de la respuesta (ej: "success" o "error").
    public String status;
    // El objeto que contiene los detalles de la canción encontrada. Puede ser nulo si no se encuentra nada.
    public AudDResult result;
}
