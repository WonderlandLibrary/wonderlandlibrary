/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.net.ssl.LaxHostnameVerifier;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
import org.apache.logging.log4j.core.util.AuthorizationProvider;

public class UrlConnectionFactory {
    private static int DEFAULT_TIMEOUT;
    private static int connectTimeoutMillis;
    private static int readTimeoutMillis;
    private static final String JSON = "application/json";
    private static final String XML = "application/xml";
    private static final String PROPERTIES = "text/x-java-properties";
    private static final String TEXT = "text/plain";
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    public static HttpURLConnection createConnection(URL url, long lastModifiedMillis, SslConfiguration sslConfiguration) throws IOException {
        String[] fileParts;
        String type;
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        AuthorizationProvider provider = ConfigurationFactory.getAuthorizationProvider();
        if (provider != null) {
            provider.addAuthorization(urlConnection);
        }
        urlConnection.setAllowUserInteraction(false);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod("GET");
        if (connectTimeoutMillis > 0) {
            urlConnection.setConnectTimeout(connectTimeoutMillis);
        }
        if (readTimeoutMillis > 0) {
            urlConnection.setReadTimeout(readTimeoutMillis);
        }
        String contentType = UrlConnectionFactory.isXml(type = (fileParts = url.getFile().split("\\."))[fileParts.length - 1].trim()) ? XML : (UrlConnectionFactory.isJson(type) ? JSON : (UrlConnectionFactory.isProperties(type) ? PROPERTIES : TEXT));
        urlConnection.setRequestProperty("Content-Type", contentType);
        if (lastModifiedMillis > 0L) {
            urlConnection.setIfModifiedSince(lastModifiedMillis);
        }
        if (url.getProtocol().equals(HTTPS) && sslConfiguration != null) {
            ((HttpsURLConnection)urlConnection).setSSLSocketFactory(sslConfiguration.getSslSocketFactory());
            if (!sslConfiguration.isVerifyHostName()) {
                ((HttpsURLConnection)urlConnection).setHostnameVerifier(LaxHostnameVerifier.INSTANCE);
            }
        }
        return urlConnection;
    }

    public static URLConnection createConnection(URL url) throws IOException {
        URLConnection urlConnection = null;
        urlConnection = url.getProtocol().equals(HTTPS) || url.getProtocol().equals(HTTP) ? UrlConnectionFactory.createConnection(url, 0L, SslConfigurationFactory.getSslConfiguration()) : url.openConnection();
        return urlConnection;
    }

    private static boolean isXml(String type) {
        return type.equalsIgnoreCase("xml");
    }

    private static boolean isJson(String type) {
        return type.equalsIgnoreCase("json") || type.equalsIgnoreCase("jsn");
    }

    private static boolean isProperties(String type) {
        return type.equalsIgnoreCase("properties");
    }

    static {
        connectTimeoutMillis = DEFAULT_TIMEOUT = 60000;
        readTimeoutMillis = DEFAULT_TIMEOUT;
    }
}

