package com.example.themusicshorizons.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.adapters.SongCursorAdapter;
import com.example.themusicshorizons.database.DatabaseHelper;
import com.example.themusicshorizons.utils.SessionManager;

public class MyHorizonFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongCursorAdapter adapter;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_horizon, container, false);

        dbHelper = new DatabaseHelper(getContext());
        sessionManager = new SessionManager(getContext());

        recyclerView = view.findViewById(R.id.songsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        long currentUserId = sessionManager.getUserId();
        Cursor cursor = dbHelper.getSongsForUser(currentUserId);

        adapter = new SongCursorAdapter(getContext(), cursor);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        long currentUserId = sessionManager.getUserId();
        if (adapter != null) {
            Cursor newCursor = dbHelper.getSongsForUser(currentUserId);
            adapter.swapCursor(newCursor);
        }
    }
}
