// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.status.StatusLogger;
import java.net.URL;
import java.io.File;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.LogEvent;
import java.net.URISyntaxException;
import java.net.URI;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "log4j", category = "Lookup")
public class Log4jLookup extends AbstractConfigurationAwareLookup
{
    public static final String KEY_CONFIG_LOCATION = "configLocation";
    public static final String KEY_CONFIG_PARENT_LOCATION = "configParentLocation";
    private static final Logger LOGGER;
    
    private static String asPath(final URI uri) {
        if (uri.getScheme() == null || uri.getScheme().equals("file")) {
            return uri.getPath();
        }
        return uri.toString();
    }
    
    private static URI getParent(final URI uri) throws URISyntaxException {
        final String s = uri.toString();
        final int offset = s.lastIndexOf(47);
        if (offset > -1) {
            return new URI(s.substring(0, offset));
        }
        return new URI("../");
    }
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        if (this.configuration != null) {
            final ConfigurationSource configSrc = this.configuration.getConfigurationSource();
            final File file = configSrc.getFile();
            if (file != null) {
                switch (key) {
                    case "configLocation": {
                        return file.getAbsolutePath();
                    }
                    case "configParentLocation": {
                        return file.getParentFile().getAbsolutePath();
                    }
                    default: {
                        return null;
                    }
                }
            }
            else {
                final URL url = configSrc.getURL();
                if (url != null) {
                    try {
                        switch (key) {
                            case "configLocation": {
                                return asPath(url.toURI());
                            }
                            case "configParentLocation": {
                                return asPath(getParent(url.toURI()));
                            }
                            default: {
                                return null;
                            }
                        }
                    }
                    catch (URISyntaxException use) {
                        Log4jLookup.LOGGER.error(use);
                    }
                }
            }
        }
        return null;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
