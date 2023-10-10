// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.time;

import org.apache.logging.log4j.core.util.Clock;

public interface PreciseClock extends Clock
{
    void init(final MutableInstant mutableInstant);
}
