package com.example.app_delitos.general;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.app_delitos.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

public class Map {

    private final String TAG = "MAP::";
    public final static GeoPoint DEFAULT_LOCATION = new GeoPoint(-22.737207, -64.3337227);
    public static String DEFAULT_MESSAGE = "Estas aquí";
    private final MapView map;
    private GeoPoint myCurrentLocation;
    private LocationManager locationManager;
    private SearchLocationListener locationListener;
    private MapListener listener;

    public interface Values {
        double DEFAULT_ZOOM = 10;
        double MAX_ZOOM = 20;
        double MIN_ZOOM = 8;
    }

    private static void loadPreferences(Context context) {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public Map(Context context) {
        this(context, null);
    }

    public Map(@NonNull Context context, GeoPoint location) {

        loadPreferences(context);

        map = new MapView(context);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        map.setMultiTouchControls(true);
        map.setMaxZoomLevel(Values.MAX_ZOOM);
        map.setMinZoomLevel(Values.MIN_ZOOM);
        map.setTilesScaledToDpi(true);
        map.setTilesScaleFactor(0.9f);
        map.invalidate();

        if (location == null) location = DEFAULT_LOCATION;

        moveMapToPoint(location);

        map.getController().setZoom(Values.DEFAULT_ZOOM);
    }

    public MapView getMap() {
        return this.map;
    }

    public void clearMap() {
        // Cierra las burbujas de todos los marcadores del mapa
        for(int i = 0; i < map.getOverlays().size(); i++) {
            Overlay overlay = map.getOverlays().get(i);
            if(overlay instanceof Marker) {
                ((Marker)overlay).closeInfoWindow();
            }
        }

        map.getOverlays().clear();
        map.invalidate();
    }

    /**
     * Método para activar la brújula.
     *
     * @return un MapView para encadenar otros setters.
     */
    public Map enableCompass() {
        Context context = this.map.getContext();

        CompassOverlay compass = new CompassOverlay(context, this.map);
        boolean enable = compass.enableCompass();
        Log.d(TAG, "enableCompass: compass enable 1: " + enable);
        compass.setEnabled(true);
        enable = compass.enableCompass();
        Log.d(TAG, "enableCompass: compass enable 2: " + enable);
        //Revisar por qué no se agrega la brújula. Problema de creación aparentemente
        // https://stackoverflow.com/questions/51696123/compass-is-not-shown-on-map-view-of-osmdroid
        this.map.getOverlays().add(compass);
        this.map.invalidate();

        return this;
    }

    /**
     * Método que permite que el mapa tenga los gestos de rotación.
     *
     * @return el mismo MapView para encadenar otros setters.
     */
    public Map setRotationControls() {
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(map);
        mRotationGestureOverlay.setEnabled(true);
        this.map.setMultiTouchControls(true);
        this.map.getOverlays().add(mRotationGestureOverlay);

        return this;
    }

    /**
     * Sitúa el mapa en la posición dada (cambio brusco).
     *
     * @param location nueva ubicación
     * @return un MapView para encadenar llamadas.
     */
    public Map moveMapToPoint(GeoPoint location) {
        this.map.getController().setCenter(location);
        return this;
    }

    /**
     * Coloca una nueva ubicación y con un desplazamiento animado (y no brusca).
     *
     * @param location nueva ubicación
     * @return un MapView para encadenar llamadas.
     */
    public Map animateMapToPoint(GeoPoint location) {
        return animateMapToPoint(location, this.map.getZoomLevelDouble());
    }

    /**
     * Coloca una nueva ubicación y con un desplazamiento animado (y no brusca).
     *
     * @param location ubicación donde se moverá el mapa
     * @param zoom     el zoom que realizará en la location dada (por seguridad, estará
     *                 dentro del rango [12; 18]. Sino, el zoom original no se verá afectado.
     * @return una instancia de esta misma clase.
     */
    public Map animateMapToPoint(GeoPoint location, double zoom) {
        if (zoom >= 12 && zoom <= 18) this.map.getController().zoomTo(zoom);
        this.map.getController().animateTo(location);

        return this;
    }

    /**
     * Se realiza un determinado zoom en la vista
     *
     * @param zoom valor de zoom positivo
     * @return una instancia de esta misma clase.
     */
    public Map setZoom(double zoom) {
        if (zoom <= 0 || zoom > this.map.getMaxZoomLevel()) return this;

        this.map.getController().setZoom(zoom);
//        this.map.getController().animateTo(this.map.getMapCenter());

        return this;
    }

    public double getZoom() {
        return this.map.getZoomLevelDouble();
    }

    /**
     * Deprecated: en su lugar usar addMarker.
     * (Coloca un nuevo marcador pero borra todos los anteriores.)
     *
     * @param marker   el marcador
     * @param location la ubicación del marcador
     * @return un MapView para encadenar llamadas.
     */
    @Deprecated
    public Map setMarker(Marker marker, GeoPoint location) {

        return setMarker(marker, location, DEFAULT_MESSAGE);
    }

    /**
     * Deprecated: en su lugar usar addMarker.
     * (Coloca un nuevo marcador pero borra todos los anteriores.)
     *
     * @param marker   el marcador
     * @param location la ubicación del marcador
     * @param message  el mensaje que se mostrará cuando se toque el marcador
     * @return un MapView para encadenar llamadas.
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    @Deprecated
    public Map setMarker(@NonNull Marker marker, GeoPoint location, String message) {
        marker.setPosition(location);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(map.getContext().getResources().getDrawable(R.drawable.ic_place_24));
        marker.setTitle(message);

        map.getOverlays().add(marker);
        map.invalidate();

        return this;
    }

    /**
     * Retorna la última posición buscada.
     *
     * @return el GeoPoint con la ubicación, o NULL  en caso que no se haya realizado alguna búsqueda
     * o setteado una ubicación previamente.
     */
    public GeoPoint getCurrentLocation() {
        return this.myCurrentLocation;
    }

    public void setCurrentLocation(GeoPoint myCurrentLocation) {
        this.myCurrentLocation = myCurrentLocation;
    }

    /**
     * Método para detectar la ubicación actual del dispositivo.
     *
     * @return true si la solicitud pudo ser procesada correctamente o false por alguno de los siguientes
     * casos: 1. Dispositivo sin gps, 2. Dispositivo sin permisos para operar, 3. GPS deshabilitado,
     * 4. No se posee un contexto válido._
     */
    public boolean getMyCurrentLocation() {
        try {
            Context context = this.map.getContext().getApplicationContext();
            if(context == null) return false;

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager == null) {
                Log.d("UPDATE LOCATION:::", "Dispositivo sin gps");
                return false;
            }

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("UPDATE LOCATION:::", "UpdateLocation: Saliendo por no tener permisos");
                return false;
            }

            if (locationListener == null) locationListener = new SearchLocationListener();

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,
                    5, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    5, locationListener);

            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }

    }//End getCurrentLocation - fin del método de búsqueda de ubicación.

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS() {
        if(locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private class SearchLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            Log.d("LATITUDE::", "" + location.getLatitude());
            Log.d("LONGITUDE::", "" + location.getLongitude());
            stopUsingGPS();
            setCurrentLocation(new GeoPoint(location));
            onLocationManagerCallback(MapListener.ON_LOCATION_CHANGE);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //Toast.makeText(map.getContext(), "GPS DESACTIVADO", Toast.LENGTH_SHORT).show();
            onLocationManagerCallback(MapListener.ON_PROVIDER_DISABLED);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //Toast.makeText(map.getContext(), "GPS ACTIVADO", Toast.LENGTH_SHORT).show();
            onLocationManagerCallback(MapListener.ON_PROVIDER_ENABLED);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }//End Location Listener.

    /**
     * Método para agregar el listener que escuchará los eventos de búsqueda cuando se intente buscar la
     * ubicación actual del dispositivo.
     *
     * @param listener el oyente.
     * @return la instancia de esta clase.
     */
    public Map setMapListener(MapListener listener) {
        this.listener = listener;
        return this;
    }

    private void onLocationManagerCallback(int status) {
        if (this.listener != null)
            this.listener.onUpdateLocationListener(status);
    }


    /**
     * Interface creada para manejar los eventos de búsqueda de ubicación por fuera de la clase.
     */
    public interface MapListener {

        int ON_LOCATION_CHANGE = 0;
        int ON_PROVIDER_DISABLED = 1;
        int ON_PROVIDER_ENABLED = 2;
        int ON_STATUS_CHANGE = 3;

        /**
         * Método que se ejecuta cuando se llaman a los eventos de búsqueda del mapa. Dependiendo de
         * lo que ocurra, la constante status tendrá información acerca de qué fue lo que llamó este método.
         * Por lo tanto, es posible una comparación del tipo:
         * if(status == ON_LOCATION_CHANGE) {...}
         * y manejar las acciones del evento respectivo.
         *
         * @param status parámetro con el evento que llamó al método.
         */
        void onUpdateLocationListener(int status);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void touchMode(boolean touch) {
        this.map.setOnTouchListener((View v, MotionEvent event)-> !touch);
    }
}
