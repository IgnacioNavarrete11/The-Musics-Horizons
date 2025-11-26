package com.example.themusicshorizons.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.database.MusicHorizonsContract;

import org.json.JSONException;
import org.json.JSONObject;

public class SongCursorAdapter extends RecyclerView.Adapter<SongCursorAdapter.SongViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public SongCursorAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView artistText;

        public SongViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.songTitleItem);
            artistText = itemView.findViewById(R.id.songArtistItem);
        }
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String songDataString = mCursor.getString(mCursor.getColumnIndexOrThrow(MusicHorizonsContract.DiscoveredSongEntry.COLUMN_SONG_DATA));
        try {
            JSONObject songData = new JSONObject(songDataString);
            String title = songData.optString("title", "Título no disponible");
            String artist = songData.optString("artist", "Artista no disponible");

            holder.titleText.setText(title);
            holder.artistText.setText(artist);

        } catch (JSONException e) {
            e.printStackTrace();
            holder.titleText.setText("Error al leer los datos");
            holder.artistText.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
