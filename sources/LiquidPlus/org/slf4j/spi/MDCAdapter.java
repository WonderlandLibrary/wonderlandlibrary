/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j.spi;

import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface MDCAdapter {
    public void put(String var1, String var2);

    public String get(String var1);

    public void remove(String var1);

    public void clear();

    public Map<String, String> getCopyOfContextMap();

    public void setContextMap(Map<String, String> var1);
}

