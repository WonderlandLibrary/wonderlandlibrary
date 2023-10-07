// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import java.io.InputStream;
import java.util.Iterator;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class PropertyFilePropertySource extends PropertiesPropertySource
{
    public PropertyFilePropertySource(final String fileName) {
        this(fileName, true);
    }
    
    public PropertyFilePropertySource(final String fileName, final boolean useTccl) {
        super(loadPropertiesFile(fileName, useTccl));
    }
    
    private static Properties loadPropertiesFile(final String fileName, final boolean useTccl) {
        final Properties props = new Properties();
        for (final URL url : LoaderUtil.findResources(fileName, useTccl)) {
            try (final InputStream in = url.openStream()) {
                props.load(in);
            }
            catch (final IOException e) {
                LowLevelLogUtil.logException("Unable to read " + url, e);
            }
        }
        return props;
    }
}
