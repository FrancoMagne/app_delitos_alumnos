package com.example.app_delitos.database.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.util.Log;

import com.example.app_delitos.database.models.Delito;

import java.util.ArrayList;

public class DelitoDAO {

    private mySQLiteOpenHelper objHelper;
    private Context context;
    private SQLiteDatabase database;

    public DelitoDAO(Context c) {
        this.context = c;
    }

    public void open() throws SQLException {
        this.objHelper = new mySQLiteOpenHelper(context);
        this.database = objHelper.getWritableDatabase(); // permite escribir en la bd
    }

    public void close() {
        this.objHelper.close();
    }

    public long insert(Delito delito) {
        long id = 0;

        try {
            // Abrimos la base de datos
            open();

            // Contenedor de variables
            ContentValues values = new ContentValues();

            values.put("id_usuario", delito.getIdUsuario());
            values.put("descripcion", delito.getDescripcion());
            values.put("id_tipo_delito", delito.getIdTipoDelito());
            values.put("latitud", delito.getLatitud());
            values.put("longitud", delito.getLongitud());
            values.put("fecha_creacion", delito.getFechaCreacion());

            id = this.database.insert("delitos", null, values);
            Log.i("Delito insert", "Delito registrado exitosamente: " + id);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            this.database.close();
        }

        return id;
    }

    public ArrayList<Delito> getAll() {
        open();

        ArrayList<Delito> delitos = new ArrayList<>();

        String query = String.format("SELECT * FROM %s WHERE fecha_eliminacion IS NULL", "delitos");
        Cursor cursor = this.database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Delito tmp = new Delito();

                tmp.setId(cursor.getInt(0));
                tmp.setIdUsuario(cursor.getInt(1));
                tmp.setIdTipoDelito(cursor.getInt(2));
                tmp.setDescripcion(cursor.getString(3));
                tmp.setLatitud(cursor.getString(4));
                tmp.setLongitud(cursor.getString(5));
                tmp.setFechaCreacion(cursor.getString(6));
                tmp.setFechaModificacion(cursor.getString(7));
                tmp.setFechaEliminacion(cursor.getString(8));

                delitos.add(tmp);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return delitos;
    }

    public boolean update(Delito delito) {
        boolean isCorrect = false;

        open();

        try {
            String query = String.format("UPDATE %s " +
                            "SET descripcion = '%s', " +
                            "id_tipo_delito = '%s', " +
                            "latitud = '%s', " +
                            "longitud = '%s', " +
                            "fecha_modificacion = '%s', " +
                            "fecha_eliminacion = '%s', " +
                            "WHERE id = '%s'",
                    "delitos",
                    delito.getDescripcion(), delito.getIdTipoDelito(), delito.getLatitud(),
                    delito.getLongitud(), delito.getFechaModificacion(), delito.getFechaEliminacion(), delito.getId());

            Log.d("Delito update", query);

            this.database.execSQL(query);
            isCorrect = true;
            Log.i("Delito update","Delito actualizado exitosamente: " + delito.getId());
        } catch (Exception ex) {
            ex.toString();
            Log.i("Delito catch","Error al actualizar el delito: " + delito.getId());
        } finally {
            this.database.close();
        }

        return isCorrect;
    }

    public boolean delete(int id) {
        // Completar
        return false;
    }

}
