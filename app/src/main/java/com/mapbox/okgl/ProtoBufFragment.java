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
        controlButton.setOnClickListener(new ControlButtonClickListener());

        progressBar.setVisibility(View.INVISIBLE);

        return view;
    }


    private class ControlButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (controlButton.getText().equals(getString(R.string.startDownload))) {
                progressBar.setVisibility(View.VISIBLE);
                controlButton.setText(R.string.cancelDownload);

            } else if (controlButton.getText().equals(getString(R.string.cancelDownload))) {
                progressBar.setVisibility(View.INVISIBLE);
                controlButton.setText(R.string.startDownload);
            }
        }
    }
}
