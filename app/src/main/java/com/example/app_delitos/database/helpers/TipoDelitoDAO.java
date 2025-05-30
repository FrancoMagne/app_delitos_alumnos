package com.example.app_delitos.database.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.app_delitos.database.models.TipoDelito;

import java.util.ArrayList;

public class TipoDelitoDAO {

    private mySQLiteOpenHelper objHelper;
    private Context context;
    private SQLiteDatabase database;

    public TipoDelitoDAO(Context c) {
        this.context = c;
    }

    public void open() throws SQLException {
        this.objHelper = new mySQLiteOpenHelper(context);
        this.database = objHelper.getWritableDatabase(); // permite escribir en la bd
    }

    public ArrayList<TipoDelito> getAll() {
        open();

        ArrayList<TipoDelito> tipoDelitos = new ArrayList<>();

        String query = String.format("SELECT * FROM %s WHERE fecha_eliminacion IS NULL", "delitos");
        Cursor cursor = this.database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                TipoDelito tmp = new TipoDelito();

                tmp.setId(cursor.getInt(0));
                tmp.setDescripcion(cursor.getString(1));

                tipoDelitos.add(tmp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return tipoDelitos;
    }

    public TipoDelito getById(int id) {
        open();

        TipoDelito tipoDelito = null;
        Cursor cursor = null;

        String query = String.format("SELECT * FROM %s WHERE id = '%s'", "tipo_delitos", id);
        cursor = this.database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            tipoDelito = new TipoDelito();

            tipoDelito.setId(cursor.getInt(0));
            tipoDelito.setDescripcion(cursor.getString(1));
        }

        cursor.close();

        return tipoDelito;
    }
}
