/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.status.StatusData;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class StatusLogger
extends AbstractLogger {
    public static final String MAX_STATUS_ENTRIES = "log4j2.status.entries";
    private static final String NOT_AVAIL = "?";
    private static final PropertiesUtil PROPS = new PropertiesUtil("log4j2.StatusLogger.properties");
    private static final int MAX_ENTRIES = PROPS.getIntegerProperty("log4j2.status.entries", 200);
    private static final String DEFAULT_STATUS_LEVEL = PROPS.getStringProperty("log4j2.StatusLogger.level");
    private static final StatusLogger STATUS_LOGGER = new StatusLogger();
    private final SimpleLogger logger;
    private final CopyOnWriteArrayList<StatusListener> listeners = new CopyOnWriteArrayList();
    private final ReentrantReadWriteLock listenersLock = new ReentrantReadWriteLock();
    private final Queue<StatusData> messages = new BoundedQueue<StatusData>(MAX_ENTRIES);
    private final ReentrantLock msgLock = new ReentrantLock();
    private int listenersLevel;

    private StatusLogger() {
        this.logger = new SimpleLogger("StatusLogger", Level.ERROR, false, true, false, false, "", null, PROPS, System.err);
        this.listenersLevel = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
    }

    public static StatusLogger getLogger() {
        return STATUS_LOGGER;
    }

    public Level getLevel() {
        return this.logger.getLevel();
    }

    public void setLevel(Level level) {
        this.logger.setLevel(level);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void registerListener(StatusListener listener) {
        this.listenersLock.writeLock().lock();
        try {
            this.listeners.add(listener);
            Level lvl = listener.getStatusLevel();
            if (this.listenersLevel < lvl.intLevel()) {
                this.listenersLevel = lvl.intLevel();
            }
        }
        finally {
            this.listenersLock.writeLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeListener(StatusListener listener) {
        this.listenersLock.writeLock().lock();
        try {
            this.listeners.remove(listener);
            int lowest = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
            for (StatusListener l : this.listeners) {
                int level = l.getStatusLevel().intLevel();
                if (lowest >= level) continue;
                lowest = level;
            }
            this.listenersLevel = lowest;
        }
        finally {
            this.listenersLock.writeLock().unlock();
        }
    }

    public Iterator<StatusListener> getListeners() {
        return this.listeners.iterator();
    }

    public void reset() {
        this.listeners.clear();
        this.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<StatusData> getStatusData() {
        this.msgLock.lock();
        try {
            ArrayList<StatusData> arrayList = new ArrayList<StatusData>(this.messages);
            return arrayList;
        }
        finally {
            this.msgLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void clear() {
        this.msgLock.lock();
        try {
            this.messages.clear();
        }
        finally {
            this.msgLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void log(Marker marker, String fqcn, Level level, Message msg, Throwable t) {
        StackTraceElement element = null;
        if (fqcn != null) {
            element = this.getStackTraceElement(fqcn, Thread.currentThread().getStackTrace());
        }
        StatusData data = new StatusData(element, level, msg, t);
        this.msgLock.lock();
        try {
            this.messages.add(data);
        }
        finally {
            this.msgLock.unlock();
        }
        if (this.listeners.size() > 0) {
            for (StatusListener listener : this.listeners) {
                if (!data.getLevel().isAtLeastAsSpecificAs(listener.getStatusLevel())) continue;
                listener.log(data);
            }
        } else {
            this.logger.log(marker, fqcn, level, msg, t);
        }
    }

    private StackTraceElement getStackTraceElement(String fqcn, StackTraceElement[] stackTrace) {
        if (fqcn == null) {
            return null;
        }
        boolean next = false;
        for (StackTraceElement element : stackTrace) {
            if (next) {
                return element;
            }
            String className = element.getClassName();
            if (fqcn.equals(className)) {
                next = true;
                continue;
            }
            if (NOT_AVAIL.equals(className)) break;
        }
        return null;
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String data) {
        return this.isEnabled(level, marker);
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String data, Throwable t) {
        return this.isEnabled(level, marker);
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, String data, Object ... p1) {
        return this.isEnabled(level, marker);
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, Object data, Throwable t) {
        return this.isEnabled(level, marker);
    }

    @Override
    protected boolean isEnabled(Level level, Marker marker, Message data, Throwable t) {
        return this.isEnabled(level, marker);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker) {
        if (this.listeners.size() > 0) {
            return this.listenersLevel >= level.intLevel();
        }
        switch (level) {
            case FATAL: {
                return this.logger.isFatalEnabled(marker);
            }
            case TRACE: {
                return this.logger.isTraceEnabled(marker);
            }
            case DEBUG: {
                return this.logger.isDebugEnabled(marker);
            }
            case INFO: {
                return this.logger.isInfoEnabled(marker);
            }
            case WARN: {
                return this.logger.isWarnEnabled(marker);
            }
            case ERROR: {
                return this.logger.isErrorEnabled(marker);
            }
        }
        return false;
    }

    private class BoundedQueue<E>
    extends ConcurrentLinkedQueue<E> {
        private static final long serialVersionUID = -3945953719763255337L;
        private final int size;

        public BoundedQueue(int size) {
            this.size = size;
        }

        @Override
        public boolean add(E object) {
            while (StatusLogger.this.messages.size() > this.size) {
                StatusLogger.this.messages.poll();
            }
            return super.add(object);
        }
    }
}

