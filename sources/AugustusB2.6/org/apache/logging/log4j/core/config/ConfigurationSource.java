// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.util.AuthorizationProvider;
import org.apache.logging.log4j.core.net.ssl.LaxHostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.util.Loader;
import java.net.URLConnection;
import java.net.MalformedURLException;
import org.apache.logging.log4j.core.net.UrlConnectionFactory;
import org.apache.logging.log4j.util.LoaderUtil;
import java.io.FileNotFoundException;
import org.apache.logging.log4j.core.util.FileUtils;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.net.URI;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import org.apache.logging.log4j.core.util.Source;
import java.io.InputStream;
import java.net.URL;
import java.io.File;

public class ConfigurationSource
{
    public static final ConfigurationSource NULL_SOURCE;
    public static final ConfigurationSource COMPOSITE_SOURCE;
    private static final String HTTPS = "https";
    private final File file;
    private final URL url;
    private final String location;
    private final InputStream stream;
    private volatile byte[] data;
    private volatile Source source;
    private final long lastModified;
    private volatile long modifiedMillis;
    
    public ConfigurationSource(final InputStream stream, final File file) {
        this.stream = Objects.requireNonNull(stream, "stream is null");
        this.file = Objects.requireNonNull(file, "file is null");
        this.location = file.getAbsolutePath();
        this.url = null;
        this.data = null;
        long modified = 0L;
        try {
            modified = file.lastModified();
        }
        catch (Exception ex) {}
        this.lastModified = modified;
    }
    
    public ConfigurationSource(final InputStream stream, final URL url) {
        this.stream = Objects.requireNonNull(stream, "stream is null");
        this.url = Objects.requireNonNull(url, "URL is null");
        this.location = url.toString();
        this.file = null;
        this.data = null;
        this.lastModified = 0L;
    }
    
    public ConfigurationSource(final InputStream stream, final URL url, final long lastModified) {
        this.stream = Objects.requireNonNull(stream, "stream is null");
        this.url = Objects.requireNonNull(url, "URL is null");
        this.location = url.toString();
        this.file = null;
        this.data = null;
        this.lastModified = lastModified;
    }
    
    public ConfigurationSource(final InputStream stream) throws IOException {
        this(toByteArray(stream), null, 0L);
    }
    
    public ConfigurationSource(final Source source, final byte[] data, final long lastModified) throws IOException {
        Objects.requireNonNull(source, "source is null");
        this.data = Objects.requireNonNull(data, "data is null");
        this.stream = new ByteArrayInputStream(data);
        this.file = source.getFile();
        this.url = source.getURI().toURL();
        this.location = source.getLocation();
        this.lastModified = lastModified;
    }
    
    private ConfigurationSource(final byte[] data, final URL url, final long lastModified) {
        this.data = Objects.requireNonNull(data, "data is null");
        this.stream = new ByteArrayInputStream(data);
        this.file = null;
        this.url = url;
        this.location = null;
        this.lastModified = lastModified;
        if (url == null) {
            this.data = data;
        }
    }
    
    private static byte[] toByteArray(final InputStream inputStream) throws IOException {
        final int buffSize = Math.max(4096, inputStream.available());
        final ByteArrayOutputStream contents = new ByteArrayOutputStream(buffSize);
        final byte[] buff = new byte[buffSize];
        for (int length = inputStream.read(buff); length > 0; length = inputStream.read(buff)) {
            contents.write(buff, 0, length);
        }
        return contents.toByteArray();
    }
    
    public File getFile() {
        return this.file;
    }
    
    public URL getURL() {
        return this.url;
    }
    
    public void setSource(final Source source) {
        this.source = source;
    }
    
    public void setData(final byte[] data) {
        this.data = data;
    }
    
    public void setModifiedMillis(final long modifiedMillis) {
        this.modifiedMillis = modifiedMillis;
    }
    
    public URI getURI() {
        URI sourceURI = null;
        if (this.url != null) {
            try {
                sourceURI = this.url.toURI();
            }
            catch (URISyntaxException ex2) {}
        }
        if (sourceURI == null && this.file != null) {
            sourceURI = this.file.toURI();
        }
        if (sourceURI == null && this.location != null) {
            try {
                sourceURI = new URI(this.location);
            }
            catch (URISyntaxException ex) {
                try {
                    sourceURI = new URI("file://" + this.location);
                }
                catch (URISyntaxException ex3) {}
            }
        }
        return sourceURI;
    }
    
