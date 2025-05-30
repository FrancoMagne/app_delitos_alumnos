package com.example.app_delitos.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_delitos.R;
import com.example.app_delitos.database.helpers.DelitoDAO;
import com.example.app_delitos.database.helpers.TipoDelitoDAO;
import com.example.app_delitos.database.models.Delito;
import com.example.app_delitos.database.models.TipoDelito;
import com.example.app_delitos.databinding.ActivityMapBinding;
import com.example.app_delitos.general.Map;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class MapActivity extends BaseActivity {

    private ActivityMapBinding binding;
    private Map map;
    private DelitoDAO delitoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar
        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setTitle("Mapa de Delitos");

        // Ajusta el tamaño de la Toolbar
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        delitoDAO = new DelitoDAO(getApplicationContext());

        // Inicializamos el Mapa
        initMap();
    }

    private void initMap() {
        try {
            if(map == null) {
                map = new Map(getApplicationContext());
                binding.mapContainerStore.addView(map.getMap());
            }

            map.clearMap(); // Limpia todos los puntos del mapa

            drawPoints();
        } catch (IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void drawPoints() {
        ArrayList<Delito> data = delitoDAO.getAll();
        if(data != null && ! data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                Delito delito = data.get(i); // Delito
                Marker marker = new Marker(map.getMap()); // Marcador que va al mapa
                marker.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_place_24));
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                try {
                    double latitude = Double.parseDouble(delito.getLatitud()); // Latitud
                    double longitude = Double.parseDouble(delito.getLongitud()); // Longitud

                    marker.setTitle(delito.getDescripcion());
                    marker.setPosition(new GeoPoint(latitude, longitude));
                    marker.setOnMarkerClickListener((markerView, mapView) -> {
                        if(markerView.isInfoWindowOpen()) {
                            markerView.closeInfoWindow();
                        } else {
                            //mapView.getController().zoomTo(Map.Values.ZOOM_CURRENT_LOCATION);
                            mapView.getController().animateTo(markerView.getPosition());
                            markerView.showInfoWindow();
                        }
                        return true;
                    });
                    map.getMap().getOverlays().add(marker);
                } catch (NullPointerException | NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            map.getMap().invalidate();
        }
    }
}