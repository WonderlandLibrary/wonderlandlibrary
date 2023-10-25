/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name="Queue", category="Core", elementType="appender", printObject=true)
public class QueueLogAppender
extends AbstractAppender {
    private static final int MAX_CAPACITY = 250;
    private static final Map<String, BlockingQueue<String>> QUEUES = new HashMap<String, BlockingQueue<String>>();
    private static final ReadWriteLock QUEUE_LOCK = new ReentrantReadWriteLock();
    private final BlockingQueue<String> queue;

    public QueueLogAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, BlockingQueue<String> queue) {
        super(name, filter, layout, ignoreExceptions);
        this.queue = queue;
    }

    @Override
    public void append(LogEvent event) {
        if (this.queue.size() >= 250) {
            this.queue.clear();
        }
        this.queue.add(this.getLayout().toSerializable(event).toString());
    }

    @PluginFactory
    public static QueueLogAppender createAppender(@PluginAttribute(value="name") String name, @PluginAttribute(value="ignoreExceptions") String ignore, @PluginElement(value="Layout") Layout<? extends Serializable> layout, @PluginElement(value="Filters") Filter filter, @PluginAttribute(value="target") String target) {
        boolean ignoreExceptions = Boolean.parseBoolean(ignore);
        if (name == null) {
            LOGGER.error("No name provided for QueueLogAppender");
            return null;
        }
        if (target == null) {
            target = name;
        }
        QUEUE_LOCK.writeLock().lock();
        BlockingQueue<String> queue = QUEUES.get(target);
        if (queue == null) {
            queue = new LinkedBlockingQueue<String>();
            QUEUES.put(target, queue);
        }
        QUEUE_LOCK.writeLock().unlock();
        if (layout == null) {
            layout = PatternLayout.newBuilder().build();
        }
        return new QueueLogAppender(name, filter, layout, ignoreExceptions, queue);
    }

    public static String getNextLogEvent(String queueName) {
        QUEUE_LOCK.readLock().lock();
        BlockingQueue<String> queue = QUEUES.get(queueName);
        QUEUE_LOCK.readLock().unlock();
        if (queue != null) {
            try {
                return queue.take();
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        return null;
    }
}

