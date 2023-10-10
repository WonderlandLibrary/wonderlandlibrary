// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.status.StatusLogger;
import javax.naming.NamingException;
import java.util.Objects;
import org.apache.logging.log4j.core.net.JndiManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "jndi", category = "Lookup")
public class JndiLookup extends AbstractLookup
{
    private static final Logger LOGGER;
    private static final Marker LOOKUP;
    static final String CONTAINER_JNDI_RESOURCE_PATH_PREFIX = "java:comp/env/";
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        if (key == null) {
            return null;
        }
        final String jndiName = this.convertJndiName(key);
        try (final JndiManager jndiManager = JndiManager.getDefaultManager()) {
            return Objects.toString(jndiManager.lookup(jndiName), null);
        }
        catch (NamingException e) {
            JndiLookup.LOGGER.warn(JndiLookup.LOOKUP, "Error looking up JNDI resource [{}].", jndiName, e);
            return null;
        }
    }
    
    private String convertJndiName(final String jndiName) {
        if (!jndiName.startsWith("java:comp/env/") && jndiName.indexOf(58) == -1) {
            return "java:comp/env/" + jndiName;
        }
        return jndiName;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        LOOKUP = MarkerManager.getMarker("LOOKUP");
    }
}
