// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.TimeUnit;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.SleepingWaitStrategy;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.util.PropertiesUtil;
import com.lmax.disruptor.WaitStrategy;
import org.apache.logging.log4j.Logger;

class DefaultAsyncWaitStrategyFactory implements AsyncWaitStrategyFactory
{
    static final String DEFAULT_WAIT_STRATEGY_CLASSNAME;
    private static final Logger LOGGER;
    private final String propertyName;
    
    public DefaultAsyncWaitStrategyFactory(final String propertyName) {
        this.propertyName = propertyName;
    }
    
    @Override
    public WaitStrategy createWaitStrategy() {
        final String strategy = PropertiesUtil.getProperties().getStringProperty(this.propertyName, "TIMEOUT");
        DefaultAsyncWaitStrategyFactory.LOGGER.trace("DefaultAsyncWaitStrategyFactory property {}={}", this.propertyName, strategy);
        final String rootUpperCase;
        final String strategyUp = rootUpperCase = Strings.toRootUpperCase(strategy);
        switch (rootUpperCase) {
            case "SLEEP": {
                final long sleepTimeNs = parseAdditionalLongProperty(this.propertyName, "SleepTimeNs", 100L);
                final String key = getFullPropertyKey(this.propertyName, "Retries");
                final int retries = PropertiesUtil.getProperties().getIntegerProperty(key, 200);
                DefaultAsyncWaitStrategyFactory.LOGGER.trace("DefaultAsyncWaitStrategyFactory creating SleepingWaitStrategy(retries={}, sleepTimeNs={})", (Object)retries, sleepTimeNs);
                return (WaitStrategy)new SleepingWaitStrategy(retries, sleepTimeNs);
            }
            case "YIELD": {
                DefaultAsyncWaitStrategyFactory.LOGGER.trace("DefaultAsyncWaitStrategyFactory creating YieldingWaitStrategy");
                return (WaitStrategy)new YieldingWaitStrategy();
            }
            case "BLOCK": {
                DefaultAsyncWaitStrategyFactory.LOGGER.trace("DefaultAsyncWaitStrategyFactory creating BlockingWaitStrategy");
                return (WaitStrategy)new BlockingWaitStrategy();
            }
            case "BUSYSPIN": {
                DefaultAsyncWaitStrategyFactory.LOGGER.trace("DefaultAsyncWaitStrategyFactory creating BusySpinWaitStrategy");
                return (WaitStrategy)new BusySpinWaitStrategy();
            }
            case "TIMEOUT": {
                return createDefaultWaitStrategy(this.propertyName);
            }
            default: {
                return createDefaultWaitStrategy(this.propertyName);
            }
        }
    }
    
    static WaitStrategy createDefaultWaitStrategy(final String propertyName) {
        final long timeoutMillis = parseAdditionalLongProperty(propertyName, "Timeout", 10L);
        DefaultAsyncWaitStrategyFactory.LOGGER.trace("DefaultAsyncWaitStrategyFactory creating TimeoutBlockingWaitStrategy(timeout={}, unit=MILLIS)", (Object)timeoutMillis);
        return (WaitStrategy)new TimeoutBlockingWaitStrategy(timeoutMillis, TimeUnit.MILLISECONDS);
    }
    
    private static String getFullPropertyKey(final String strategyKey, final String additionalKey) {
        if (strategyKey.startsWith("AsyncLogger.")) {
            return "AsyncLogger." + additionalKey;
        }
        if (strategyKey.startsWith("AsyncLoggerConfig.")) {
            return "AsyncLoggerConfig." + additionalKey;
        }
        return strategyKey + additionalKey;
    }
    
    private static long parseAdditionalLongProperty(final String propertyName, final String additionalKey, final long defaultValue) {
        final String key = getFullPropertyKey(propertyName, additionalKey);
        return PropertiesUtil.getProperties().getLongProperty(key, defaultValue);
    }
    
    static {
        DEFAULT_WAIT_STRATEGY_CLASSNAME = TimeoutBlockingWaitStrategy.class.getName();
        LOGGER = StatusLogger.getLogger();
    }
}
