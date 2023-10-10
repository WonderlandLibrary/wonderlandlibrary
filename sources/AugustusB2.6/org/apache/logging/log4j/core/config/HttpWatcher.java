// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.io.IOException;
import org.apache.logging.log4j.core.net.UrlConnectionFactory;
import org.apache.logging.log4j.core.util.Watcher;
import java.net.MalformedURLException;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.List;
import java.net.URL;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.util.AbstractWatcher;

@Plugin(name = "http", category = "Watcher", elementType = "watcher", printObject = true)
@PluginAliases({ "https" })
public class HttpWatcher extends AbstractWatcher
{
    private Logger LOGGER;
    private SslConfiguration sslConfiguration;
    private URL url;
    private volatile long lastModifiedMillis;
    private static final int NOT_MODIFIED = 304;
    private static final int OK = 200;
    private static final int BUF_SIZE = 1024;
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    
    public HttpWatcher(final Configuration configuration, final Reconfigurable reconfigurable, final List<ConfigurationListener> configurationListeners, final long lastModifiedMillis) {
        super(configuration, reconfigurable, configurationListeners);
        this.LOGGER = StatusLogger.getLogger();
        this.sslConfiguration = SslConfigurationFactory.getSslConfiguration();
        this.lastModifiedMillis = lastModifiedMillis;
    }
    
    @Override
    public long getLastModified() {
        return this.lastModifiedMillis;
    }
    
    @Override
    public boolean isModified() {
        return this.refreshConfiguration();
    }
    
    @Override
    public void watching(final Source source) {
        if (!source.getURI().getScheme().equals("http") && !source.getURI().getScheme().equals("https")) {
            throw new IllegalArgumentException("HttpWatcher requires a url using the HTTP or HTTPS protocol, not " + source.getURI().getScheme());
        }
        try {
            this.url = source.getURI().toURL();
        }
        catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid URL for HttpWatcher " + source.getURI(), ex);
        }
        super.watching(source);
    }
    
    @Override
    public Watcher newWatcher(final Reconfigurable reconfigurable, final List<ConfigurationListener> listeners, final long lastModifiedMillis) {
        final HttpWatcher watcher = new HttpWatcher(this.getConfiguration(), reconfigurable, listeners, lastModifiedMillis);
        if (this.getSource() != null) {
            watcher.watching(this.getSource());
        }
        return watcher;
    }
    
    private boolean refreshConfiguration() {
        try {
            final HttpURLConnection urlConnection = UrlConnectionFactory.createConnection(this.url, this.lastModifiedMillis, this.sslConfiguration);
            urlConnection.connect();
            try {
                final int code = urlConnection.getResponseCode();
                switch (code) {
                    case 304: {
                        this.LOGGER.debug("Configuration Not Modified");
                        return false;
                    }
                    case 200: {
                        try (final InputStream is = urlConnection.getInputStream()) {
                            final ConfigurationSource configSource = this.getConfiguration().getConfigurationSource();
                            configSource.setData(this.readStream(is));
                            configSource.setModifiedMillis(this.lastModifiedMillis = urlConnection.getLastModified());
                            this.LOGGER.debug("Content was modified for {}", this.url.toString());
                            return true;
                        }
                        catch (IOException e) {
                            try (final InputStream es = urlConnection.getErrorStream()) {
                                this.LOGGER.info("Error accessing configuration at {}: {}", this.url, this.readStream(es));
                            }
                            catch (IOException ioe3) {
                                this.LOGGER.error("Error accessing configuration at {}: {}", this.url, e.getMessage());
                            }
                            return false;
                        }
                        break;
                    }
                }
                if (code < 0) {
                    this.LOGGER.info("Invalid response code returned");
                }
                else {
                    this.LOGGER.info("Unexpected response code returned {}", (Object)code);
                }
                return false;
            }
            catch (IOException ioe) {
                this.LOGGER.error("Error accessing configuration at {}: {}", this.url, ioe.getMessage());
            }
        }
        catch (IOException ioe2) {
            this.LOGGER.error("Error connecting to configuration at {}: {}", this.url, ioe2.getMessage());
        }
        return false;
    }
    
    private byte[] readStream(final InputStream is) throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toByteArray();
    }
}
