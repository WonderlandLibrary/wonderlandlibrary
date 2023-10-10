// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
import java.io.IOException;
import org.apache.logging.log4j.core.util.AuthorizationProvider;
import org.apache.logging.log4j.core.net.ssl.LaxHostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.net.URLConnection;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import java.net.HttpURLConnection;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import java.net.URL;

public class UrlConnectionFactory
{
    private static int DEFAULT_TIMEOUT;
    private static int connectTimeoutMillis;
    private static int readTimeoutMillis;
    private static final String JSON = "application/json";
    private static final String XML = "application/xml";
    private static final String PROPERTIES = "text/x-java-properties";
    private static final String TEXT = "text/plain";
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    
    public static HttpURLConnection createConnection(final URL url, final long lastModifiedMillis, final SslConfiguration sslConfiguration) throws IOException {
        final HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        final AuthorizationProvider provider = ConfigurationFactory.getAuthorizationProvider();
        if (provider != null) {
            provider.addAuthorization(urlConnection);
        }
        urlConnection.setAllowUserInteraction(false);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod("GET");
        if (UrlConnectionFactory.connectTimeoutMillis > 0) {
            urlConnection.setConnectTimeout(UrlConnectionFactory.connectTimeoutMillis);
        }
        if (UrlConnectionFactory.readTimeoutMillis > 0) {
            urlConnection.setReadTimeout(UrlConnectionFactory.readTimeoutMillis);
        }
        final String[] fileParts = url.getFile().split("\\.");
        final String type = fileParts[fileParts.length - 1].trim();
        final String contentType = isXml(type) ? "application/xml" : (isJson(type) ? "application/json" : (isProperties(type) ? "text/x-java-properties" : "text/plain"));
        urlConnection.setRequestProperty("Content-Type", contentType);
        if (lastModifiedMillis > 0L) {
            urlConnection.setIfModifiedSince(lastModifiedMillis);
        }
        if (url.getProtocol().equals("https") && sslConfiguration != null) {
            ((HttpsURLConnection)urlConnection).setSSLSocketFactory(sslConfiguration.getSslSocketFactory());
            if (!sslConfiguration.isVerifyHostName()) {
                ((HttpsURLConnection)urlConnection).setHostnameVerifier(LaxHostnameVerifier.INSTANCE);
            }
        }
        return urlConnection;
    }
    
    public static URLConnection createConnection(final URL url) throws IOException {
        URLConnection urlConnection = null;
        if (url.getProtocol().equals("https") || url.getProtocol().equals("http")) {
            urlConnection = createConnection(url, 0L, SslConfigurationFactory.getSslConfiguration());
        }
        else {
            urlConnection = url.openConnection();
        }
        return urlConnection;
    }
    
    private static boolean isXml(final String type) {
        return type.equalsIgnoreCase("xml");
    }
    
    private static boolean isJson(final String type) {
        return type.equalsIgnoreCase("json") || type.equalsIgnoreCase("jsn");
    }
    
    private static boolean isProperties(final String type) {
        return type.equalsIgnoreCase("properties");
    }
    
    static {
        UrlConnectionFactory.DEFAULT_TIMEOUT = 60000;
        UrlConnectionFactory.connectTimeoutMillis = UrlConnectionFactory.DEFAULT_TIMEOUT;
        UrlConnectionFactory.readTimeoutMillis = UrlConnectionFactory.DEFAULT_TIMEOUT;
    }
}
