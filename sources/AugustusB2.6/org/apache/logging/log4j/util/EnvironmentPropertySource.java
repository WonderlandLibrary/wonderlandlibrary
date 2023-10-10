// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.util.Iterator;
import java.util.Map;

public class EnvironmentPropertySource implements PropertySource
{
    private static final String PREFIX = "LOG4J_";
    private static final int DEFAULT_PRIORITY = -100;
    
    @Override
    public int getPriority() {
        return -100;
    }
    
    @Override
    public void forEach(final BiConsumer<String, String> action) {
        Map<String, String> getenv;
        try {
            getenv = System.getenv();
        }
        catch (SecurityException e) {
            LowLevelLogUtil.logException("The system environment variables are not available to Log4j due to security restrictions: " + e, e);
            return;
        }
        for (final Map.Entry<String, String> entry : getenv.entrySet()) {
            final String key = entry.getKey();
            if (key.startsWith("LOG4J_")) {
                action.accept(key.substring("LOG4J_".length()), entry.getValue());
            }
        }
    }
    
    @Override
    public CharSequence getNormalForm(final Iterable<? extends CharSequence> tokens) {
        final StringBuilder sb = new StringBuilder("LOG4J");
        for (final CharSequence token : tokens) {
            sb.append('_');
            for (int i = 0; i < token.length(); ++i) {
                sb.append(Character.toUpperCase(token.charAt(i)));
            }
        }
        return sb.toString();
    }
}
