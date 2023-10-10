// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import org.apache.logging.log4j.core.config.ConfigurationFileWatcher;
import java.io.File;
import java.util.ServiceLoader;
import org.apache.logging.log4j.util.LoaderUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.List;
import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.AbstractLifeCycle;

public class WatchManager extends AbstractLifeCycle
{
    private static Logger logger;
    private final ConcurrentMap<Source, ConfigurationMonitor> watchers;
    private int intervalSeconds;
    private ScheduledFuture<?> future;
    private final ConfigurationScheduler scheduler;
    private final List<WatchEventService> eventServiceList;
    private final UUID id;
    
    public WatchManager(final ConfigurationScheduler scheduler) {
        this.watchers = new ConcurrentHashMap<Source, ConfigurationMonitor>();
        this.intervalSeconds = 0;
        this.id = LocalUUID.get();
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler");
        this.eventServiceList = this.getEventServices();
    }
    
    public void checkFiles() {
        new WatchRunnable().run();
    }
    
    public Map<Source, Watcher> getConfigurationWatchers() {
        final Map<Source, Watcher> map = new HashMap<Source, Watcher>(this.watchers.size());
        for (final Map.Entry<Source, ConfigurationMonitor> entry : this.watchers.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getWatcher());
        }
        return map;
    }
    
    private List<WatchEventService> getEventServices() {
        final List<WatchEventService> list = new ArrayList<WatchEventService>();
        for (final ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
            try {
                final ServiceLoader<WatchEventService> serviceLoader = ServiceLoader.load(WatchEventService.class, classLoader);
                for (final WatchEventService service : serviceLoader) {
                    list.add(service);
                }
            }
            catch (Throwable ex) {
                WatchManager.LOGGER.debug("Unable to retrieve WatchEventService from ClassLoader {}", classLoader, ex);
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
        final Map<File, FileWatcher> map = new HashMap<File, FileWatcher>(this.watchers.size());
        for (final Map.Entry<Source, ConfigurationMonitor> entry : this.watchers.entrySet()) {
            if (entry.getValue().getWatcher() instanceof ConfigurationFileWatcher) {
                map.put(entry.getKey().getFile(), (FileWatcher)entry.getValue().getWatcher());
            }
            else {
                map.put(entry.getKey().getFile(), new WrappedFileWatcher((FileWatcher)entry.getValue().getWatcher()));
            }
        }
        return map;
    }
    
    public boolean hasEventListeners() {
        return this.eventServiceList.size() > 0;
    }
    
    private String millisToString(final long millis) {
        return new Date(millis).toString();
    }
    
    public void reset() {
        WatchManager.logger.debug("Resetting {}", this);
        for (final Source source : this.watchers.keySet()) {
            this.reset(source);
        }
    }
    
    public void reset(final File file) {
        if (file == null) {
            return;
        }
        final Source source = new Source(file);
        this.reset(source);
    }
    
    public void reset(final Source source) {
        if (source == null) {
            return;
        }
        final ConfigurationMonitor monitor = this.watchers.get(source);
        if (monitor != null) {
            final Watcher watcher = monitor.getWatcher();
            if (watcher.isModified()) {
                final long lastModifiedMillis = watcher.getLastModified();
                if (WatchManager.logger.isDebugEnabled()) {
                    WatchManager.logger.debug("Resetting file monitor for '{}' from {} ({}) to {} ({})", source.getLocation(), this.millisToString(monitor.lastModifiedMillis), monitor.lastModifiedMillis, this.millisToString(lastModifiedMillis), lastModifiedMillis);
                }
                monitor.setLastModifiedMillis(lastModifiedMillis);
            }
        }
    }
    
    public void setIntervalSeconds(final int intervalSeconds) {
        if (!this.isStarted()) {
            if (this.intervalSeconds > 0 && intervalSeconds == 0) {
                this.scheduler.decrementScheduledItems();
            }
            else if (this.intervalSeconds == 0 && intervalSeconds > 0) {
                this.scheduler.incrementScheduledItems();
            }
            this.intervalSeconds = intervalSeconds;
        }
    }
    
    @Override
    public void start() {
        super.start();
        if (this.intervalSeconds > 0) {
            this.future = this.scheduler.scheduleWithFixedDelay(new WatchRunnable(), this.intervalSeconds, this.intervalSeconds, TimeUnit.SECONDS);
        }
        for (final WatchEventService service : this.eventServiceList) {
            service.subscribe(this);
        }
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        this.setStopping();
        for (final WatchEventService service : this.eventServiceList) {
            service.unsubscribe(this);
        }
        final boolean stopped = this.stop(this.future);
        this.setStopped();
        return stopped;
    }
    
    @Override
    public String toString() {
        return "WatchManager [intervalSeconds=" + this.intervalSeconds + ", watchers=" + this.watchers + ", scheduler=" + this.scheduler + ", future=" + this.future + "]";
    }
    
    public void unwatch(final Source source) {
        WatchManager.logger.debug("Unwatching configuration {}", source);
        this.watchers.remove(source);
    }
    
    public void unwatchFile(final File file) {
        final Source source = new Source(file);
        this.unwatch(source);
    }
    
    public void watch(final Source source, final Watcher watcher) {
        watcher.watching(source);
        final long lastModified = watcher.getLastModified();
        if (WatchManager.logger.isDebugEnabled()) {
            WatchManager.logger.debug("Watching configuration '{}' for lastModified {} ({})", source, this.millisToString(lastModified), lastModified);
        }
        this.watchers.put(source, new ConfigurationMonitor(lastModified, watcher));
    }
    
    public void watchFile(final File file, final FileWatcher fileWatcher) {
        Watcher watcher;
        if (fileWatcher instanceof Watcher) {
            watcher = (Watcher)fileWatcher;
        }
        else {
            watcher = new WrappedFileWatcher(fileWatcher);
        }
        final Source source = new Source(file);
        this.watch(source, watcher);
    }
    
    static {
        WatchManager.logger = StatusLogger.getLogger();
    }
    
    private final class ConfigurationMonitor
    {
        private final Watcher watcher;
        private volatile long lastModifiedMillis;
        
        public ConfigurationMonitor(final long lastModifiedMillis, final Watcher watcher) {
            this.watcher = watcher;
            this.lastModifiedMillis = lastModifiedMillis;
        }
        
        public Watcher getWatcher() {
            return this.watcher;
        }
        
        private void setLastModifiedMillis(final long lastModifiedMillis) {
            this.lastModifiedMillis = lastModifiedMillis;
        }
        
        @Override
        public String toString() {
            return "ConfigurationMonitor [watcher=" + this.watcher + ", lastModifiedMillis=" + this.lastModifiedMillis + "]";
        }
    }
    
    private static class LocalUUID
    {
        private static final long LOW_MASK = 4294967295L;
        private static final long MID_MASK = 281470681743360L;
        private static final long HIGH_MASK = 1152640029630136320L;
        private static final int NODE_SIZE = 8;
        private static final int SHIFT_2 = 16;
        private static final int SHIFT_4 = 32;
        private static final int SHIFT_6 = 48;
        private static final int HUNDRED_NANOS_PER_MILLI = 10000;
        private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
        private static final AtomicInteger COUNT;
        private static final long TYPE1 = 4096L;
        private static final byte VARIANT = Byte.MIN_VALUE;
        private static final int SEQUENCE_MASK = 16383;
        
        public static UUID get() {
            final long time = System.currentTimeMillis() * 10000L + 122192928000000000L + LocalUUID.COUNT.incrementAndGet() % 10000;
            final long timeLow = (time & 0xFFFFFFFFL) << 32;
            final long timeMid = (time & 0xFFFF00000000L) >> 16;
            final long timeHi = (time & 0xFFF000000000000L) >> 48;
            final long most = timeLow | timeMid | 0x1000L | timeHi;
            return new UUID(most, LocalUUID.COUNT.incrementAndGet());
        }
        
        static {
            COUNT = new AtomicInteger(0);
        }
    }
    
    private final class WatchRunnable implements Runnable
    {
        private final String SIMPLE_NAME;
        
        private WatchRunnable() {
            this.SIMPLE_NAME = WatchRunnable.class.getSimpleName();
        }
        
        @Override
        public void run() {
            WatchManager.logger.trace("{} run triggered.", this.SIMPLE_NAME);
            for (final Map.Entry<Source, ConfigurationMonitor> entry : WatchManager.this.watchers.entrySet()) {
                final Source source = entry.getKey();
                final ConfigurationMonitor monitor = entry.getValue();
                if (monitor.getWatcher().isModified()) {
                    final long lastModified = monitor.getWatcher().getLastModified();
                    if (WatchManager.logger.isInfoEnabled()) {
                        WatchManager.logger.info("Source '{}' was modified on {} ({}), previous modification was on {} ({})", source, WatchManager.this.millisToString(lastModified), lastModified, WatchManager.this.millisToString(monitor.lastModifiedMillis), monitor.lastModifiedMillis);
                    }
                    monitor.lastModifiedMillis = lastModified;
                    monitor.getWatcher().modified();
                }
            }
            WatchManager.logger.trace("{} run ended.", this.SIMPLE_NAME);
        }
    }
}
