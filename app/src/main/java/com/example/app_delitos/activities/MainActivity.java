package com.example.app_delitos.activities;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_delitos.adapters.ItemDelitoAdapter;
import com.example.app_delitos.database.helpers.DelitoDAO;
import com.example.app_delitos.database.models.Delito;
import com.example.app_delitos.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private ItemDelitoAdapter adapter;
    private DelitoDAO delitoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar
        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setTitle("Listado de Delitos");

        // Ajusta el tamaño de la Toolbar
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        delitoDAO = new DelitoDAO(getApplicationContext());

        configurar();
    }

    private void configurar() {

        binding.mapa.setOnClickListener(v -> {
            goActivity(MapActivity.class, false);
        });

        binding.nuevoDelito.setOnClickListener(v -> {
            goActivity(RecordActivity.class, false);
        });

        // Configuramos el listado
        ArrayList<Delito> delitos = delitoDAO.getAll();
        adapter = new ItemDelitoAdapter(delitos);
        binding.recyclerRequest.setAdapter(adapter);

        adapter.setOnClickListener(v -> {
            int position = binding.recyclerRequest.getChildAdapterPosition(v);

            Toast.makeText(getApplicationContext(), "Item " + position, Toast.LENGTH_SHORT).show();
        });

        // Solicitud de permisos de escritura en memoria
        if(! getWriteExternalStoragePermission()) {
            requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return;
        }

        if(! getLocationPermission()) {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
}