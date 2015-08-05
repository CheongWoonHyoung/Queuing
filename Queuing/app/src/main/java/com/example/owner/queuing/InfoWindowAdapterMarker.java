package com.example.owner.queuing;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by mark_mac on 2015. 7. 27..
 */
public class InfoWindowAdapterMarker implements GoogleMap.InfoWindowAdapter {

    private Marker markerShowingInfoWindow;
    private Context mContext;

    public InfoWindowAdapterMarker(Context context) {
        mContext = context;
    }

    public InfoWindowAdapterMarker() {

    }

    @Override
    public View getInfoWindow(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        // Getting view from the layout file info_window_layout
        View popUp = inflater.inflate(R.layout.activity_marker, null);

        TextView title = (TextView) popUp.findViewById(R.id.marker_title);
        TextView cuisine = (TextView) popUp.findViewById(R.id.cuisine_window);
        TextView line_num = (TextView) popUp.findViewById(R.id.number_window);
        title.setText(marker.getTitle());
        cuisine.setText(marker.getSnippet());
        try {
            String linenum = new get_linenum().execute(marker.getTitle()).get();
            line_num.setText(linenum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Returning the view containing InfoWindow contents
        return popUp;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;

    }

    /**
     * This method is called after the bitmap has been loaded. It checks if the currently displayed
     * info window is the same info window which has been saved. If it is, then refresh the window
     * to display the newly loaded image.
     */
  /* private Callback onImageLoaded = new Callback() {

        @Override
        public void execute(String result) {
            if (markerShowingInfoWindow != null && markerShowingInfoWindow.isInfoWindowShown()) {
                markerShowingInfoWindow.hideInfoWindow();
                markerShowingInfoWindow.showInfoWindow();
            }
        }
    };*/
    private class get_linenum extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... info) {
            String sResult = null;

            try {
                Log.d("INFO", "rest name is " + info[0]);
                URL url = new URL("http://52.69.163.43/get_linenum.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                String post_value = "title=" + info[0];

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(post_value);
                osw.flush();

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;

                while ((str = reader.readLine()) != null) {
                    builder.append(str);
                }
                sResult = builder.toString();

            } catch (Exception e) {
                Log.e("HTTPPOST","Error in Http POST REQUEST : " + e.toString());
            }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result){
            if(result.equals("CANNOT FIND RESTAURANT"))
                Log.d("SEARCH",result);
            else {
                Log.d("SEARCH",result);

                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }
}
