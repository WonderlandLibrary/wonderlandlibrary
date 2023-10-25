/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.lookup;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.AbstractConfigurationAwareLookup;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="log4j", category="Lookup")
public class Log4jLookup
extends AbstractConfigurationAwareLookup {
    public static final String KEY_CONFIG_LOCATION = "configLocation";
    public static final String KEY_CONFIG_PARENT_LOCATION = "configParentLocation";
    private static final Logger LOGGER = StatusLogger.getLogger();

    private static String asPath(URI uri) {
        if (uri.getScheme() == null || uri.getScheme().equals("file")) {
            return uri.getPath();
        }
        return uri.toString();
    }

    private static URI getParent(URI uri) throws URISyntaxException {
        String s = uri.toString();
        int offset = s.lastIndexOf(47);
        if (offset > -1) {
            return new URI(s.substring(0, offset));
        }
        return new URI("../");
    }

    @Override
    public String lookup(LogEvent event, String key) {
        if (this.configuration != null) {
            ConfigurationSource configSrc = this.configuration.getConfigurationSource();
            File file = configSrc.getFile();
            if (file != null) {
                switch (key) {
                    case "configLocation": {
                        return file.getAbsolutePath();
                    }
                    case "configParentLocation": {
                        return file.getParentFile().getAbsolutePath();
                    }
                }
                return null;
            }
            URL url = configSrc.getURL();
            if (url != null) {
                try {
                    switch (key) {
                        case "configLocation": {
                            return Log4jLookup.asPath(url.toURI());
                        }
                        case "configParentLocation": {
                            return Log4jLookup.asPath(Log4jLookup.getParent(url.toURI()));
                        }
                    }
                    return null;
                }
                catch (URISyntaxException use) {
                    LOGGER.error(use);
                }
            }
        }
        return null;
    }
}

