package com.example.themusicshorizons.models;

// Esta clase representa el objeto "result" dentro de la respuesta de AudD.
// Contiene todos los detalles de la canción que ha sido reconocida.
public class AudDResult {
    // El nombre del artista o banda.
    public String artist;
    // El título de la canción.
    public String title;
    // El nombre del álbum al que pertenece la canción.
    public String album;
    // Un objeto que contiene datos específicos de Spotify (carátulas, enlaces, etc.).
    public SpotifyData spotify;
    // Una URL genérica a la carátula del álbum. Se usa como alternativa si no se encuentran datos de Spotify.
    public String artwork;

    // --- Clases internas que definen la estructura de los datos de Spotify ---

    // Contiene la información del álbum de Spotify.
    public class SpotifyData {
        public AlbumData album;
        public ExternalUrls external_urls;
    }

    // Contiene una lista de las imágenes del álbum en diferentes tamaños.
    public class AlbumData {
        public Image[] images;
    }

    // Representa una imagen individual del álbum, con su URL.
    public class Image {
        public String url;
    }

    // Contiene los enlaces a la canción o álbum en la plataforma de Spotify.
    public class ExternalUrls {
        public String spotify;
    }
}
