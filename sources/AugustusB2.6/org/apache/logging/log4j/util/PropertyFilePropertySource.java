// 
// Decompiled by Procyon v0.5.36
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
        super(loadPropertiesFile(fileName));
    }
    
    private static Properties loadPropertiesFile(final String fileName) {
        final Properties props = new Properties();
        for (final URL url : LoaderUtil.findResources(fileName)) {
            try (final InputStream in = url.openStream()) {
                props.load(in);
            }
            catch (IOException e) {
                LowLevelLogUtil.logException("Unable to read " + url, e);
            }
        }
        return props;
    }
    
    @Override
    public int getPriority() {
        return 0;
    }
}
