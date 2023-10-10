// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Util
{
    private static final Logger LOG;
    
    private Util() {
    }
    
    public static void sleep(final long ms) {
        try {
            Util.LOG.trace("Sleeping for {} ms", (Object)ms);
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
            Util.LOG.warn("Interrupted while sleeping for {} ms: {}", (Object)ms, e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    public static boolean wildcardMatch(final String text, final String pattern) {
        if (pattern.length() > 0 && pattern.charAt(0) == '^') {
            return !wildcardMatch(text, pattern.substring(1));
        }
        return text.matches(pattern.replace("?", ".?").replace("*", ".*?"));
    }
    
    public static boolean isBlank(final String s) {
        return s == null || s.isEmpty();
    }
    
    public static boolean isBlankOrUnknown(final String s) {
        return isBlank(s) || "unknown".equals(s);
    }
    
    static {
        LOG = LoggerFactory.getLogger(Util.class);
    }
}
