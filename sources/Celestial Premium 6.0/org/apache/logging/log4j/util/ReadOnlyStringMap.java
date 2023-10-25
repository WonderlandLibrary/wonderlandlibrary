/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.io.Serializable;
import java.util.Map;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.TriConsumer;

public interface ReadOnlyStringMap
extends Serializable {
    public Map<String, String> toMap();

    public boolean containsKey(String var1);

    public <V> void forEach(BiConsumer<String, ? super V> var1);

    public <V, S> void forEach(TriConsumer<String, ? super V, S> var1, S var2);

    public <V> V getValue(String var1);

    public boolean isEmpty();

    public int size();
}

