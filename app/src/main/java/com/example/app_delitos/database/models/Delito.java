package com.example.app_delitos.database.models;

public class Delito {

    private int id;
    private int idUsuario;
    private String descripcion;
    private int idTipoDelito;
    private String latitud;
    private String longitud;
    private String fechaCreacion;
    private String fechaModificacion;
    private String fechaEliminacion;

    public Delito() {

    }

    public Delito(int id, int idUsuario, String descripcion, int idTipoDelito, String latitud, String longitud,
                  String fechaCreacion, String fechaModificacion, String fechaEliminacion) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
        this.idTipoDelito = idTipoDelito;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.fechaEliminacion = fechaEliminacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdTipoDelito() {
        return idTipoDelito;
    }

    public void setIdTipoDelito(int idTipoDelito) {
        this.idTipoDelito = idTipoDelito;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(String fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }
}
