/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import java.util.Map;
import org.apache.logging.log4j.util.StringMap;

public interface ReadOnlyThreadContextMap {
    public void clear();

    public boolean containsKey(String var1);

    public String get(String var1);

    public Map<String, String> getCopy();

    public Map<String, String> getImmutableMapOrNull();

    public StringMap getReadOnlyContextData();

    public boolean isEmpty();
}