    public long getLastModified() {
        return this.lastModified;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public InputStream getInputStream() {
        return this.stream;
    }
    
    public ConfigurationSource resetInputStream() throws IOException {
        if (this.source != null) {
            return new ConfigurationSource(this.source, this.data, this.lastModified);
        }
        if (this.file != null) {
            return new ConfigurationSource(new FileInputStream(this.file), this.file);
        }
        if (this.url != null && this.data != null) {
            return new ConfigurationSource(this.data, this.url, (this.modifiedMillis == 0L) ? this.lastModified : this.modifiedMillis);
        }
        if (this.url != null) {
            return fromUri(this.getURI());
        }
        if (this.data != null) {
            return new ConfigurationSource(this.data, null, this.lastModified);
        }
        return null;
    }
    
    @Override
    public String toString() {
        if (this.location != null) {
            return this.location;
        }
        if (this == ConfigurationSource.NULL_SOURCE) {
            return "NULL_SOURCE";
        }
        final int length = (this.data == null) ? -1 : this.data.length;
        return "stream (" + length + " bytes, unknown location)";
    }
    
    public static ConfigurationSource fromUri(final URI configLocation) {
        final File configFile = FileUtils.fileFromUri(configLocation);
        if (configFile != null && configFile.exists() && configFile.canRead()) {
            try {
                return new ConfigurationSource(new FileInputStream(configFile), configFile);
            }
            catch (FileNotFoundException ex) {
                ConfigurationFactory.LOGGER.error("Cannot locate file {}", configLocation.getPath(), ex);
            }
        }
        if (ConfigurationFactory.isClassLoaderUri(configLocation)) {
            final ClassLoader loader = LoaderUtil.getThreadContextClassLoader();
            final String path = ConfigurationFactory.extractClassLoaderUriPath(configLocation);
            return fromResource(path, loader);
        }
        if (!configLocation.isAbsolute()) {
            ConfigurationFactory.LOGGER.error("File not found in file system or classpath: {}", configLocation.toString());
            return null;
        }
        try {
            final URL url = configLocation.toURL();
            final URLConnection urlConnection = UrlConnectionFactory.createConnection(url);
            final InputStream is = urlConnection.getInputStream();
            final long lastModified = urlConnection.getLastModified();
            return new ConfigurationSource(is, configLocation.toURL(), lastModified);
        }
        catch (FileNotFoundException ex) {
            ConfigurationFactory.LOGGER.warn("Could not locate file {}", configLocation.toString());
        }
        catch (MalformedURLException ex2) {
            ConfigurationFactory.LOGGER.error("Invalid URL {}", configLocation.toString(), ex2);
        }
        catch (Exception ex3) {
            ConfigurationFactory.LOGGER.error("Unable to access {}", configLocation.toString(), ex3);
        }
        return null;
    }
    
    public static ConfigurationSource fromResource(final String resource, final ClassLoader loader) {
        final URL url = Loader.getResource(resource, loader);
        if (url == null) {
            return null;
        }
        return getConfigurationSource(url);
    }
    
    private static ConfigurationSource getConfigurationSource(final URL url) {
        try {
            final URLConnection urlConnection = url.openConnection();
            final AuthorizationProvider provider = ConfigurationFactory.authorizationProvider(PropertiesUtil.getProperties());
            provider.addAuthorization(urlConnection);
            if (url.getProtocol().equals("https")) {
                final SslConfiguration sslConfiguration = SslConfigurationFactory.getSslConfiguration();
                if (sslConfiguration != null) {
                    ((HttpsURLConnection)urlConnection).setSSLSocketFactory(sslConfiguration.getSslSocketFactory());
                    if (!sslConfiguration.isVerifyHostName()) {
                        ((HttpsURLConnection)urlConnection).setHostnameVerifier(LaxHostnameVerifier.INSTANCE);
                    }
                }
            }
            final File file = FileUtils.fileFromUri(url.toURI());
            try {
                if (file != null) {
                    return new ConfigurationSource(urlConnection.getInputStream(), FileUtils.fileFromUri(url.toURI()));
                }
                return new ConfigurationSource(urlConnection.getInputStream(), url, urlConnection.getLastModified());
            }
            catch (FileNotFoundException ex2) {
                ConfigurationFactory.LOGGER.info("Unable to locate file {}, ignoring.", url.toString());
                return null;
            }
        }
        catch (IOException | URISyntaxException ex4) {
            final Exception ex3;
            final Exception ex = ex3;
            ConfigurationFactory.LOGGER.warn("Error accessing {} due to {}, ignoring.", url.toString(), ex.getMessage());
            return null;
        }
    }
    
    static {
        NULL_SOURCE = new ConfigurationSource(Constants.EMPTY_BYTE_ARRAY, null, 0L);
        COMPOSITE_SOURCE = new ConfigurationSource(Constants.EMPTY_BYTE_ARRAY, null, 0L);
    }
}
