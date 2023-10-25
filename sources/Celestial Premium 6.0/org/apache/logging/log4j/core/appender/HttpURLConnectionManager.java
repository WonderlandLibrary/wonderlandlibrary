/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Objects;
import javax.net.ssl.HttpsURLConnection;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.HttpManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.net.ssl.LaxHostnameVerifier;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;

public class HttpURLConnectionManager
extends HttpManager {
    private static final Charset CHARSET = Charset.forName("US-ASCII");
    private final URL url;
    private final boolean isHttps;
    private final String method;
    private final int connectTimeoutMillis;
    private final int readTimeoutMillis;
    private final Property[] headers;
    private final SslConfiguration sslConfiguration;
    private final boolean verifyHostname;

    public HttpURLConnectionManager(Configuration configuration, LoggerContext loggerContext, String name, URL url, String method, int connectTimeoutMillis, int readTimeoutMillis, Property[] headers, SslConfiguration sslConfiguration, boolean verifyHostname) {
        super(configuration, loggerContext, name);
        this.url = url;
        if (!url.getProtocol().equalsIgnoreCase("http") && !url.getProtocol().equalsIgnoreCase("https")) {
            throw new ConfigurationException("URL must have scheme http or https");
        }
        this.isHttps = this.url.getProtocol().equalsIgnoreCase("https");
        this.method = Objects.requireNonNull(method, "method");
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.headers = headers != null ? headers : new Property[0];
        this.sslConfiguration = sslConfiguration;
        if (this.sslConfiguration != null && !this.isHttps) {
            throw new ConfigurationException("SSL configuration can only be specified with URL scheme https");
        }
        this.verifyHostname = verifyHostname;
    }

    @Override
    public void send(Layout<?> layout, LogEvent event) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection)this.url.openConnection();
        urlConnection.setAllowUserInteraction(false);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod(this.method);
        if (this.connectTimeoutMillis > 0) {
            urlConnection.setConnectTimeout(this.connectTimeoutMillis);
        }
        if (this.readTimeoutMillis > 0) {
            urlConnection.setReadTimeout(this.readTimeoutMillis);
        }
        if (layout.getContentType() != null) {
            urlConnection.setRequestProperty("Content-Type", layout.getContentType());
        }
        for (Property property : this.headers) {
            urlConnection.setRequestProperty(property.getName(), property.isValueNeedsLookup() ? this.getConfiguration().getStrSubstitutor().replace(event, property.getValue()) : property.getValue());
        }
        if (this.sslConfiguration != null) {
            ((HttpsURLConnection)urlConnection).setSSLSocketFactory(this.sslConfiguration.getSslSocketFactory());
        }
        if (this.isHttps && !this.verifyHostname) {
            ((HttpsURLConnection)urlConnection).setHostnameVerifier(LaxHostnameVerifier.INSTANCE);
        }
        byte[] msg = layout.toByteArray(event);
        urlConnection.setFixedLengthStreamingMode(msg.length);
        urlConnection.connect();
        try (OutputStream os = urlConnection.getOutputStream();){
            os.write(msg);
        }
        byte[] buffer = new byte[1024];
        try {
            Throwable throwable = null;
            try (InputStream is = urlConnection.getInputStream();){
                while (-1 != is.read(buffer)) {
                }
            }
            catch (Throwable throwable2) {
                Throwable throwable3 = throwable2;
                throw throwable2;
            }
        }
        catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            try (InputStream es = urlConnection.getErrorStream();){
                stringBuilder.append(urlConnection.getResponseCode());
                if (urlConnection.getResponseMessage() != null) {
                    stringBuilder.append(' ').append(urlConnection.getResponseMessage());
                }
                if (es != null) {
                    int n;
                    stringBuilder.append(" - ");
                    while (-1 != (n = es.read(buffer))) {
                        stringBuilder.append(new String(buffer, 0, n, CHARSET));
                    }
                }
            }
            if (urlConnection.getResponseCode() > -1) {
                throw new IOException(stringBuilder.toString());
            }
            throw e;
        }
    }
}

