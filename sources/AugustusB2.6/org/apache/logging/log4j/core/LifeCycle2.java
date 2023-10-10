// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import java.util.concurrent.TimeUnit;

public interface LifeCycle2 extends LifeCycle
{
    boolean stop(final long timeout, final TimeUnit timeUnit);
}
