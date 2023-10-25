/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.lookup;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(name="jndi", category="Lookup")
public class JndiLookup
implements StrLookup {
    static final String CONTAINER_JNDI_RESOURCE_PATH_PREFIX = "java:comp/env/";

    @Override
    public String lookup(String key) {
        return this.lookup(null, key);
    }

    @Override
    public String lookup(LogEvent event, String key) {
        if (key == null) {
            return null;
        }
        try {
            InitialContext ctx = new InitialContext();
            return (String)ctx.lookup(this.convertJndiName(key));
        }
        catch (NamingException e) {
            return null;
        }
    }

    private String convertJndiName(String jndiName) {
        if (!jndiName.startsWith(CONTAINER_JNDI_RESOURCE_PATH_PREFIX) && jndiName.indexOf(58) == -1) {
            jndiName = CONTAINER_JNDI_RESOURCE_PATH_PREFIX + jndiName;
        }
        return jndiName;
    }
}

