package com.example.app_delitos.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.app_delitos.R;
import com.example.app_delitos.general.Preferences;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    private Preferences prefs;
    private Snackbar snack;
    private ProgressDialog progressDialog;
    private String permission;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Iniciamos el preferences
        prefs = Preferences.getSharedPreferences(this);

        // Iniciamos el snackbar
        snack = Snackbar
                .make(findViewById(android.R.id.content), "", Snackbar.LENGTH_SHORT)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
    }

    public void requestPermission(String permission) {

        this.permission = permission;

        if(checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            return; // true
        }

        if (shouldShowRequestPermissionRationale(permission)) {
            loadPermissionDialog();
        } else {
            if(permission.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Permiso para leer Imagenes en Versiones de Android 13 y superiores
                    requestPermissionWriteExternalStorageLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                } else {
                    requestPermissionWriteExternalStorageLauncher.launch(permission);
                }
            } else {
                requestPermissionAccessLocationLauncher.launch(new String[] {
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                });
            }
        }
    }

    private final ActivityResultLauncher<Intent> requestPermissionManualLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) loadPermissionDialog(this.intent);
                else showSnackBarDefault("Error con los permisos");
            }
    );

    private final ActivityResultLauncher<String[]> requestPermissionAccessLocationLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {

                // Obtenemos el permiso de Ubicacion Precisa
                Boolean fineLocationGranted = null;
                fineLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false);

                // Obtenemos el permiso de Ubicacion Aproximada
                Boolean coarseLocationGranted = null;
                coarseLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false);

                if ((fineLocationGranted != null && fineLocationGranted) || (coarseLocationGranted != null && coarseLocationGranted)) {
                    showSnackBarDefault("Permisos aceptados con exito");
                } else {
                    // No location access granted.
                    loadPermissionDialog();
                }
            }
    );

    private final ActivityResultLauncher<String> requestPermissionWriteExternalStorageLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if(! result) loadPermissionDialog();
                else showSnackBarDefault("Permisos aceptados con exito");

            }
    );

    public void loadPermissionDialog(Intent intent) {
        /** TODO: se deberia agregar un dialogo para que pregunte antes de redirigir */
        // requestPermissionManualLauncher.launch(intent);
    }

    public void loadPermissionDialog() {
        /** TODO: se deberia agregar un dialogo para que pregunte antes de redirigir */
        /*Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);*/
    }

    public void dialogGPSDisabled() {
        /** TODO: se deberia agregar un dialogo para que pregunte antes de redirigir */
        /*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent); */
    }

    public boolean getLocationPermission() {
        return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean getWriteExternalStoragePermission() {
        // [infinity; Android 13)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else { // [Android 13; infinity)
            return checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        }
    }

    public Preferences getPrefs() {
        return prefs;
    }

    protected String getToken() {
        try {
            return prefs.getLUserTokenType() + " " + prefs.getLUserToken();
        } catch (IllegalStateException | NullPointerException e) {
            return null;
        }
    }

    private void restoreSnackBase() {
        snack.setDuration(Snackbar.LENGTH_SHORT);
        snack.setAction(null, null);
    }

    public void showSnackBarDefault(String message) {
        View view = snack.getView();
        restoreSnackBase();
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        snack.setText(message);
        snack.show();
    }

    public void showSnackBarError(String message) {
        View view = snack.getView();
        restoreSnackBase();
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        snack.setText(message);
        snack.show();
    }

    public boolean verifyConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void goActivity(Class<?> classActivity, boolean destroyActivity) {
        Intent i = new Intent(this, classActivity);
        startActivity(i);
        // overridePendingTransition(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_exit_anim);
        if(destroyActivity) finish();
    }

    public void goActivity(Class<?> classActivity, Map<String, String> params, boolean destroyActivity) {
        Intent i = new Intent(this, classActivity);

        // Add params for new Activity
        for (Map.Entry<String, String> param : params.entrySet()) {
            Log.i("BaseActivity","clave=" + param.getKey() + ", valor=" + param.getValue());
            i.putExtra(param.getKey(), param.getValue());
        }

        startActivity(i);
        // overridePendingTransition(androidx.navigation.ui.R.anim.nav_default_enter_anim, androidx.navigation.ui.R.anim.nav_default_exit_anim);
        if(destroyActivity) finish();
    }

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Espere un momento");
        progressDialog.setMessage("Cargando");
    }

    public void showProgressDialog() {
        showProgressDialog("Espere un momento", "Cargando");
    }

    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) createProgressDialog();

        if(!title.isEmpty()) progressDialog.setTitle(title);
        if(!message.isEmpty()) progressDialog.setMessage(message);

        progressDialog.show();
    }

    public void changeMessageProgressDialog(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.setMessage(msg);
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


}