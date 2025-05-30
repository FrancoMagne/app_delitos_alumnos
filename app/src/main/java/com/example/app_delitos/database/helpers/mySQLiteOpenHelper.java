package com.example.app_delitos.database.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db_delitos.db";
    private static final int DATABASE_VERSION = 1;

    public mySQLiteOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tabla Usuarios
        db.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "email TEXT," +
                "password TEXT," +
                "fecha_creacion TEXT)");

        // Tabla Tipo de Delitos
        db.execSQL("CREATE TABLE tipo_delitos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "descripcion TEXT," +
                "fecha_creacion TEXT," +
                "fecha_modificacion TEXT," +
                "fecha_eliminacion TEXT)");

        // Tabla Delitos
        db.execSQL("CREATE TABLE delitos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER," +
                "id_tipo_delito INTEGER," +
                "descripcion TEXT," +
                "latitud TEXT," +
                "longitud TEXT," +
                "fecha_creacion TEXT," +
                "fecha_modificacion TEXT DEFAULT NULL," +
                "fecha_eliminacion TEXT DEFAULT NULL," +
                "FOREIGN KEY(id_usuario) REFERENCES usuarios(id)," +
                "FOREIGN KEY(id_tipo_delito) REFERENCES tipo_delitos(id))");

        insertarDatos(db);
    }

    private void insertarDatos(SQLiteDatabase db) {
        // Insertar usuarios por defecto
        db.execSQL("INSERT INTO usuarios (nombre, email, password, fecha_creacion) VALUES " +
                "('Admin', 'admin@example.com', '12345', '2025-05-30 01:10:00')");

        // Insertar tipos de delitos por defecto
        db.execSQL("INSERT INTO tipo_delitos (descripcion, fecha_creacion, fecha_modificacion, fecha_eliminacion) VALUES " +
                "('Robo', '2025-05-30 01:12:00', null, null)," +
                "('Asalto', '2025-05-30 01:14:00', null, null)," +
                "('Fraude', '2025-05-30 01:16:00', null, null)");

        // Insertar delitos por defecto
        db.execSQL("INSERT INTO delitos (id_usuario, descripcion, id_tipo_delito, latitud, longitud, fecha_creacion, fecha_modificacion, fecha_eliminacion) VALUES " +
                "(1, 'Robo en supermercado', 1, '-22.726274360099218', '-64.32563695806367', '2025-05-30 01:20:01', null, null)," +
                "(1, 'Asalto a mano armada', 2, '-22.727243257247956', '-64.32675075608424', '2025-05-30 01:22:00', null, null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS delitos");
        db.execSQL("DROP TABLE IF EXISTS tipo_delitos");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }
}
