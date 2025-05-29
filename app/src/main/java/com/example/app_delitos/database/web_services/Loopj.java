package com.example.app_delitos.database.web_services;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Loopj {

    public static AsyncHttpClient client;

    static {
        // fixNoHttpResponseException: ya sea para solucionar el problema o no, al omitir la verificación SSL
        // httpPort: el puerto HTTP que se utilizará, debe ser mayor que 0
        // httpsPort: el puerto HTTPS que se utilizará, debe ser mayor que 0
        client = new AsyncHttpClient(true,80,443);
        client.setResponseTimeout(40000);
    }

    /**
     * Establece la conexión con la base de datos por el método GET.
     * @param context         Contexto desde el cual se llama a la funcion.
     * @param api             La dirección donde se encuentra el archivo php para ejecutar la consulta.
     *                        (por defecto se encuentra en database.web_service.general.Api)
     * @param params          Los parámetros para la consulta (get).
     * @param responseHandler El manejador de la respuesta a la consulta.
     */

    public static void get(Context context, String api, RequestParams params, ResponseHandlerInterface responseHandler) {
        client.get(context, api, params, responseHandler);
    }

    public static void get(Context context, String url, Header[] headers, RequestParams params, ResponseHandlerInterface responseHandlerInterface) {
        client.get(context, url, headers, params, responseHandlerInterface);
    }

    public static void get(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandlerInterface) {
        client.get(context, url, entity, contentType, responseHandlerInterface);
    }

    /**
     * Establece la conexión con la base de datos por el método POST.
     * @param context         Contexto desde el cual se llama a la funcion.
     * @param api             La dirección donde se encuentra el archivo php para ejecutar la consulta.
     *                        (por defecto se encuentra en database.web_service.general.Api)
     * @param params          Los parámetros para la consulta (post).
     * @param responseHandler El manejador de la respuesta a la consulta.
     */
    public static void post(Context context, String api, RequestParams params, ResponseHandlerInterface responseHandler) {
        client.post(context, api, params, responseHandler);
    }
    /**
     * Establece la conexión con la base de datos por el método POST.
     * @param context           Contexto desde el cual se llama a la funcion.
     * @param url    La dirección donde se encuentra el archivo php para ejecutar la consulta.
     *                          (por defecto se encuentra en database.web_service.general.login.Api)
     * @param entity            Parametros que se envia al endpoint en el RAW de POSTMAN
     * @param contentType       El tipo de content type de la consulta
     * @param responseHandlerInterface   El manejador de la respuesta a la consulta.
     */

    public static void post(Context context, String url, StringEntity entity, String contentType, ResponseHandlerInterface responseHandlerInterface) {
        client.post(context, url, entity, contentType, responseHandlerInterface);
    }

    public static void post(Context context, String url, Header[] headers, HttpEntity httpEntity, String contentType, ResponseHandlerInterface responseHandlerInterface) {
        client.post(context, url, headers, httpEntity, contentType, responseHandlerInterface);
    }

    public static void post(Context context, String url, Header[] headers, RequestParams params, String contentType, ResponseHandlerInterface responseHandlerInterface) {
        client.post(context, url, headers, params, contentType, responseHandlerInterface);
    }

    public static void patch(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandlerInterface) {
        client.patch(context, url, params, responseHandlerInterface);
    }

    public static void patch(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandlerInterface) {
        client.patch(context, url, entity, contentType, responseHandlerInterface);
    }

    public static void patch(Context context, String url, Header[] headers, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandlerInterface) {
        client.patch(context, url, headers, entity, contentType, responseHandlerInterface);
    }
}
