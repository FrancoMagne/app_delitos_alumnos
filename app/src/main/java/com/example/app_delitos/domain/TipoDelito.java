package com.example.app_delitos.domain;

public class TipoDelito {

    private String id;
    private String descripcion;

    public TipoDelito() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
