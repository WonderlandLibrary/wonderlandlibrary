package org.apache.logging.log4j.core.appender.routing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import org.apache.logging.log4j.core.config.Scheduled;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "IdlePurgePolicy", category = "Core", printObject = true)
@Scheduled
public class IdlePurgePolicy extends AbstractLifeCycle implements PurgePolicy, Runnable {
  private final long timeToLive;
  
  private final long checkInterval;
  
  private final ConcurrentMap<String, Long> appendersUsage = new ConcurrentHashMap<>();
  
  private RoutingAppender routingAppender;
  
  private final ConfigurationScheduler scheduler;
  
  private volatile ScheduledFuture<?> future;
  
  public IdlePurgePolicy(long timeToLive, long checkInterval, ConfigurationScheduler scheduler) {
    this.timeToLive = timeToLive;
    this.checkInterval = checkInterval;
    this.scheduler = scheduler;
  }
  
  public void initialize(RoutingAppender routingAppender) {
    this.routingAppender = routingAppender;
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    boolean stopped = stop(this.future);
    setStopped();
    return stopped;
  }
  
  public void purge() {
    long createTime = System.currentTimeMillis() - this.timeToLive;
    for (Map.Entry<String, Long> entry : this.appendersUsage.entrySet()) {
      long entryValue = ((Long)entry.getValue()).longValue();
      if (entryValue < createTime && 
        this.appendersUsage.remove(entry.getKey(), Long.valueOf(entryValue))) {
        LOGGER.debug("Removing appender {}", entry.getKey());
        this.routingAppender.deleteAppender(entry.getKey());
      } 
    } 
  }
  
  public void update(String key, LogEvent event) {
    long now = System.currentTimeMillis();
    this.appendersUsage.put(key, Long.valueOf(now));
    if (this.future == null)
      synchronized (this) {
        if (this.future == null)
          scheduleNext(); 
      }  
  }
  
  public void run() {
    purge();
    scheduleNext();
  }
  
  private void scheduleNext() {
    long updateTime = Long.MAX_VALUE;
    for (Map.Entry<String, Long> entry : this.appendersUsage.entrySet()) {
      if (((Long)entry.getValue()).longValue() < updateTime)
        updateTime = ((Long)entry.getValue()).longValue(); 
    } 
    if (updateTime < Long.MAX_VALUE) {
      long interval = this.timeToLive - System.currentTimeMillis() - updateTime;
      this.future = this.scheduler.schedule(this, interval, TimeUnit.MILLISECONDS);
    } else {
      this.future = this.scheduler.schedule(this, this.checkInterval, TimeUnit.MILLISECONDS);
    } 
  }
  
  @PluginFactory
  public static PurgePolicy createPurgePolicy(@PluginAttribute("timeToLive") String timeToLive, @PluginAttribute("checkInterval") String checkInterval, @PluginAttribute("timeUnit") String timeUnit, @PluginConfiguration Configuration configuration) {
    TimeUnit units;
    long ci;
    if (timeToLive == null) {
      LOGGER.error("A timeToLive value is required");
      return null;
    } 
    if (timeUnit == null) {
      units = TimeUnit.MINUTES;
    } else {
      try {
        units = TimeUnit.valueOf(timeUnit.toUpperCase());
      } catch (Exception ex) {
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
        LOGGER.error("checkInterval must be positive. checkInterval set equal to timeToLive = {}", Long.valueOf(ttl));
        ci = ttl;
      } 
    } 
    return new IdlePurgePolicy(ttl, ci, configuration.getScheduler());
  }
  
  public String toString() {
    return "timeToLive=" + this.timeToLive;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\routing\IdlePurgePolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */