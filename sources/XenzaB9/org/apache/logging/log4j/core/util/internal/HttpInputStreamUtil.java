// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util.internal;

import org.apache.logging.log4j.status.StatusLogger;
import java.io.ByteArrayOutputStream;
import org.apache.logging.log4j.core.config.ConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import org.apache.logging.log4j.core.net.UrlConnectionFactory;
import org.apache.logging.log4j.core.net.ssl.SslConfigurationFactory;
import java.net.HttpURLConnection;
import org.apache.logging.log4j.core.util.AuthorizationProvider;
import org.apache.logging.log4j.Logger;

public final class HttpInputStreamUtil
{
    private static final Logger LOGGER;
    private static final int NOT_MODIFIED = 304;
    private static final int NOT_AUTHORIZED = 401;
    private static final int NOT_FOUND = 404;
    private static final int OK = 200;
    private static final int BUF_SIZE = 1024;
    
    public static Result getInputStream(final LastModifiedSource source, final AuthorizationProvider authorizationProvider) {
        final Result result = new Result();
        try {
            final long lastModified = source.getLastModified();
            final HttpURLConnection connection = UrlConnectionFactory.createConnection(source.getURI().toURL(), lastModified, SslConfigurationFactory.getSslConfiguration(), authorizationProvider);
            connection.connect();
            try {
                final int code = connection.getResponseCode();
                switch (code) {
                    case 304: {
                        HttpInputStreamUtil.LOGGER.debug("Configuration not modified");
                        result.status = Status.NOT_MODIFIED;
                        return result;
                    }
                    case 404: {
                        HttpInputStreamUtil.LOGGER.debug("Unable to access {}: Not Found", source.toString());
                        result.status = Status.NOT_FOUND;
                        return result;
                    }
                    case 200: {
                        try (final InputStream is = connection.getInputStream()) {
                            source.setLastModified(connection.getLastModified());
                            HttpInputStreamUtil.LOGGER.debug("Content was modified for {}. previous lastModified: {}, new lastModified: {}", source.toString(), lastModified, connection.getLastModified());
                            result.status = Status.SUCCESS;
                            result.inputStream = new ByteArrayInputStream(readStream(is));
                            return result;
                        }
                        catch (final IOException e) {
                            try (final InputStream es = connection.getErrorStream()) {
                                HttpInputStreamUtil.LOGGER.info("Error accessing configuration at {}: {}", source.toString(), readStream(es));
                            }
                            catch (final IOException ioe) {
                                HttpInputStreamUtil.LOGGER.error("Error accessing configuration at {}: {}", source.toString(), e.getMessage());
                            }
                            throw new ConfigurationException("Unable to access " + source.toString(), e);
                        }
                    }
                    case 401: {
                        throw new ConfigurationException("Authorization failed");
                    }
                    default: {
                        if (code < 0) {
                            HttpInputStreamUtil.LOGGER.info("Invalid response code returned");
                        }
                        else {
                            HttpInputStreamUtil.LOGGER.info("Unexpected response code returned {}", (Object)code);
                        }
                        throw new ConfigurationException("Unable to access " + source.toString());
                    }
                }
            }
            finally {
                connection.disconnect();
            }
        }
        catch (final IOException e2) {
            HttpInputStreamUtil.LOGGER.warn("Error accessing {}: {}", source.toString(), e2.getMessage());
            throw new ConfigurationException("Unable to access " + source.toString(), e2);
        }
    }
    
    public static byte[] readStream(final InputStream is) throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toByteArray();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    public static class Result
    {
        private InputStream inputStream;
        private Status status;
        
        public Result() {
        }
        
        public Result(final Status status) {
            this.status = status;
        }
        
        public InputStream getInputStream() {
            return this.inputStream;
        }
        
        public Status getStatus() {
            return this.status;
        }
    }
}
