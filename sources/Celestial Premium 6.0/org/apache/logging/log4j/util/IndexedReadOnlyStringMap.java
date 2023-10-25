/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import org.apache.logging.log4j.util.ReadOnlyStringMap;

public interface IndexedReadOnlyStringMap
extends ReadOnlyStringMap {
    public String getKeyAt(int var1);

    public <V> V getValueAt(int var1);

    public int indexOfKey(String var1);
}

