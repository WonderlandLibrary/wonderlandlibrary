/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.async;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.core.async.BlockingQueueFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name="ArrayBlockingQueue", category="Core", elementType="BlockingQueueFactory")
public class ArrayBlockingQueueFactory<E>
implements BlockingQueueFactory<E> {
    @Override
    public BlockingQueue<E> create(int capacity) {
        return new ArrayBlockingQueue(capacity);
    }

    @PluginFactory
    public static <E> ArrayBlockingQueueFactory<E> createFactory() {
        return new ArrayBlockingQueueFactory<E>();
    }
}

