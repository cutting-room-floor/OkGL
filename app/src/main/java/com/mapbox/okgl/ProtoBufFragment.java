package com.mapbox.okgl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

public class ProtoBufFragment extends Fragment {

    private static final String TAG = "ProtoBufFragment";

    private ProgressBar progressBar;
    private Button controlButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_protobuf, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        controlButton = (Button) view.findViewById(R.id.controlButton);

        return view;
    }

}
