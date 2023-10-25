/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.routing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.routing.PurgePolicy;
import org.apache.logging.log4j.core.appender.routing.RoutingAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import org.apache.logging.log4j.core.config.Scheduled;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name="IdlePurgePolicy", category="Core", printObject=true)
@Scheduled
public class IdlePurgePolicy
extends AbstractLifeCycle
implements PurgePolicy,
Runnable {
    private final long timeToLive;
    private final long checkInterval;
    private final ConcurrentMap<String, Long> appendersUsage = new ConcurrentHashMap<String, Long>();
    private RoutingAppender routingAppender;
    private final ConfigurationScheduler scheduler;
    private volatile ScheduledFuture<?> future;

    public IdlePurgePolicy(long timeToLive, long checkInterval, ConfigurationScheduler scheduler) {
        this.timeToLive = timeToLive;
        this.checkInterval = checkInterval;
        this.scheduler = scheduler;
    }

    @Override
    public void initialize(RoutingAppender routingAppender) {
        this.routingAppender = routingAppender;
    }

    @Override
    public boolean stop(long timeout, TimeUnit timeUnit) {
        this.setStopping();
        boolean stopped = this.stop(this.future);
        this.setStopped();
        return stopped;
    }

    @Override
    public void purge() {
        long createTime = System.currentTimeMillis() - this.timeToLive;
        for (Map.Entry entry : this.appendersUsage.entrySet()) {
            long entryValue = (Long)entry.getValue();
            if (entryValue >= createTime || !this.appendersUsage.remove(entry.getKey(), entryValue)) continue;
            LOGGER.debug("Removing appender {}", entry.getKey());
            this.routingAppender.deleteAppender((String)entry.getKey());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void update(String key, LogEvent event) {
        long now = System.currentTimeMillis();
        this.appendersUsage.put(key, now);
        if (this.future == null) {
            IdlePurgePolicy idlePurgePolicy = this;
            synchronized (idlePurgePolicy) {
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
        for (Map.Entry entry : this.appendersUsage.entrySet()) {
            if ((Long)entry.getValue() >= updateTime) continue;
            updateTime = (Long)entry.getValue();
        }
        if (updateTime < Long.MAX_VALUE) {
            long interval = this.timeToLive - (System.currentTimeMillis() - updateTime);
            this.future = this.scheduler.schedule(this, interval, TimeUnit.MILLISECONDS);
        } else {
            this.future = this.scheduler.schedule(this, this.checkInterval, TimeUnit.MILLISECONDS);
        }
    }

    @PluginFactory
    public static PurgePolicy createPurgePolicy(@PluginAttribute(value="timeToLive") String timeToLive, @PluginAttribute(value="checkInterval") String checkInterval, @PluginAttribute(value="timeUnit") String timeUnit, @PluginConfiguration Configuration configuration) {
        long ci;
        TimeUnit units;
        if (timeToLive == null) {
            LOGGER.error("A timeToLive value is required");
            return null;
        }
        if (timeUnit == null) {
            units = TimeUnit.MINUTES;
        } else {
            try {
                units = TimeUnit.valueOf(timeUnit.toUpperCase());
            }
            catch (Exception ex) {
                LOGGER.error("Invalid timeUnit value {}. timeUnit set to MINUTES", timeUnit, ex);
                units = TimeUnit.MINUTES;
            }
        }
        long ttl = units.toMillis(Long.parseLong(timeToLive));
        if (ttl < 0L) {
            LOGGER.error("timeToLive must be positive. timeToLive set to 0");
            ttl = 0L;
        }
        if (checkInterval == null) {
            ci = ttl;
        } else {
            ci = units.toMillis(Long.parseLong(checkInterval));
            if (ci < 0L) {
                LOGGER.error("checkInterval must be positive. checkInterval set equal to timeToLive = {}", ttl);
                ci = ttl;
            }
        }
        return new IdlePurgePolicy(ttl, ci, configuration.getScheduler());
    }

    public String toString() {
        return "timeToLive=" + this.timeToLive;
    }
}

