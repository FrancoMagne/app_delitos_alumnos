package com.example.app_delitos.activities;

import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_delitos.database.helpers.UsuarioDAO;
import com.example.app_delitos.database.models.Usuario;
import com.example.app_delitos.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar
        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setTitle("");

        // Ajusta el tamaño de la Toolbar
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Usuario que provienen del CSV
        usuarioDAO = new UsuarioDAO(getApplicationContext());

        configurar();
    }

    private void configurar() {

        binding.loginButton.setOnClickListener(v -> {

            // Campos ingresados.
            Usuario user = usuarioDAO.getByEmail(binding.emailUser.getText().toString().trim());

            if (user != null) {
                // Comprobar que los datos dni y contraseña sean correctos.
                if (user.getPassword().equals(binding.passwordUser.getText().toString().trim())) {
                    showProgressDialog("Ingresando", "Espere por favor...");

                    // Datos correctos. Guardamos los datos en SharedPreferences.
                    getPrefs().saveLUserId(String.valueOf(user.getId()));
                    getPrefs().saveLUserName(user.getNombre());
                    getPrefs().saveLUserEmail(user.getEmail());

                    // Redirigir al Home
                    goActivity(MainActivity.class, true);
                } else {
                    // La contraseña es incorrecta.
                    showSnackBarDefault("La contraseña ingresada es incorrecta.");
                }
            } else {
                showSnackBarDefault("Datos incorrectos");
            }
        });
    }
}