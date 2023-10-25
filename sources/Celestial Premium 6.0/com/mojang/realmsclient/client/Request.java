/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.client;

import com.mojang.realmsclient.client.RealmsClientConfig;
import com.mojang.realmsclient.exception.RealmsHttpException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

public abstract class Request<T extends Request> {
    protected HttpURLConnection connection;
    private boolean connected;
    protected String url;
    private static final int DEFAULT_READ_TIMEOUT = 60000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;

    public Request(String url, int connectTimeout, int readTimeout) {
        try {
            this.url = url;
            Proxy proxy = RealmsClientConfig.getProxy();
            this.connection = proxy != null ? (HttpURLConnection)new URL(url).openConnection(proxy) : (HttpURLConnection)new URL(url).openConnection();
            this.connection.setConnectTimeout(connectTimeout);
            this.connection.setReadTimeout(readTimeout);
        }
        catch (MalformedURLException e) {
            throw new RealmsHttpException(e.getMessage(), e);
        }
        catch (IOException e) {
            throw new RealmsHttpException(e.getMessage(), e);
        }
    }

    public void cookie(String key, String value) {
        Request.cookie(this.connection, key, value);
    }

    public static void cookie(HttpURLConnection connection, String key, String value) {
        String cookie = connection.getRequestProperty("Cookie");
        if (cookie == null) {
            connection.setRequestProperty("Cookie", key + "=" + value);
        } else {
            connection.setRequestProperty("Cookie", cookie + ";" + key + "=" + value);
        }
    }

    public T header(String name, String value) {
        this.connection.addRequestProperty(name, value);
        return (T)this;
    }

    public int getRetryAfterHeader() {
        return Request.getRetryAfterHeader(this.connection);
    }

    public static int getRetryAfterHeader(HttpURLConnection connection) {
        String pauseTime = connection.getHeaderField("Retry-After");
        try {
            return Integer.valueOf(pauseTime);
        }
        catch (Exception ignored) {
            return 5;
        }
    }

    public int responseCode() {
        try {
            this.connect();
            return this.connection.getResponseCode();
        }
        catch (Exception e) {
            throw new RealmsHttpException(e.getMessage(), e);
        }
    }

    public String text() {
        try {
            this.connect();
            String result = null;
            result = this.responseCode() >= 400 ? this.read(this.connection.getErrorStream()) : this.read(this.connection.getInputStream());
            this.dispose();
            return result;
        }
        catch (IOException e) {
            throw new RealmsHttpException(e.getMessage(), e);
        }
    }

    private String read(InputStream in) throws IOException {
        if (in == null) {
            return "";
        }
        InputStreamReader streamReader = new InputStreamReader(in, "UTF-8");
        StringBuilder sb = new StringBuilder();
        int x = streamReader.read();
        while (x != -1) {
            sb.append((char)x);
            x = streamReader.read();
        }
        return sb.toString();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void dispose() {
        byte[] bytes = new byte[1024];
        try {
            int count = 0;
            InputStream in = this.connection.getInputStream();
            while ((count = in.read(bytes)) > 0) {
            }
            in.close();
        }
        catch (Exception ignore) {
            int ret;
            InputStream errorStream;
            block13: {
                errorStream = this.connection.getErrorStream();
                ret = 0;
                if (errorStream != null) break block13;
                return;
            }
            try {
                while ((ret = errorStream.read(bytes)) > 0) {
                }
                errorStream.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        finally {
            if (this.connection != null) {
                this.connection.disconnect();
            }
        }
    }

    protected T connect() {
        if (this.connected) {
            return (T)this;
        }
        T t = this.doConnect();
        this.connected = true;
        return t;
    }

    protected abstract T doConnect();

    public static Request<?> get(String url) {
        return new Get(url, 5000, 60000);
    }

    public static Request<?> get(String url, int connectTimeoutMillis, int readTimeoutMillis) {
        return new Get(url, connectTimeoutMillis, readTimeoutMillis);
    }

    public static Request<?> post(String uri, String content) {
        return new Post(uri, content, 5000, 60000);
    }

    public static Request<?> post(String uri, String content, int connectTimeoutMillis, int readTimeoutMillis) {
        return new Post(uri, content, connectTimeoutMillis, readTimeoutMillis);
    }

    public static Request<?> delete(String url) {
        return new Delete(url, 5000, 60000);
    }

    public static Request<?> put(String url, String content) {
        return new Put(url, content, 5000, 60000);
    }

    public static Request<?> put(String url, String content, int connectTimeoutMillis, int readTimeoutMillis) {
        return new Put(url, content, connectTimeoutMillis, readTimeoutMillis);
    }

    public String getHeader(String header) {
        return Request.getHeader(this.connection, header);
    }

    public static String getHeader(HttpURLConnection connection, String header) {
        try {
            return connection.getHeaderField(header);
        }
        catch (Exception ignored) {
            return "";
        }
    }

    public static class Post
    extends Request<Post> {
        private final String content;

        public Post(String uri, String content, int connectTimeout, int readTimeout) {
            super(uri, connectTimeout, readTimeout);
            this.content = content;
        }

        @Override
        public Post doConnect() {
            try {
                if (this.content != null) {
                    this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                }
                this.connection.setDoInput(true);
                this.connection.setDoOutput(true);
                this.connection.setUseCaches(false);
                this.connection.setRequestMethod("POST");
                OutputStream out = this.connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
                writer.write(this.content);
                writer.close();
                out.flush();
                return this;
            }
            catch (Exception e) {
                throw new RealmsHttpException(e.getMessage(), e);
            }
        }
    }

    public static class Put
    extends Request<Put> {
        private final String content;

        public Put(String uri, String content, int connectTimeout, int readTimeout) {
            super(uri, connectTimeout, readTimeout);
            this.content = content;
        }

        @Override
        public Put doConnect() {
            try {
                if (this.content != null) {
                    this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                }
                this.connection.setDoOutput(true);
                this.connection.setDoInput(true);
                this.connection.setRequestMethod("PUT");
                OutputStream out = this.connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
                writer.write(this.content);
                writer.close();
                out.flush();
                return this;
            }
            catch (Exception e) {
                throw new RealmsHttpException(e.getMessage(), e);
            }
        }
    }

    public static class Get
    extends Request<Get> {
        public Get(String uri, int connectTimeout, int readTimeout) {
            super(uri, connectTimeout, readTimeout);
        }

        @Override
        public Get doConnect() {
            try {
                this.connection.setDoInput(true);
                this.connection.setDoOutput(true);
                this.connection.setUseCaches(false);
                this.connection.setRequestMethod("GET");
                return this;
            }
            catch (Exception e) {
                throw new RealmsHttpException(e.getMessage(), e);
            }
        }
    }

    public static class Delete
    extends Request<Delete> {
        public Delete(String uri, int connectTimeout, int readTimeout) {
            super(uri, connectTimeout, readTimeout);
        }

        @Override
        public Delete doConnect() {
            try {
                this.connection.setDoOutput(true);
                this.connection.setRequestMethod("DELETE");
                this.connection.connect();
                return this;
            }
            catch (Exception e) {
                throw new RealmsHttpException(e.getMessage(), e);
            }
        }
    }
}

