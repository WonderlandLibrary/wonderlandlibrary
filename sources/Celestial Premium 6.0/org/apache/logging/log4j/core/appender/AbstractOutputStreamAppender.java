/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.OutputStreamManager;

public abstract class AbstractOutputStreamAppender
extends AbstractAppender {
    protected final boolean immediateFlush;
    private volatile OutputStreamManager manager;
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = this.rwLock.readLock();
    private final Lock writeLock = this.rwLock.writeLock();

    protected AbstractOutputStreamAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, boolean immediateFlush, OutputStreamManager manager) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
        this.immediateFlush = immediateFlush;
    }

    protected OutputStreamManager getManager() {
        return this.manager;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void replaceManager(OutputStreamManager newManager) {
        this.writeLock.lock();
        try {
            OutputStreamManager old = this.manager;
            this.manager = newManager;
            old.release();
        }
        finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public void start() {
        if (this.getLayout() == null) {
            LOGGER.error("No layout set for the appender named [" + this.getName() + "].");
        }
        if (this.manager == null) {
            LOGGER.error("No OutputStreamManager set for the appender named [" + this.getName() + "].");
        }
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        this.manager.release();
    }

    @Override
    public void append(LogEvent event) {
        this.readLock.lock();
        try {
            byte[] bytes = this.getLayout().toByteArray(event);
            if (bytes.length > 0) {
                this.manager.write(bytes);
                if (this.immediateFlush || event.isEndOfBatch()) {
                    this.manager.flush();
                }
            }
        }
        catch (AppenderLoggingException ex) {
            this.error("Unable to write to stream " + this.manager.getName() + " for appender " + this.getName());
            throw ex;
        }
        finally {
            this.readLock.unlock();
        }
    }
}

