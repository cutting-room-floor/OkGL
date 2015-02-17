package com.mapbox.okgl;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FontFragment extends Fragment {

    private static final String TAG = "FontFragment";

    private TextView demoTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_font, container, false);
        demoTextView = (TextView)view.findViewById(R.id.textDemoTextView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        final Handler uiHandler = new Handler(Looper.getMainLooper());

        OkHttpClient client = new OkHttpClient();

        // Load The Image Asynchronously
        final Request request = new Request.Builder().url("https://github.com/todylu/monaco.ttf/raw/master/monaco.ttf").build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "FAILURE! " + e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Response was NOT Successful: " + response);
                    throw new IOException("Unexpected Code: " + response);
                }

                // Save to cache dir and unzip it
                InputStream inputStream = null;
                OutputStream outputStream = null;

                final ResponseBody body = response.body();

                String outPath = getActivity().getCacheDir() + "/tmp.raw";

                try {
                    inputStream = body.byteStream();
                    byte[] buffer = new byte[64];
                    outputStream = new BufferedOutputStream(new FileOutputStream(outPath));

                    int l = 0;
                    while ((l = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, l);
                    }

                    File font = new File(outPath);

                    final Typeface typeface = Typeface.createFromFile(outPath);

                    font.delete();

                    // Send Back To the UI Thread
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            demoTextView.setTypeface(typeface);
                        }
                    });

                } catch (IOException ex) {
                    Log.e(TAG, "Error reading typeface from resource.", ex);
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException ex) {
                        Log.e(TAG, "Error closing typeface streams.", ex);
                    }
                }
            }
        });
    }

}
