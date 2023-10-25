package org.apache.logging.log4j.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;

public class WatchManager extends AbstractLifeCycle {
  private final class ConfigurationMonitor {
    private final Watcher watcher;
    
    private volatile long lastModifiedMillis;
    
    public ConfigurationMonitor(long lastModifiedMillis, Watcher watcher) {
      this.watcher = watcher;
      this.lastModifiedMillis = lastModifiedMillis;
    }
    
    public Watcher getWatcher() {
      return this.watcher;
    }
    
    private void setLastModifiedMillis(long lastModifiedMillis) {
      this.lastModifiedMillis = lastModifiedMillis;
    }
    
    public String toString() {
      return "ConfigurationMonitor [watcher=" + this.watcher + ", lastModifiedMillis=" + this.lastModifiedMillis + "]";
    }
  }
  
  private static class LocalUUID {
    private static final long LOW_MASK = 4294967295L;
    
    private static final long MID_MASK = 281470681743360L;
    
    private static final long HIGH_MASK = 1152640029630136320L;
    
    private static final int NODE_SIZE = 8;
    
    private static final int SHIFT_2 = 16;
    
    private static final int SHIFT_4 = 32;
    
    private static final int SHIFT_6 = 48;
    
    private static final int HUNDRED_NANOS_PER_MILLI = 10000;
    
    private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
    
    private static final AtomicInteger COUNT = new AtomicInteger(0);
    
    private static final long TYPE1 = 4096L;
    
    private static final byte VARIANT = -128;
    
    private static final int SEQUENCE_MASK = 16383;
    
    public static UUID get() {
      long time = System.currentTimeMillis() * 10000L + 122192928000000000L + (COUNT.incrementAndGet() % 10000);
      long timeLow = (time & 0xFFFFFFFFL) << 32L;
      long timeMid = (time & 0xFFFF00000000L) >> 16L;
      long timeHi = (time & 0xFFF000000000000L) >> 48L;
      long most = timeLow | timeMid | 0x1000L | timeHi;
      return new UUID(most, COUNT.incrementAndGet());
    }
  }
  
  private final class WatchRunnable implements Runnable {
    private final String SIMPLE_NAME = WatchRunnable.class.getSimpleName();
    
    public void run() {
      WatchManager.logger.trace("{} run triggered.", this.SIMPLE_NAME);
      for (Map.Entry<Source, WatchManager.ConfigurationMonitor> entry : (Iterable<Map.Entry<Source, WatchManager.ConfigurationMonitor>>)WatchManager.this.watchers.entrySet()) {
        Source source = entry.getKey();
        WatchManager.ConfigurationMonitor monitor = entry.getValue();
        if (monitor.getWatcher().isModified()) {
          long lastModified = monitor.getWatcher().getLastModified();
          if (WatchManager.logger.isInfoEnabled())
            WatchManager.logger.info("Source '{}' was modified on {} ({}), previous modification was on {} ({})", source, WatchManager.this
                .millisToString(lastModified), Long.valueOf(lastModified), WatchManager.this.millisToString(monitor.lastModifiedMillis), 
                Long.valueOf(monitor.lastModifiedMillis)); 
          monitor.lastModifiedMillis = lastModified;
          monitor.getWatcher().modified();
        } 
      } 
      WatchManager.logger.trace("{} run ended.", this.SIMPLE_NAME);
    }
    
    private WatchRunnable() {}
  }
  
  private static Logger logger = (Logger)StatusLogger.getLogger();
  
  private final ConcurrentMap<Source, ConfigurationMonitor> watchers = new ConcurrentHashMap<>();
  
  private int intervalSeconds = 0;
  
  private ScheduledFuture<?> future;
  
  private final ConfigurationScheduler scheduler;
  
  private final List<WatchEventService> eventServiceList;
  
  private final UUID id = LocalUUID.get();
  
  public WatchManager(ConfigurationScheduler scheduler) {
    this.scheduler = Objects.<ConfigurationScheduler>requireNonNull(scheduler, "scheduler");
    this.eventServiceList = getEventServices();
  }
  
  public void checkFiles() {
    (new WatchRunnable()).run();
  }
  
  public Map<Source, Watcher> getConfigurationWatchers() {
    Map<Source, Watcher> map = new HashMap<>(this.watchers.size());
    for (Map.Entry<Source, ConfigurationMonitor> entry : this.watchers.entrySet())
      map.put(entry.getKey(), ((ConfigurationMonitor)entry.getValue()).getWatcher()); 
    return map;
  }
  
