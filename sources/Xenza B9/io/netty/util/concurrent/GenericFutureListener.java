// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import java.util.EventListener;

public interface GenericFutureListener<F extends Future<?>> extends EventListener
{
    void operationComplete(final F p0) throws Exception;
}
