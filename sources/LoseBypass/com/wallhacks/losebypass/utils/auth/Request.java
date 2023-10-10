/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.auth;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

public class Request {
    public HttpURLConnection connection;

    public Request(String url) throws MalformedURLException, IOException {
        this.connection = (HttpURLConnection)new URL(url).openConnection();
        this.connection.setDoOutput(true);
        this.connection.setDoInput(true);
        this.connection.setConnectTimeout(5000);
        this.connection.setReadTimeout(5000);
    }

    public void header(String key, String value) {
        this.connection.setRequestProperty(key, value);
    }

    public void post(String data) throws IOException {
        this.connection.setRequestMethod("POST");
        try (OutputStream os = this.connection.getOutputStream();){
            os.write(data.getBytes(StandardCharsets.UTF_8));
            return;
        }
    }

    public void post(Map<Object, Object> map) throws IOException {
        StringJoiner joiner = new StringJoiner("&");
        Iterator<Map.Entry<Object, Object>> iterator = map.entrySet().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.post(joiner.toString());
                return;
            }
            Map.Entry<Object, Object> entry = iterator.next();
            joiner.add(URLEncoder.encode(entry.getKey().toString(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
        }
    }

    public void get() throws ProtocolException {
        this.connection.setRequestMethod("GET");
    }

    public int response() throws IOException {
        return this.connection.getResponseCode();
    }

    public String body() throws IOException {
        StringBuilder builder = new StringBuilder();
        try (InputStreamReader r = new InputStreamReader(this.connection.getInputStream(), StandardCharsets.UTF_8);){
            int i;
            while ((i = ((Reader)r).read()) >= 0) {
                builder.append((char)i);
            }
            return builder.toString();
        }
    }

    public String error() throws IOException {
        StringBuilder builder = new StringBuilder();
        try (InputStreamReader r = new InputStreamReader(this.connection.getErrorStream(), StandardCharsets.UTF_8);){
            int i;
            while ((i = ((Reader)r).read()) >= 0) {
                builder.append((char)i);
            }
            return builder.toString();
        }
    }
}

