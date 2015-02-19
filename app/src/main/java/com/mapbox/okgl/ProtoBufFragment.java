package com.mapbox.okgl;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

public class ProtoBufFragment extends Fragment {

    private static final String TAG = "ProtoBufFragment";

    private ProgressBar progressBar;
    private Button controlButton;

    private OkHttpClient client;
    private Call call;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_protobuf, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        controlButton = (Button) view.findViewById(R.id.controlButton);
        controlButton.setOnClickListener(new ControlButtonClickListener());

        progressBar.setVisibility(View.INVISIBLE);

        client = new OkHttpClient();

        return view;
    }

    private void downloadProtoBufFile() {

        final Handler uiHandler = new Handler(Looper.getMainLooper());

        try {
            if (client.getCache() != null) {
                client.getCache().evictAll();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error clearing cache: " + e.toString());
        }

        Request request = new Request.Builder().url("https://s3.amazonaws.com/metro-extracts.mapzen.com/madison_wisconsin.osm.pbf").tag("DL").build();

        call = client.newCall(request);

        final Activity activity = getActivity();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "FAILURE! " + e.toString());
                progressBar.setVisibility(View.INVISIBLE);
                controlButton.setText(R.string.startDownload);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Response was NOT Successful: " + response);
                    throw new IOException("Unexpected Code: " + response);
                }

                // Send Back To the UI Thread
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getApplicationContext(), "Finished Downloading ProtoBuf", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        controlButton.setText(R.string.startDownload);
                    }
                });
            }
        });

    }

    private void cancelDownload() {
        if (call != null) {
            client.cancel("DL");
            Log.i(TAG, "cancelDownload() called.  status = '" + call.isCanceled() + "'");
            Toast.makeText(getActivity(), "cancelDownload() called.  status = '" + call.isCanceled() + "'", Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "call is null, so can't cancel.");
        }
    }

    private class ControlButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (controlButton.getText().equals(getString(R.string.startDownload))) {
                progressBar.setVisibility(View.VISIBLE);
                controlButton.setText(R.string.cancelDownload);
                downloadProtoBufFile();
            } else if (controlButton.getText().equals(getString(R.string.cancelDownload))) {
                progressBar.setVisibility(View.INVISIBLE);
                controlButton.setText(R.string.startDownload);
                cancelDownload();
            }
        }
    }
}
