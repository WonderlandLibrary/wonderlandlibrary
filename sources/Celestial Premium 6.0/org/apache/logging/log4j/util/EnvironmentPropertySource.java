/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.util.Map;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.LowLevelLogUtil;
import org.apache.logging.log4j.util.PropertySource;

public class EnvironmentPropertySource
implements PropertySource {
    private static final String PREFIX = "LOG4J_";
    private static final int DEFAULT_PRIORITY = -100;

    @Override
    public int getPriority() {
        return -100;
    }

    @Override
    public void forEach(BiConsumer<String, String> action) {
        Map<String, String> getenv;
        try {
            getenv = System.getenv();
        }
        catch (SecurityException e) {
            LowLevelLogUtil.logException("The system environment variables are not available to Log4j due to security restrictions: " + e, e);
            return;
        }
        for (Map.Entry<String, String> entry : getenv.entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith(PREFIX)) continue;
            action.accept(key.substring(PREFIX.length()), entry.getValue());
        }
    }

    @Override
    public CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
        StringBuilder sb = new StringBuilder("LOG4J");
        for (CharSequence charSequence : tokens) {
            sb.append('_');
            for (int i = 0; i < charSequence.length(); ++i) {
                sb.append(Character.toUpperCase(charSequence.charAt(i)));
            }
        }
        return sb.toString();
    }
}

