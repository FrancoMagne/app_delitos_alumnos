package com.example.app_delitos.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Preferences {

    public interface Keys {
        String PREFERENCES = "my_preferences";

        // Login
        String L_USER_ID = "l_user_id";
        String L_USER_NAME = "l_user_name";
        String L_USER_EMAIL = "l_user_email";
        String L_USER_TOKEN = "l_user_token";
        String L_USER_TOKEN_TYPE = "l_user_token_type";
        String L_USER_TOKEN_EXPIRATION = "l_user_token_expiration";
    }

    private static SharedPreferences preferences;
    private static Preferences prefs;

    /**
     * Constructor for Singleton
     *
     * @param context a context for creation
     */
    public Preferences(@NotNull Context context) {
        preferences = context.getSharedPreferences(Keys.PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * Método para asegurar que pueda trabajar con multi-threading y hacer más seguro
     * singleton.
     *
     * @param context contexto para crear las preferencias.
     * @return una instancia de Preferences.
     */

    private static synchronized Preferences createPreferences(@NonNull Context context) {
        if (prefs == null) prefs = new Preferences(context);
        return prefs;
    }

    /**
     * Método que se llama para obtener las preferencias del usuario
     *
     * @param context un Context para poder crear las preferencias.
     * @return Una instancia de Preferences que es única (singleton).
     */

    public static Preferences getSharedPreferences(@NonNull Context context) {
        return createPreferences(context);
    }

    /* *******************************************************************************************
     *                                                  *
     *  Metodos para manejar la Activity del Login      *
     *                                                  *
     *********************************************************************************************/

    public void saveLUserId(String userId) {
        preferences.edit().putString(Keys.L_USER_ID, userId).apply();
    }

    public void saveLUserName(String userName) {
        preferences.edit().putString(Keys.L_USER_NAME, userName).apply();
    }

    public void saveLUserEmail(String userEmail) {
        preferences.edit().putString(Keys.L_USER_EMAIL, userEmail).apply();
    }

    public void saveLUserToken(String userToken) {
        preferences.edit().putString(Keys.L_USER_TOKEN, userToken).apply();
    }

    public void saveLUserTokenType(String userTokenType) {
        preferences.edit().putString(Keys.L_USER_TOKEN_TYPE, userTokenType).apply();
    }

    public void saveLUserTokenExpiration(String userTokenExpiration) {
        preferences.edit().putString(Keys.L_USER_TOKEN_EXPIRATION, userTokenExpiration).apply();
    }

    public String getLUserId() {
        return preferences.getString(Keys.L_USER_ID, "");
    }

    public String getLUserName() {
        return preferences.getString(Keys.L_USER_NAME, "");
    }

    public String getLUserEmail() {
        return preferences.getString(Keys.L_USER_EMAIL, "");
    }

    public String getLUserToken() { return preferences.getString(Keys.L_USER_TOKEN, ""); }

    public String getLUserTokenType() { return preferences.getString(Keys.L_USER_TOKEN_TYPE, ""); }

    public String getLUserTokenExpiration() { return preferences.getString(Keys.L_USER_TOKEN_EXPIRATION, ""); }

    // Elimina las preferencias del usuario logeado
    @SuppressLint("CommitPrefEdits")
    public void resetLoggedInUserData() {
        saveLUserId("");
        saveLUserName("");
        saveLUserEmail("");
        saveLUserToken("");
        saveLUserTokenType("");
        saveLUserTokenExpiration("");
    }
}
