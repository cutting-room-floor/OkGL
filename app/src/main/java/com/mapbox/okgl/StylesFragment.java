package com.mapbox.okgl;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class StylesFragment extends Fragment {

    private static final String TAG = "StylesFragment";

    private ListView listView;
    private JSONArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_styles, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter = new JSONArrayAdapter(getActivity(), R.layout.list_jsonarray));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Handler uiHandler = new Handler(Looper.getMainLooper());

        OkHttpClient client = new OkHttpClient();

        // Load The Image Asynchronously
        Request request = new Request.Builder().url("https://api.tiles.mapbox.com/v4/mapbox.streets.json?secure&access_token=pk.eyJ1IjoiYmxlZWdlIiwiYSI6InRIWGRhQTgifQ.aqpWzaYuYupQd78KaSK_SA").build();

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

                // Deserialize JSON
                try {
                    final JSONObject json = new JSONObject(response.body().string());

                    // Send Back To the UI Thread
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Iterator i = json.keys();
                            while (i.hasNext()) {
                                arrayAdapter.add(i.next().toString());
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class JSONArrayAdapter extends ArrayAdapter<String> {
        private int resourceId;

        public JSONArrayAdapter(Context context, int resource) {
            super(context, resource);
            this.resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;
            if (convertView != null) {
                rowView = convertView;
            } else {
                rowView = inflater.inflate(resourceId, parent, false);
            }

            TextView value = (TextView)rowView.findViewById(R.id.valueView);
            value.setText(getItem(position));

            return rowView;
        }
    }
}
