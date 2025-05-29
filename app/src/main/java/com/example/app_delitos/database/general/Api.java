package com.example.app_delitos.database.general;

public class Api {

    private final static String ENDPOINT = ""; // completar con URL de ngrok

    private final static String BASE_URL = ENDPOINT + "/api";

    public final static String login = BASE_URL + "/auth/login";
    public final static String delitos = BASE_URL + "/delitos";
    public final static String delito = BASE_URL + "/delito";
}
