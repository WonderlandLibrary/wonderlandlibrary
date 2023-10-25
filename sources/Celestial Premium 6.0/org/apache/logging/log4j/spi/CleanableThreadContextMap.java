/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.spi.ThreadContextMap2;

public interface CleanableThreadContextMap
extends ThreadContextMap2 {
    public void removeAll(Iterable<String> var1);
}

