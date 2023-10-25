/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.time;

import org.apache.logging.log4j.core.time.MutableInstant;
import org.apache.logging.log4j.core.util.Clock;

public interface PreciseClock
extends Clock {
    public void init(MutableInstant var1);
}

