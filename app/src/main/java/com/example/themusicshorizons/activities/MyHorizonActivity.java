package com.example.themusicshorizons.activities;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.adapters.SongCursorAdapter;
import com.example.themusicshorizons.database.DatabaseHelper;
import com.example.themusicshorizons.utils.SessionManager;

public class MyHorizonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SongCursorAdapter adapter;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_horizon);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        recyclerView = findViewById(R.id.songsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        long currentUserId = sessionManager.getUserId();
        Cursor cursor = dbHelper.getSongsForUser(currentUserId);

        adapter = new SongCursorAdapter(this, cursor);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        long currentUserId = sessionManager.getUserId();
        if (adapter != null) {
            Cursor newCursor = dbHelper.getSongsForUser(currentUserId);
            adapter.swapCursor(newCursor);
        }
    }
}
