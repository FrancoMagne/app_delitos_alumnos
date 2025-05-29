package com.example.app_delitos.database.web_services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.app_delitos.database.general.Key;
import com.example.app_delitos.database.general.Response;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class WebRequest {

    private final String TAG = "WebRequest::";
    private final Context context;
    private final ResponseListener listener;
    private final WebResponse response;
    private JSONObject json = null;

    public WebRequest(@NonNull Context context, @NonNull ResponseListener listener) {
        this.context = context;
        this.listener = listener;
        this.response = new WebResponse();
    }

    public void connect(String url, String method, RequestParams params) {
        if (url != null && !url.isEmpty()) {
            switch (method) {
                case Key.METHOD_POST:
                    Loopj.post(this.context, url, params, this.response);
                    break;
                case Key.METHOD_PATCH:
                    Loopj.patch(this.context, url, params, this.response);
                    break;
                default: // GET
                    Loopj.get(this.context, url, params, this.response);
            }
        } else {
            Log.i(TAG, "connect: falló la conexion con " + url);
        }
    }

    public void connect(String url, String method, Header[] headers, RequestParams params, String contentType) {
        if (url != null && !url.isEmpty()) {
            for (Header header: headers) {
                Loopj.client.addHeader(header.getName(), header.getValue());
            }

            switch (method) {
                case Key.METHOD_POST:
                    Loopj.post(this.context, url, headers, params, contentType, this.response);
                    break;
                default: // GET: No tiene contentType
                    Loopj.get(this.context, url, headers, params, this.response);
            }
        } else {
            Log.i(TAG, "connect: falló la conexion con " + url);
        }
    }

    public void connect(String url, String method, Header[] headers, HttpEntity entity, String contentType) {
        if (url != null && !url.isEmpty()) {
            for (Header header: headers) {
                Loopj.client.addHeader(header.getName(), header.getValue());
            }

            switch (method) {
                case Key.METHOD_POST:
                    Loopj.post(this.context, url, headers, entity, contentType, this.response);
                    break;
                default: // PATCH
                    Loopj.patch(this.context, url, headers, entity, contentType, this.response);
            }
        } else {
            Log.i(TAG, "connect: falló la conexion con " + url);
        }
    }

    public void connect(String url, String method, StringEntity entity, String contentType) {
        if (url != null && !url.isEmpty()) {
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            switch (method) {
                case Key.METHOD_POST:
                    Loopj.post(this.context, url, entity, contentType, this.response);
                    break;
                default: // GET
                    Loopj.get(this.context, url, entity, contentType, this.response);
            }
        } else {
            Log.i(TAG, "connect: falló la conexion con " + url);
        }
    }

    /**
     * Devuelve el json de la última consulta realizada si tuvo éxito o null si hubo un error.
     *
     * @return el objeto json o null si falló la consulta.
     */
    public JSONObject getLastQuery() {
        return this.json;
    }

    private final class WebResponse extends JsonHttpResponseHandler {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                if (statusCode == Response.OK) {
                    json = response;
                    listener.onSuccess(response);
                } else {
                    listener.onFailure(statusCode, headers, response.getString(Response.MESSAGE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            try {
                if(statusCode == Response.OK) {
                    json = new JSONObject();
                    json.put(Response.DATA, response);
                    listener.onSuccess(json);
                } else {
                    listener.onFailure(statusCode, headers, Response.MESSAGE_ERROR);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                listener.onFailure(statusCode, headers, Response.MESSAGE_ERROR);
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            String msg = "Error (1) - output:\n\t" + responseString + "\nHeader: " + Arrays.toString(headers) + "\nThrowables: " + throwable;
            Log.e(TAG, msg);
            listener.onFailure(statusCode, headers, Response.MESSAGE_ERROR);
            json = null;
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            String msg = "Error (2) - output:\n\t" + errorResponse;
            Log.e(TAG, msg);
            listener.onFailure(statusCode, headers, Response.MESSAGE_ERROR);
            json = null;
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e(TAG, "Error (3) - output:\n\t" + errorResponse);

            try {
                json = errorResponse;
                listener.onFailure(statusCode, headers, errorResponse.getString(Response.MESSAGE));
            } catch (JSONException | NullPointerException e) {
                //e.printStackTrace();
                listener.onFailure(statusCode, headers, Response.MESSAGE_ERROR);
            }
        }
    }

    public interface ResponseListener {
        void onSuccess(JSONObject json) throws JSONException;
        void onFailure(int statusCode, Header[] headers, String message);
    }
}
