package com.example.owner.queuing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

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
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        markerShowingInfoWindow = marker;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        // Getting view from the layout file info_window_layout
        View popUp = inflater.inflate(R.layout.popup_marker, null);

        TextView popUpTitle = (TextView) popUp.findViewById(R.id.marker_title);
        TextView popupContent = (TextView) popUp.findViewById(R.id.marker_res_num);

        popUpTitle.setText(marker.getTitle());
        popupContent.setText(marker.getSnippet());
        //popupCuisine.setText();
        // Load the image thumbnail
        //final String imagePath = markers.get(marker.getId());
        //ImageLoader imageLoader = ((AppConfig)mContext.getApplicationContext()).getImageLoader();
        //imageLoader.loadBitmap(imagePath, popUpImage, 0, 0, onImageLoaded);

        // Returning the view containing InfoWindow contents
        return popUp;
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

}
