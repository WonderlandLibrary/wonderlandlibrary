/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.time;

import org.apache.logging.log4j.util.StringBuilderFormattable;

public interface Instant
extends StringBuilderFormattable {
    public long getEpochSecond();

    public int getNanoOfSecond();

    public long getEpochMillisecond();

    public int getNanoOfMillisecond();
}

