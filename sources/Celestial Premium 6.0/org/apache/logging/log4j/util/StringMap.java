/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import org.apache.logging.log4j.util.ReadOnlyStringMap;

public interface StringMap
extends ReadOnlyStringMap {
    public void clear();

    public boolean equals(Object var1);

    public void freeze();

    public int hashCode();

    public boolean isFrozen();

    public void putAll(ReadOnlyStringMap var1);

    public void putValue(String var1, Object var2);

    public void remove(String var1);
}

