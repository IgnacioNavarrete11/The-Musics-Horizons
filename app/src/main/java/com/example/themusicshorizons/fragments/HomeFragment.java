package com.example.themusicshorizons.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.themusicshorizons.R;
import com.example.themusicshorizons.activities.ExploreEventsActivity;
import com.example.themusicshorizons.activities.ScanAudioActivity;
import com.example.themusicshorizons.activities.ScanVisualActivity;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button scanAudioButton = view.findViewById(R.id.scanAudioButton);
        Button scanVisualButton = view.findViewById(R.id.scanVisualButton);
        Button exploreEventsButton = view.findViewById(R.id.exploreEventsButton);

        scanAudioButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), ScanAudioActivity.class)));
        scanVisualButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), ScanVisualActivity.class)));
        exploreEventsButton.setOnClickListener(v -> startActivity(new Intent(getActivity(), ExploreEventsActivity.class)));

        return view;
    }
}
