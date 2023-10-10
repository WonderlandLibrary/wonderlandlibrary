// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.routing;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.config.Scheduled;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.AbstractLifeCycle;

@Plugin(name = "IdlePurgePolicy", category = "Core", printObject = true)
@Scheduled
public class IdlePurgePolicy extends AbstractLifeCycle implements PurgePolicy, Runnable
{
    private final long timeToLive;
    private final long checkInterval;
    private final ConcurrentMap<String, Long> appendersUsage;
    private RoutingAppender routingAppender;
    private final ConfigurationScheduler scheduler;
    private volatile ScheduledFuture<?> future;
    
    public IdlePurgePolicy(final long timeToLive, final long checkInterval, final ConfigurationScheduler scheduler) {
        this.appendersUsage = new ConcurrentHashMap<String, Long>();
        this.timeToLive = timeToLive;
        this.checkInterval = checkInterval;
        this.scheduler = scheduler;
    }
    
    @Override
    public void initialize(final RoutingAppender routingAppender) {
        this.routingAppender = routingAppender;
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        final boolean stopped = this.stop(this.future);
        this.setStopped();
        return stopped;
    }
    
    @Override
    public void purge() {
        final long createTime = System.currentTimeMillis() - this.timeToLive;
        for (final Map.Entry<String, Long> entry : this.appendersUsage.entrySet()) {
            final long entryValue = entry.getValue();
            if (entryValue < createTime && this.appendersUsage.remove(entry.getKey(), entryValue)) {
                IdlePurgePolicy.LOGGER.debug("Removing appender {}", entry.getKey());
                this.routingAppender.deleteAppender(entry.getKey());
            }
        }
    }
    
    @Override
    public void update(final String key, final LogEvent event) {
        final long now = System.currentTimeMillis();
        this.appendersUsage.put(key, now);
        if (this.future == null) {
            synchronized (this) {
                if (this.future == null) {
                    this.scheduleNext();
                }
            }
        }
    }
    
    @Override
    public void run() {
        this.purge();
        this.scheduleNext();
    }
    
    private void scheduleNext() {
        long updateTime = Long.MAX_VALUE;
        for (final Map.Entry<String, Long> entry : this.appendersUsage.entrySet()) {
            if (entry.getValue() < updateTime) {
                updateTime = entry.getValue();
            }
        }
        if (updateTime < Long.MAX_VALUE) {
            final long interval = this.timeToLive - (System.currentTimeMillis() - updateTime);
            this.future = this.scheduler.schedule(this, interval, TimeUnit.MILLISECONDS);
        }
        else {
            this.future = this.scheduler.schedule(this, this.checkInterval, TimeUnit.MILLISECONDS);
        }
    }
    
    @PluginFactory
    public static PurgePolicy createPurgePolicy(@PluginAttribute("timeToLive") final String timeToLive, @PluginAttribute("checkInterval") final String checkInterval, @PluginAttribute("timeUnit") final String timeUnit, @PluginConfiguration final Configuration configuration) {
        if (timeToLive == null) {
            IdlePurgePolicy.LOGGER.error("A timeToLive value is required");
            return null;
        }
        TimeUnit units;
        if (timeUnit == null) {
            units = TimeUnit.MINUTES;
        }
        else {
            try {
                units = TimeUnit.valueOf(timeUnit.toUpperCase());
            }
            catch (Exception ex) {
                IdlePurgePolicy.LOGGER.error("Invalid timeUnit value {}. timeUnit set to MINUTES", timeUnit, ex);
                units = TimeUnit.MINUTES;
            }
        }
        long ttl = units.toMillis(Long.parseLong(timeToLive));
        if (ttl < 0L) {
            IdlePurgePolicy.LOGGER.error("timeToLive must be positive. timeToLive set to 0");
            ttl = 0L;
        }
        long ci;
        if (checkInterval == null) {
            ci = ttl;
        }
        else {
            ci = units.toMillis(Long.parseLong(checkInterval));
            if (ci < 0L) {
                IdlePurgePolicy.LOGGER.error("checkInterval must be positive. checkInterval set equal to timeToLive = {}", (Object)ttl);
                ci = ttl;
            }
        }
        return new IdlePurgePolicy(ttl, ci, configuration.getScheduler());
    }
    
    @Override
    public String toString() {
        return "timeToLive=" + this.timeToLive;
    }
}
