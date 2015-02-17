package com.mapbox.okgl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

public class ImageFragment extends Fragment {

    private static final String TAG = "ImageFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onResume() {
        // ImageView
        ImageView iv = (ImageView)getView().findViewById(R.id.demoImageView);

        OkHttpClient client = new OkHttpClient();

        // Load The Image Asynchronously
        Request request = new Request.Builder().url("http://api.tiles.mapbox.com/v4/marker/pin-l-rocket+3887be.png?access_token=pk.eyJ1IjoiYmxlZWdlIiwiYSI6InRIWGRhQTgifQ.aqpWzaYuYupQd78KaSK_SA").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "FAILURE! " + e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }
}
