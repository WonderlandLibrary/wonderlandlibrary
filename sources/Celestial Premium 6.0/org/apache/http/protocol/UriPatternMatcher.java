/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.protocol;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@ThreadSafe
public class UriPatternMatcher<T> {
    @GuardedBy(value="this")
    private final Map<String, T> map = new HashMap<String, T>();

    public synchronized void register(String pattern, T obj) {
        Args.notNull(pattern, "URI request pattern");
        this.map.put(pattern, obj);
    }

    public synchronized void unregister(String pattern) {
        if (pattern == null) {
            return;
        }
        this.map.remove(pattern);
    }

    @Deprecated
    public synchronized void setHandlers(Map<String, T> map) {
        Args.notNull(map, "Map of handlers");
        this.map.clear();
        this.map.putAll(map);
    }

    @Deprecated
    public synchronized void setObjects(Map<String, T> map) {
        Args.notNull(map, "Map of handlers");
        this.map.clear();
        this.map.putAll(map);
    }

    @Deprecated
    public synchronized Map<String, T> getObjects() {
        return this.map;
    }

    public synchronized T lookup(String path) {
        Args.notNull(path, "Request path");
        T obj = this.map.get(path);
        if (obj == null) {
            String bestMatch = null;
            for (String pattern : this.map.keySet()) {
                if (!this.matchUriRequestPattern(pattern, path) || bestMatch != null && bestMatch.length() >= pattern.length() && (bestMatch.length() != pattern.length() || !pattern.endsWith("*"))) continue;
                obj = this.map.get(pattern);
                bestMatch = pattern;
            }
        }
        return obj;
    }

    protected boolean matchUriRequestPattern(String pattern, String path) {
        if (pattern.equals("*")) {
            return true;
        }
        return pattern.endsWith("*") && path.startsWith(pattern.substring(0, pattern.length() - 1)) || pattern.startsWith("*") && path.endsWith(pattern.substring(1, pattern.length()));
    }

    public String toString() {
        return this.map.toString();
    }
}