  private List<WatchEventService> getEventServices() {
    List<WatchEventService> list = new ArrayList<>();
    for (ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
      try {
        ServiceLoader<WatchEventService> serviceLoader = ServiceLoader.load(WatchEventService.class, classLoader);
        for (WatchEventService service : serviceLoader)
          list.add(service); 
      } catch (Throwable ex) {
        LOGGER.debug("Unable to retrieve WatchEventService from ClassLoader {}", classLoader, ex);
      } 
    } 
    return list;
  }
  
  public UUID getId() {
    return this.id;
  }
  
  public int getIntervalSeconds() {
    return this.intervalSeconds;
  }
  
  @Deprecated
  public Map<File, FileWatcher> getWatchers() {
    Map<File, FileWatcher> map = new HashMap<>(this.watchers.size());
    for (Map.Entry<Source, ConfigurationMonitor> entry : this.watchers.entrySet()) {
      if (((ConfigurationMonitor)entry.getValue()).getWatcher() instanceof org.apache.logging.log4j.core.config.ConfigurationFileWatcher) {
        map.put(((Source)entry.getKey()).getFile(), (FileWatcher)((ConfigurationMonitor)entry.getValue()).getWatcher());
        continue;
      } 
      map.put(((Source)entry.getKey()).getFile(), new WrappedFileWatcher((FileWatcher)((ConfigurationMonitor)entry.getValue()).getWatcher()));
    } 
    return map;
  }
  
  public boolean hasEventListeners() {
    return (this.eventServiceList.size() > 0);
  }
  
  private String millisToString(long millis) {
    return (new Date(millis)).toString();
  }
  
  public void reset() {
    logger.debug("Resetting {}", this);
    for (Source source : this.watchers.keySet())
      reset(source); 
  }
  
  public void reset(File file) {
    if (file == null)
      return; 
    Source source = new Source(file);
    reset(source);
  }
  
  public void reset(Source source) {
    if (source == null)
      return; 
    ConfigurationMonitor monitor = this.watchers.get(source);
    if (monitor != null) {
      Watcher watcher = monitor.getWatcher();
      if (watcher.isModified()) {
        long lastModifiedMillis = watcher.getLastModified();
        if (logger.isDebugEnabled())
          logger.debug("Resetting file monitor for '{}' from {} ({}) to {} ({})", source.getLocation(), 
              millisToString(monitor.lastModifiedMillis), Long.valueOf(monitor.lastModifiedMillis), 
              millisToString(lastModifiedMillis), Long.valueOf(lastModifiedMillis)); 
        monitor.setLastModifiedMillis(lastModifiedMillis);
      } 
    } 
  }
  
  public void setIntervalSeconds(int intervalSeconds) {
    if (!isStarted()) {
      if (this.intervalSeconds > 0 && intervalSeconds == 0) {
        this.scheduler.decrementScheduledItems();
      } else if (this.intervalSeconds == 0 && intervalSeconds > 0) {
        this.scheduler.incrementScheduledItems();
      } 
      this.intervalSeconds = intervalSeconds;
    } 
  }
  
  public void start() {
    super.start();
    if (this.intervalSeconds > 0)
      this
        .future = this.scheduler.scheduleWithFixedDelay(new WatchRunnable(), this.intervalSeconds, this.intervalSeconds, TimeUnit.SECONDS); 
    for (WatchEventService service : this.eventServiceList)
      service.subscribe(this); 
  }
  
  public boolean stop(long timeout, TimeUnit timeUnit) {
    setStopping();
    for (WatchEventService service : this.eventServiceList)
      service.unsubscribe(this); 
    boolean stopped = stop(this.future);
    setStopped();
    return stopped;
  }
  
  public String toString() {
    return "WatchManager [intervalSeconds=" + this.intervalSeconds + ", watchers=" + this.watchers + ", scheduler=" + this.scheduler + ", future=" + this.future + "]";
  }
  
  public void unwatch(Source source) {
    logger.debug("Unwatching configuration {}", source);
    this.watchers.remove(source);
  }
  
  public void unwatchFile(File file) {
    Source source = new Source(file);
    unwatch(source);
  }
  
  public void watch(Source source, Watcher watcher) {
    watcher.watching(source);
    long lastModified = watcher.getLastModified();
    if (logger.isDebugEnabled())
      logger.debug("Watching configuration '{}' for lastModified {} ({})", source, millisToString(lastModified), 
          Long.valueOf(lastModified)); 
    this.watchers.put(source, new ConfigurationMonitor(lastModified, watcher));
  }
  
  public void watchFile(File file, FileWatcher fileWatcher) {
    Watcher watcher;
    if (fileWatcher instanceof Watcher) {
      watcher = (Watcher)fileWatcher;
    } else {
      watcher = new WrappedFileWatcher(fileWatcher);
    } 
    Source source = new Source(file);
    watch(source, watcher);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\WatchManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */