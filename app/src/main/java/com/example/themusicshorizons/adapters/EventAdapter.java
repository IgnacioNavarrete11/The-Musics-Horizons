package com.example.themusicshorizons.adapters;

// --- IMPORTACIONES DE HERRAMIENTAS ---

import android.content.Context; // Proporciona acceso a recursos y servicios del sistema Android.
import android.content.Intent; // Herramienta para abrir otras pantallas o aplicaciones.
import android.net.Uri; // Herramienta para manejar direcciones web (URLs).
import android.view.LayoutInflater; // Herramienta para "inflar" o cargar un layout XML en una vista de Java.
import android.view.View; // La base de todos los componentes visuales.
import android.view.ViewGroup; // Representa un contenedor de vistas.
import android.widget.ImageButton; // Específicamente, el componente de botón con imagen.
import android.widget.ImageView; // Específicamente, el componente para mostrar imágenes.
import android.widget.TextView; // Específicamente, el componente para mostrar texto.
import android.widget.Toast; // Herramienta para mostrar mensajes flotantes.

import androidx.annotation.NonNull; // Anotación para indicar que un parámetro nunca puede ser nulo.
import androidx.recyclerview.widget.RecyclerView; // El componente para mostrar listas largas y eficientes.

import com.bumptech.glide.Glide; // La librería externa para descargar y mostrar imágenes de internet.
import com.example.themusicshorizons.R; // Conecta el código con los recursos.
import com.example.themusicshorizons.database.DatabaseHelper; // Nuestro propio "fontanero" de la base de datos.
import com.example.themusicshorizons.models.ticketmaster.Event; // Nuestro propio modelo que representa un evento.
import com.example.themusicshorizons.utils.SessionManager; // Nuestra propia "memoria" del usuario.
import com.google.gson.Gson; // La librería externa para convertir texto JSON a objetos Java y viceversa.

import java.util.List; // Representa una colección ordenada de objetos.

// Este adaptador es el "motor" que alimenta la lista de eventos.
// Recibe una lista de objetos Event y se encarga de mostrarlos en el RecyclerView.
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private long userId;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
        this.dbHelper = new DatabaseHelper(context);
        this.sessionManager = new SessionManager(context);
        this.userId = sessionManager.getUserId();
    }

    // Se ejecuta para crear cada "fila" o vista de la lista.
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Carga el diseño XML de una fila individual (item_event.xml).
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    // Este método se ejecuta para rellenar los datos de cada fila con la información del evento correspondiente.
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        // Rellena los campos de texto con el nombre, la fecha y el lugar del evento.
        holder.eventName.setText(event.name);
        holder.eventDate.setText(event.dates.start.localDate);
        
        if (event._embedded != null && event._embedded.venues != null && !event._embedded.venues.isEmpty()) {
            holder.eventVenue.setText(event._embedded.venues.get(0).name);
        }

        // Usa la librería Glide para descargar y mostrar la imagen del evento.
        if (event.images != null && !event.images.isEmpty()) {
            Glide.with(context).load(event.images.get(0).url).into(holder.eventImage);
        }

        // Comprueba si el evento ya está guardado y establece el icono de la estrella (llena o vacía) en consecuencia.
        updateSaveButtonState(holder.saveEventButton, event.id);

        // Define la acción que ocurre al pulsar el botón de guardar (la estrella).
        holder.saveEventButton.setOnClickListener(v -> {
            if (dbHelper.isEventSaved(userId, event.id)) {
                // Si el evento ya estaba guardado, lo elimina de la base de datos.
                dbHelper.deleteSavedEvent(userId, event.id);
                Toast.makeText(context, "Evento eliminado de tu horizonte", Toast.LENGTH_SHORT).show();
            } else {
                // Si no estaba guardado, lo convierte a JSON y lo guarda en la base de datos.
                Gson gson = new Gson();
                String eventData = gson.toJson(event);
                dbHelper.addSavedEvent(userId, event.id, eventData);
                Toast.makeText(context, "Evento guardado en tu horizonte", Toast.LENGTH_SHORT).show();
            }
            // Actualiza visualmente el icono de la estrella para que refleje el nuevo estado.
            updateSaveButtonState(holder.saveEventButton, event.id);
        });

        // Define la acción que ocurre al pulsar en cualquier otra parte de la fila: abrir la página del evento en el navegador.
        holder.itemView.findViewById(R.id.eventClickableLayout).setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.url));
            context.startActivity(browserIntent);
        });
    }

    // Este método auxiliar cambia el icono y el color de la estrella según si el evento está guardado o no.
    private void updateSaveButtonState(ImageButton button, String eventId) {
        if (dbHelper.isEventSaved(userId, eventId)) {
            button.setImageResource(R.drawable.ic_star_filled);
            button.setColorFilter(context.getResources().getColor(R.color.celeste));
        } else {
            button.setImageResource(R.drawable.ic_star_border);
            button.setColorFilter(context.getResources().getColor(R.color.white));
        }
    }

    // Devuelve el número total de eventos en la lista.
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // Esta clase interna define los componentes de una fila individual de la lista.
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName, eventVenue, eventDate;
        ImageButton saveEventButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.eventImageView);
            eventName = itemView.findViewById(R.id.eventNameTextView);
            eventVenue = itemView.findViewById(R.id.eventVenueTextView);
            eventDate = itemView.findViewById(R.id.eventDateTextView);
            saveEventButton = itemView.findViewById(R.id.saveEventButton);
        }
    }
}
