/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.auth.util;

import com.github.steveice10.mc.auth.exception.request.InvalidCredentialsException;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.exception.request.ServiceUnavailableException;
import com.github.steveice10.mc.auth.exception.request.UserMigratedException;
import com.github.steveice10.mc.auth.util.UUIDSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.UUID;

public class HTTP {
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter((Type)((Object)UUID.class), new UUIDSerializer()).create();

    private HTTP() {
    }

    public static void makeRequest(Proxy proxy, String url, Object input) throws RequestException {
        HTTP.makeRequest(proxy, url, input, null);
    }

    public static <T> T makeRequest(Proxy proxy, String url, Object input, Class<T> clazz) throws RequestException {
        JsonElement response = null;
        try {
            String jsonString = input == null ? HTTP.performGetRequest(proxy, url) : HTTP.performPostRequest(proxy, url, GSON.toJson(input), "application/json");
            response = GSON.fromJson(jsonString, JsonElement.class);
        }
        catch (Exception e) {
            throw new ServiceUnavailableException("Could not make request to '" + url + "'.", e);
        }
        if (response != null) {
            JsonObject object;
            if (response.isJsonObject() && (object = response.getAsJsonObject()).has("error")) {
                String errorMessage;
                String error = object.get("error").getAsString();
                String cause = object.has("cause") ? object.get("cause").getAsString() : "";
                String string = errorMessage = object.has("errorMessage") ? object.get("errorMessage").getAsString() : "";
                if (!error.equals("")) {
                    if (error.equals("ForbiddenOperationException")) {
                        if (cause != null && cause.equals("UserMigratedException")) {
                            throw new UserMigratedException(errorMessage);
                        }
                        throw new InvalidCredentialsException(errorMessage);
                    }
                    throw new RequestException(errorMessage);
                }
            }
            if (clazz != null) {
                return GSON.fromJson(response, clazz);
            }
        }
        return null;
    }

    private static HttpURLConnection createUrlConnection(Proxy proxy, String url) throws IOException {
        if (proxy == null) {
            throw new IllegalArgumentException("Proxy cannot be null.");
        }
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null.");
        }
        HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection(proxy);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setUseCaches(false);
        return connection;
    }

    private static String performGetRequest(Proxy proxy, String url) throws IOException {
        if (proxy == null) {
            throw new IllegalArgumentException("Proxy cannot be null.");
        }
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null.");
        }
        HttpURLConnection connection = HTTP.createUrlConnection(proxy, url);
        connection.setDoInput(true);
        InputStream in = null;
        try {
            int responseCode = connection.getResponseCode();
            in = responseCode == 200 ? connection.getInputStream() : connection.getErrorStream();
            if (in != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                String string = result.toString();
                return string;
            }
            return "";
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException iOException) {}
            }
        }
    }

    private static String performPostRequest(Proxy proxy, String url, String post, String type) throws IOException {
        if (proxy == null) {
            throw new IllegalArgumentException("Proxy cannot be null.");
        }
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null.");
        }
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        byte[] bytes = post.getBytes("UTF-8");
        HttpURLConnection connection = HTTP.createUrlConnection(proxy, url);
        connection.setRequestProperty("Content-Type", String.valueOf(type) + "; charset=utf-8");
        connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
        connection.setDoInput(true);
        connection.setDoOutput(true);
        OutputStream out = null;
        try {
            out = connection.getOutputStream();
            out.write(bytes);
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException iOException) {}
            }
        }
        InputStream in = null;
        try {
            int responseCode = connection.getResponseCode();
            in = responseCode == 200 ? connection.getInputStream() : connection.getErrorStream();
            if (in != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                String string = result.toString();
                return string;
            }
            return "";
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException iOException) {}
            }
        }
    }
}

