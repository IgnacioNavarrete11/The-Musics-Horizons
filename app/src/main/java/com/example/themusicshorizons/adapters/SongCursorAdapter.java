package com.example.themusicshorizons.adapters;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.content.Context; // Proporciona acceso a recursos y servicios del sistema Android.
import android.content.Intent; // Herramienta para abrir otras pantallas o aplicaciones.
import android.database.Cursor; // Herramienta para leer los resultados de una consulta a la base de datos, fila por fila.
import android.view.LayoutInflater; // Herramienta para "inflar" o cargar un layout XML en una vista de Java.
import android.view.View; // La base de todos los componentes visuales.
import android.view.ViewGroup; // Representa un contenedor de vistas.
import android.widget.ImageView; // Específicamente, el componente para mostrar imágenes.
import android.widget.TextView; // Específicamente, el componente para mostrar texto.

import androidx.annotation.NonNull; // Anotación para indicar que un parámetro nunca puede ser nulo.
import androidx.recyclerview.widget.RecyclerView; // El componente para mostrar listas largas y eficientes.

import com.bumptech.glide.Glide; // La librería externa para descargar y mostrar imágenes de internet.
import com.example.themusicshorizons.R; // Conecta el código con los recursos.
import com.example.themusicshorizons.activities.SongDetailActivity; // La pantalla de detalles de la canción, para poder abrirla.
import com.example.themusicshorizons.database.MusicHorizonsContract; // Nuestro propio "contrato" que define los nombres de las tablas y columnas.

import org.json.JSONException; // Herramienta para manejar posibles errores al trabajar con texto en formato JSON.
import org.json.JSONObject; // Herramienta para crear y leer objetos JSON.

// Este adaptador es el "motor" que alimenta la lista de canciones guardadas.
// Recibe un Cursor (un puntero a los resultados de la base de datos) y se encarga de mostrar cada canción en el RecyclerView.
public class SongCursorAdapter extends RecyclerView.Adapter<SongCursorAdapter.SongViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public SongCursorAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    // Esta clase interna define los componentes de una fila individual de la lista.
    // Actúa como un "molde" para cada elemento de la lista.
    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView artistText;
        public ImageView artworkImage;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.songTitleItem);
            artistText = itemView.findViewById(R.id.songArtistItem);
            artworkImage = itemView.findViewById(R.id.songArtworkImageView);
        }
    }

    // Se ejecuta para crear cada "fila" o vista de la lista.
    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    // Este método se ejecuta para rellenar los datos de cada fila con la información de la canción correspondiente.
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        // Mueve el cursor a la posición correcta de la lista. Si no puede, detiene la operación para evitar un crash.
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        // Extrae los datos de la canción de las columnas del cursor usando los nombres definidos en el "contrato".
        long songId = mCursor.getLong(mCursor.getColumnIndexOrThrow(MusicHorizonsContract.DiscoveredSongEntry._ID));
        String songDataString = mCursor.getString(mCursor.getColumnIndexOrThrow(MusicHorizonsContract.DiscoveredSongEntry.COLUMN_SONG_DATA));
        String artworkUrl = mCursor.getString(mCursor.getColumnIndexOrThrow("artwork_url"));
        String spotifyUrl = mCursor.getString(mCursor.getColumnIndexOrThrow("spotify_url"));

        // Usa la librería Glide para descargar y mostrar la imagen de la carátula.
        if (artworkUrl != null && !artworkUrl.isEmpty()) {
            Glide.with(mContext)
                .load(artworkUrl)
                .placeholder(R.color.gris_oscuro) // Muestra un color gris mientras se descarga la imagen.
                .error(R.drawable.ic_person) // Muestra un icono de error si la descarga falla.
                .into(holder.artworkImage);
        } else {
            holder.artworkImage.setImageResource(R.drawable.ic_person); // Si no hay URL, muestra un icono por defecto.
        }

        try {
            // El resto de los datos de la canción se guardaron como un solo texto JSON.
            // Aquí se "desempaqueta" ese texto para obtener los datos individuales.
            JSONObject songData = new JSONObject(songDataString);
            String title = songData.optString("title", "Título no disponible");
            String artist = songData.optString("artist", "Artista no disponible");
            String album = songData.optString("album", "Álbum no disponible");

            // Rellena los campos de texto con el título y el artista.
            holder.titleText.setText(title);
            holder.artistText.setText(artist);

            // Define la acción que ocurre al pulsar en una fila: abrir la pantalla de detalles de la canción.
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, SongDetailActivity.class);
                // Se empaquetan todos los datos de la canción en el Intent para enviarlos a la siguiente pantalla.
                intent.putExtra("SONG_ID", songId);
                intent.putExtra("ARTIST", artist);
                intent.putExtra("TITLE", title);
                intent.putExtra("ALBUM", album);
                intent.putExtra("ARTWORK_URL", artworkUrl);
                intent.putExtra("SPOTIFY_URL", spotifyUrl);
                mContext.startActivity(intent);
            });

        } catch (JSONException e) {
            e.printStackTrace();
            holder.titleText.setText("Error al leer los datos");
            holder.artistText.setText("");
        }
    }

    // Devuelve el número total de canciones en la lista (el número de filas en el cursor).
    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    // Este método se usa para actualizar la lista con nuevos datos sin tener que crear un nuevo adaptador.
    // Es mucho más eficiente que crear un adaptador nuevo cada vez que los datos cambian.
    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close(); // Cierra el cursor antiguo para liberar recursos.
        }
        mCursor = newCursor;

        if (newCursor != null) {
            // Notifica al RecyclerView que los datos han cambiado para que se redibuje.
            notifyDataSetChanged(); 
        }
    }
}
