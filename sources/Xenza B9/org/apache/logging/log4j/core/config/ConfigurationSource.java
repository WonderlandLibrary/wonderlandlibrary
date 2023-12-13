// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.util.Constants;
import java.net.URLConnection;
import java.net.URISyntaxException;
import java.net.JarURLConnection;
import org.apache.logging.log4j.core.net.UrlConnectionFactory;
import org.apache.logging.log4j.core.util.Loader;
import java.net.MalformedURLException;
import org.apache.logging.log4j.util.LoaderUtil;
import java.io.FileNotFoundException;
import org.apache.logging.log4j.core.util.FileUtils;
import java.io.FileInputStream;
import java.net.URI;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Objects;
import java.io.File;
import org.apache.logging.log4j.core.util.Source;
import java.io.InputStream;

public class ConfigurationSource
{
    public static final ConfigurationSource NULL_SOURCE;
    public static final ConfigurationSource COMPOSITE_SOURCE;
    private static final String HTTPS = "https";
    private static final String JAR = "jar";
    private final InputStream stream;
    private volatile byte[] data;
    private volatile Source source;
    private final long lastModified;
    private volatile long modifiedMillis;
    
    public ConfigurationSource(final InputStream stream, final File file) {
        this.stream = Objects.requireNonNull(stream, "stream is null");
        this.data = null;
        this.source = new Source(file);
        long modified = 0L;
        try {
            modified = file.lastModified();
        }
        catch (final Exception ex) {}
        this.lastModified = modified;
    }
    
    public ConfigurationSource(final InputStream stream, final Path path) {
        this.stream = Objects.requireNonNull(stream, "stream is null");
        this.data = null;
        this.source = new Source(path);
        long modified = 0L;
        try {
            modified = Files.getLastModifiedTime(path, new LinkOption[0]).toMillis();
        }
        catch (final Exception ex) {}
        this.lastModified = modified;
    }
    
    public ConfigurationSource(final InputStream stream, final URL url) {
        this.stream = Objects.requireNonNull(stream, "stream is null");
        this.data = null;
        this.lastModified = 0L;
        this.source = new Source(url);
    }
    
    public ConfigurationSource(final InputStream stream, final URL url, final long lastModified) {
        this.stream = Objects.requireNonNull(stream, "stream is null");
        this.data = null;
        this.lastModified = lastModified;
        this.source = new Source(url);
    }
    
    public ConfigurationSource(final InputStream stream) throws IOException {
        this(toByteArray(stream), null, 0L);
    }
    
    public ConfigurationSource(final Source source, final byte[] data, final long lastModified) {
        Objects.requireNonNull(source, "source is null");
        this.data = Objects.requireNonNull(data, "data is null");
        this.stream = new ByteArrayInputStream(data);
        this.lastModified = lastModified;
        this.source = source;
    }
    
    private ConfigurationSource(final byte[] data, final URL url, final long lastModified) {
        this.data = Objects.requireNonNull(data, "data is null");
        this.stream = new ByteArrayInputStream(data);
        this.lastModified = lastModified;
        if (url == null) {
            this.data = data;
        }
        else {
            this.source = new Source(url);
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
        return (this.source == null) ? null : this.source.getFile();
    }
    
    private boolean isFile() {
        return this.source != null && this.source.getFile() != null;
    }
    
    private boolean isURL() {
        return this.source != null && this.source.getURI() != null;
    }
    
    private boolean isLocation() {
        return this.source != null && this.source.getLocation() != null;
    }
    
    public URL getURL() {
        return (this.source == null) ? null : this.source.getURL();
    }
    
    @Deprecated
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
        return (this.source == null) ? null : this.source.getURI();
    }
    
    public long getLastModified() {
        return this.lastModified;
    }
    
    public String getLocation() {
        return (this.source == null) ? null : this.source.getLocation();
    }
    
    public InputStream getInputStream() {
        return this.stream;
    }
    
    public ConfigurationSource resetInputStream() throws IOException {
        if (this.source != null && this.data != null) {
            return new ConfigurationSource(this.source, this.data, this.lastModified);
        }
        if (this.isFile()) {
            return new ConfigurationSource(new FileInputStream(this.getFile()), this.getFile());
        }
        if (this.isURL() && this.data != null) {
            return new ConfigurationSource(this.data, this.getURL(), (this.modifiedMillis == 0L) ? this.lastModified : this.modifiedMillis);
        }
        if (this.isURL()) {
            return fromUri(this.getURI());
        }
        if (this.data != null) {
            return new ConfigurationSource(this.data, null, this.lastModified);
        }
        return null;
    }
    
    @Override
    public String toString() {
        if (this.isLocation()) {
            return this.getLocation();
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
            catch (final FileNotFoundException ex) {
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
            return getConfigurationSource(configLocation.toURL());
        }
        catch (final MalformedURLException ex2) {
            ConfigurationFactory.LOGGER.error("Invalid URL {}", configLocation.toString(), ex2);
            return null;
        }
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
            final File file = FileUtils.fileFromUri(url.toURI());
            final URLConnection urlConnection = UrlConnectionFactory.createConnection(url);
            try {
                if (file != null) {
                    return new ConfigurationSource(urlConnection.getInputStream(), FileUtils.fileFromUri(url.toURI()));
                }
                if ("jar".equals(url.getProtocol())) {
                    final long lastModified = new File(((JarURLConnection)urlConnection).getJarFile().getName()).lastModified();
                    return new ConfigurationSource(urlConnection.getInputStream(), url, lastModified);
                }
                return new ConfigurationSource(urlConnection.getInputStream(), url, urlConnection.getLastModified());
            }
            catch (final FileNotFoundException ex) {
                ConfigurationFactory.LOGGER.info("Unable to locate file {}, ignoring.", url.toString());
                return null;
            }
        }
        catch (final IOException | URISyntaxException ex2) {
            ConfigurationFactory.LOGGER.warn("Error accessing {} due to {}, ignoring.", url.toString(), ex2.getMessage());
            return null;
        }
    }
    
    static {
        NULL_SOURCE = new ConfigurationSource(Constants.EMPTY_BYTE_ARRAY, null, 0L);
        COMPOSITE_SOURCE = new ConfigurationSource(Constants.EMPTY_BYTE_ARRAY, null, 0L);
    }
}
