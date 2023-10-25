/*
 * Decompiled with CFR 0.150.
 */
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
import org.apache.logging.log4j.core.config.ConfigurationFileWatcher;
import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import org.apache.logging.log4j.core.util.FileWatcher;
import org.apache.logging.log4j.core.util.Source;
import org.apache.logging.log4j.core.util.WatchEventService;
import org.apache.logging.log4j.core.util.Watcher;
import org.apache.logging.log4j.core.util.WrappedFileWatcher;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;

public class WatchManager
extends AbstractLifeCycle {
    private static Logger logger = StatusLogger.getLogger();
    private final ConcurrentMap<Source, ConfigurationMonitor> watchers = new ConcurrentHashMap<Source, ConfigurationMonitor>();
    private int intervalSeconds = 0;
    private ScheduledFuture<?> future;
    private final ConfigurationScheduler scheduler;
    private final List<WatchEventService> eventServiceList;
    private final UUID id = LocalUUID.get();

    public WatchManager(ConfigurationScheduler scheduler) {
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler");
        this.eventServiceList = this.getEventServices();
    }

    public void checkFiles() {
        new WatchRunnable().run();
    }

    public Map<Source, Watcher> getConfigurationWatchers() {
        HashMap<Source, Watcher> map = new HashMap<Source, Watcher>(this.watchers.size());
        for (Map.Entry entry : this.watchers.entrySet()) {
            map.put((Source)entry.getKey(), ((ConfigurationMonitor)entry.getValue()).getWatcher());
        }
        return map;
    }

    private List<WatchEventService> getEventServices() {
        ArrayList<WatchEventService> list = new ArrayList<WatchEventService>();
        for (ClassLoader classLoader : LoaderUtil.getClassLoaders()) {
            try {
                ServiceLoader<WatchEventService> serviceLoader = ServiceLoader.load(WatchEventService.class, classLoader);
                for (WatchEventService service : serviceLoader) {
                    list.add(service);
                }
            }
            catch (Throwable ex) {
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
        HashMap<File, FileWatcher> map = new HashMap<File, FileWatcher>(this.watchers.size());
        for (Map.Entry entry : this.watchers.entrySet()) {
            if (((ConfigurationMonitor)entry.getValue()).getWatcher() instanceof ConfigurationFileWatcher) {
                map.put(((Source)entry.getKey()).getFile(), (FileWatcher)((Object)((ConfigurationMonitor)entry.getValue()).getWatcher()));
                continue;
            }
            map.put(((Source)entry.getKey()).getFile(), new WrappedFileWatcher((FileWatcher)((Object)((ConfigurationMonitor)entry.getValue()).getWatcher())));
        }
        return map;
    }

    public boolean hasEventListeners() {
        return this.eventServiceList.size() > 0;
    }

    private String millisToString(long millis) {
        return new Date(millis).toString();
    }

    public void reset() {
        logger.debug("Resetting {}", this);
        for (Source source : this.watchers.keySet()) {
            this.reset(source);
        }
    }

    public void reset(File file) {
        if (file == null) {
            return;
        }
        Source source = new Source(file);
        this.reset(source);
    }

    public void reset(Source source) {
        Watcher watcher;
        if (source == null) {
            return;
        }
        ConfigurationMonitor monitor = (ConfigurationMonitor)this.watchers.get(source);
        if (monitor != null && (watcher = monitor.getWatcher()).isModified()) {
            long lastModifiedMillis = watcher.getLastModified();
            if (logger.isDebugEnabled()) {
                logger.debug("Resetting file monitor for '{}' from {} ({}) to {} ({})", source.getLocation(), this.millisToString(monitor.lastModifiedMillis), monitor.lastModifiedMillis, this.millisToString(lastModifiedMillis), lastModifiedMillis);
            }
            monitor.setLastModifiedMillis(lastModifiedMillis);
        }
    }

    public void setIntervalSeconds(int intervalSeconds) {
        if (!this.isStarted()) {
            if (this.intervalSeconds > 0 && intervalSeconds == 0) {
                this.scheduler.decrementScheduledItems();
            } else if (this.intervalSeconds == 0 && intervalSeconds > 0) {
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
        for (WatchEventService service : this.eventServiceList) {
            service.subscribe(this);
        }
    }

    @Override
    public boolean stop(long timeout, TimeUnit timeUnit) {
        this.setStopping();
        for (WatchEventService service : this.eventServiceList) {
            service.unsubscribe(this);
        }
        boolean stopped = this.stop(this.future);
        this.setStopped();
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
        this.unwatch(source);
    }

    public void watch(Source source, Watcher watcher) {
        watcher.watching(source);
        long lastModified = watcher.getLastModified();
        if (logger.isDebugEnabled()) {
            logger.debug("Watching configuration '{}' for lastModified {} ({})", source, this.millisToString(lastModified), lastModified);
        }
        this.watchers.put(source, new ConfigurationMonitor(lastModified, watcher));
    }

    public void watchFile(File file, FileWatcher fileWatcher) {
        Watcher watcher = fileWatcher instanceof Watcher ? (Watcher)((Object)fileWatcher) : new WrappedFileWatcher(fileWatcher);
        Source source = new Source(file);
        this.watch(source, watcher);
    }

    private final class WatchRunnable
    implements Runnable {
        private final String SIMPLE_NAME = WatchRunnable.class.getSimpleName();

        private WatchRunnable() {
        }

        @Override
        public void run() {
            logger.trace("{} run triggered.", this.SIMPLE_NAME);
            for (Map.Entry entry : WatchManager.this.watchers.entrySet()) {
                Source source = (Source)entry.getKey();
                ConfigurationMonitor monitor = (ConfigurationMonitor)entry.getValue();
                if (!monitor.getWatcher().isModified()) continue;
                long lastModified = monitor.getWatcher().getLastModified();
                if (logger.isInfoEnabled()) {
                    logger.info("Source '{}' was modified on {} ({}), previous modification was on {} ({})", source, WatchManager.this.millisToString(lastModified), lastModified, WatchManager.this.millisToString(monitor.lastModifiedMillis), monitor.lastModifiedMillis);
                }
                monitor.lastModifiedMillis = lastModified;
                monitor.getWatcher().modified();
            }
            logger.trace("{} run ended.", this.SIMPLE_NAME);
        }
    }

    private static class LocalUUID {
        private static final long LOW_MASK = 0xFFFFFFFFL;
        private static final long MID_MASK = 0xFFFF00000000L;
        private static final long HIGH_MASK = 0xFFF000000000000L;
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

        private LocalUUID() {
        }

        public static UUID get() {
            long time = System.currentTimeMillis() * 10000L + 122192928000000000L + (long)(COUNT.incrementAndGet() % 10000);
            long timeLow = (time & 0xFFFFFFFFL) << 32;
            long timeMid = (time & 0xFFFF00000000L) >> 16;
            long timeHi = (time & 0xFFF000000000000L) >> 48;
            long most = timeLow | timeMid | 0x1000L | timeHi;
            return new UUID(most, COUNT.incrementAndGet());
        }
    }

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
}

