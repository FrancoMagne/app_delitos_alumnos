package com.example.app_delitos.database.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.app_delitos.database.models.Usuario;

public class UsuarioDAO {

    private mySQLiteOpenHelper objHelper;
    private Context context;
    private SQLiteDatabase database;

    public UsuarioDAO(Context c) {
        this.context = c;
    }

    public void open() throws SQLException {
        this.objHelper = new mySQLiteOpenHelper(context);
        this.database = objHelper.getWritableDatabase(); // permite escribir en la bd
    }

    /**
     * Obtiene todos los datos de un usuario: _id, email, password, nombre
     * @param email de tipo String
     * @return Una instancia de usuario de la clase Usuario o null en caso de no encontrar el usuario
     */
    public Usuario getByEmail(String email) {
        open();

        Usuario usuario = null;
        Cursor cursor = null;

        String query = String.format("SELECT * FROM %s WHERE email = '%s'", "usuarios", email);
        cursor = this.database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            usuario = new Usuario();

            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setEmail(cursor.getString(2));
            usuario.setPassword(cursor.getString(3));
            usuario.setFechaCreacion(cursor.getString(4));
        }

        cursor.close();

        return usuario;
    }
}
