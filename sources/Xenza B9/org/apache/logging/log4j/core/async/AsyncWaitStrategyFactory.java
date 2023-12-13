// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.WaitStrategy;

public interface AsyncWaitStrategyFactory
{
    WaitStrategy createWaitStrategy();
}
