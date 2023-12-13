// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net;

import java.net.JarURLConnection;
import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.core.net.ssl.LaxHostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Arrays;
import org.apache.logging.log4j.util.Strings;
import java.util.Locale;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.net.URLConnection;
import org.apache.logging.log4j.core.util.AuthorizationProvider;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import java.net.URL;

public class UrlConnectionFactory
{
    private static final int DEFAULT_TIMEOUT = 60000;
    private static final int connectTimeoutMillis = 60000;
    private static final int readTimeoutMillis = 60000;
    private static final String JSON = "application/json";
    private static final String XML = "application/xml";
    private static final String PROPERTIES = "text/x-java-properties";
    private static final String TEXT = "text/plain";
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String JAR = "jar";
    private static final String DEFAULT_ALLOWED_PROTOCOLS = "https, file, jar";
    private static final String NO_PROTOCOLS = "_none";
    public static final String ALLOWED_PROTOCOLS = "log4j2.Configuration.allowedProtocols";
    
    public static <T extends URLConnection> T createConnection(final URL url, final long lastModifiedMillis, final SslConfiguration sslConfiguration, final AuthorizationProvider authorizationProvider) throws IOException {
        final PropertiesUtil props = PropertiesUtil.getProperties();
        final List<String> allowed = Arrays.asList(Strings.splitList(props.getStringProperty("log4j2.Configuration.allowedProtocols", "https, file, jar").toLowerCase(Locale.ROOT)));
        if (allowed.size() == 1 && "_none".equals(allowed.get(0))) {
            throw new ProtocolException("No external protocols have been enabled");
        }
        final String protocol = url.getProtocol();
        if (protocol == null) {
            throw new ProtocolException("No protocol was specified on " + url.toString());
        }
        if (!allowed.contains(protocol)) {
            throw new ProtocolException("Protocol " + protocol + " has not been enabled as an allowed protocol");
        }
        URLConnection urlConnection;
        if (url.getProtocol().equals("http") || url.getProtocol().equals("https")) {
            final HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            if (authorizationProvider != null) {
                authorizationProvider.addAuthorization(httpURLConnection);
            }
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(60000);
            httpURLConnection.setReadTimeout(60000);
            final String[] fileParts = url.getFile().split("\\.");
            final String type = fileParts[fileParts.length - 1].trim();
            final String contentType = isXml(type) ? "application/xml" : (isJson(type) ? "application/json" : (isProperties(type) ? "text/x-java-properties" : "text/plain"));
            httpURLConnection.setRequestProperty("Content-Type", contentType);
            if (lastModifiedMillis > 0L) {
                httpURLConnection.setIfModifiedSince(lastModifiedMillis);
            }
            if (url.getProtocol().equals("https") && sslConfiguration != null) {
                ((HttpsURLConnection)httpURLConnection).setSSLSocketFactory(sslConfiguration.getSslSocketFactory());
                if (!sslConfiguration.isVerifyHostName()) {
                    ((HttpsURLConnection)httpURLConnection).setHostnameVerifier(LaxHostnameVerifier.INSTANCE);
                }
            }
            urlConnection = httpURLConnection;
        }
        else if (url.getProtocol().equals("jar")) {
            urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
        }
        else {
            urlConnection = url.openConnection();
        }
        return (T)urlConnection;
    }
    
    public static URLConnection createConnection(final URL url) throws IOException {
        URLConnection urlConnection = null;
        if (url.getProtocol().equals("https") || url.getProtocol().equals("http")) {
            final AuthorizationProvider provider = ConfigurationFactory.authorizationProvider(PropertiesUtil.getProperties());
            urlConnection = createConnection(url, 0L, SslConfigurationFactory.getSslConfiguration(), provider);
        }
        else {
            urlConnection = url.openConnection();
            if (urlConnection instanceof JarURLConnection) {
                urlConnection.setUseCaches(false);
            }
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
}
