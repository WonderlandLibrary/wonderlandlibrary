/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;

@Plugin(name="Async", category="Core", elementType="appender", printObject=true)
public final class AsyncAppender
extends AbstractAppender {
    private static final int DEFAULT_QUEUE_SIZE = 128;
    private static final String SHUTDOWN = "Shutdown";
    private final BlockingQueue<Serializable> queue;
    private final boolean blocking;
    private final Configuration config;
    private final AppenderRef[] appenderRefs;
    private final String errorRef;
    private final boolean includeLocation;
    private AppenderControl errorAppender;
    private AsyncThread thread;
    private static final AtomicLong threadSequence = new AtomicLong(1L);

    private AsyncAppender(String name, Filter filter, AppenderRef[] appenderRefs, String errorRef, int queueSize, boolean blocking, boolean ignoreExceptions, Configuration config, boolean includeLocation) {
        super(name, filter, null, ignoreExceptions);
        this.queue = new ArrayBlockingQueue<Serializable>(queueSize);
        this.blocking = blocking;
        this.config = config;
        this.appenderRefs = appenderRefs;
        this.errorRef = errorRef;
        this.includeLocation = includeLocation;
    }

    @Override
    public void start() {
        Map<String, Appender> map = this.config.getAppenders();
        ArrayList<AppenderControl> appenders = new ArrayList<AppenderControl>();
        for (AppenderRef appenderRef : this.appenderRefs) {
            if (map.containsKey(appenderRef.getRef())) {
                appenders.add(new AppenderControl(map.get(appenderRef.getRef()), appenderRef.getLevel(), appenderRef.getFilter()));
                continue;
            }
            LOGGER.error("No appender named {} was configured", appenderRef);
        }
        if (this.errorRef != null) {
            if (map.containsKey(this.errorRef)) {
                this.errorAppender = new AppenderControl(map.get(this.errorRef), null, null);
            } else {
                LOGGER.error("Unable to set up error Appender. No appender named {} was configured", this.errorRef);
            }
        }
        if (appenders.size() > 0) {
            this.thread = new AsyncThread(appenders, this.queue);
            this.thread.setName("AsyncAppender-" + this.getName());
        } else if (this.errorRef == null) {
            throw new ConfigurationException("No appenders are available for AsyncAppender " + this.getName());
        }
        this.thread.start();
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        this.thread.shutdown();
        try {
            this.thread.join();
        }
        catch (InterruptedException ex) {
            LOGGER.warn("Interrupted while stopping AsyncAppender {}", this.getName());
        }
    }

    @Override
    public void append(LogEvent event) {
        if (!this.isStarted()) {
            throw new IllegalStateException("AsyncAppender " + this.getName() + " is not active");
        }
        if (event instanceof Log4jLogEvent) {
            boolean appendSuccessful = false;
            if (this.blocking) {
                try {
                    this.queue.put(Log4jLogEvent.serialize((Log4jLogEvent)event, this.includeLocation));
                    appendSuccessful = true;
                }
                catch (InterruptedException e) {
                    LOGGER.warn("Interrupted while waiting for a free slot in the AsyncAppender LogEvent-queue {}", this.getName());
                }
            } else {
                appendSuccessful = this.queue.offer(Log4jLogEvent.serialize((Log4jLogEvent)event, this.includeLocation));
                if (!appendSuccessful) {
                    this.error("Appender " + this.getName() + " is unable to write primary appenders. queue is full");
                }
            }
            if (!appendSuccessful && this.errorAppender != null) {
                this.errorAppender.callAppender(event);
            }
        }
    }

    @PluginFactory
    public static AsyncAppender createAppender(@PluginElement(value="AppenderRef") AppenderRef[] appenderRefs, @PluginAttribute(value="errorRef") @PluginAliases(value={"error-ref"}) String errorRef, @PluginAttribute(value="blocking") String blocking, @PluginAttribute(value="bufferSize") String size, @PluginAttribute(value="name") String name, @PluginAttribute(value="includeLocation") String includeLocation, @PluginElement(value="Filter") Filter filter, @PluginConfiguration Configuration config, @PluginAttribute(value="ignoreExceptions") String ignore) {
        if (name == null) {
            LOGGER.error("No name provided for AsyncAppender");
            return null;
        }
        if (appenderRefs == null) {
            LOGGER.error("No appender references provided to AsyncAppender {}", name);
        }
        boolean isBlocking = Booleans.parseBoolean(blocking, true);
        int queueSize = AbstractAppender.parseInt(size, 128);
        boolean isIncludeLocation = Boolean.parseBoolean(includeLocation);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        return new AsyncAppender(name, filter, appenderRefs, errorRef, queueSize, isBlocking, ignoreExceptions, config, isIncludeLocation);
    }

    private class AsyncThread
    extends Thread {
        private volatile boolean shutdown = false;
        private final List<AppenderControl> appenders;
        private final BlockingQueue<Serializable> queue;

        public AsyncThread(List<AppenderControl> appenders, BlockingQueue<Serializable> queue) {
            this.appenders = appenders;
            this.queue = queue;
            this.setDaemon(true);
            this.setName("AsyncAppenderThread" + threadSequence.getAndIncrement());
        }

        @Override
        public void run() {
            Log4jLogEvent event;
            Serializable s;
            while (!this.shutdown) {
                block13: {
                    try {
                        s = this.queue.take();
                        if (s != null && s instanceof String && AsyncAppender.SHUTDOWN.equals(s.toString())) {
                            this.shutdown = true;
                        }
                        break block13;
                    }
                    catch (InterruptedException ex) {}
                    continue;
                }
                event = Log4jLogEvent.deserialize(s);
                event.setEndOfBatch(this.queue.isEmpty());
                boolean success = false;
                for (AppenderControl control : this.appenders) {
                    try {
                        control.callAppender(event);
                        success = true;
                    }
                    catch (Exception ex) {}
                }
                if (success || AsyncAppender.this.errorAppender == null) continue;
                try {
                    AsyncAppender.this.errorAppender.callAppender(event);
                }
                catch (Exception ex) {}
            }
            while (!this.queue.isEmpty()) {
                try {
                    s = this.queue.take();
                    if (!(s instanceof Log4jLogEvent)) continue;
                    event = Log4jLogEvent.deserialize(s);
                    event.setEndOfBatch(this.queue.isEmpty());
                    for (AppenderControl control : this.appenders) {
                        control.callAppender(event);
                    }
                }
                catch (InterruptedException interruptedException) {
                }
            }
        }

        public void shutdown() {
            this.shutdown = true;
            if (this.queue.isEmpty()) {
                this.queue.offer((Serializable)((Object)AsyncAppender.SHUTDOWN));
            }
        }
    }
}

