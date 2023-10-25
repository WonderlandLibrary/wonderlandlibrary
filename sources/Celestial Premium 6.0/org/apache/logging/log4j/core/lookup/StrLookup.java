/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;

public interface StrLookup {
    public String lookup(String var1);

    public String lookup(LogEvent var1, String var2);
}

