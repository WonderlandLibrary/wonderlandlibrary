/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jctools.queues.MpscArrayQueue
 */
package org.apache.logging.log4j.core.async;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import org.apache.logging.log4j.core.async.BlockingQueueFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jctools.queues.MpscArrayQueue;

@Plugin(name="JCToolsBlockingQueue", category="Core", elementType="BlockingQueueFactory")
public class JCToolsBlockingQueueFactory<E>
implements BlockingQueueFactory<E> {
    private final WaitStrategy waitStrategy;

    private JCToolsBlockingQueueFactory(WaitStrategy waitStrategy) {
        this.waitStrategy = waitStrategy;
    }

    @Override
    public BlockingQueue<E> create(int capacity) {
        return new MpscBlockingQueue(capacity, this.waitStrategy);
    }

    @PluginFactory
    public static <E> JCToolsBlockingQueueFactory<E> createFactory(@PluginAttribute(value="WaitStrategy", defaultString="PARK") WaitStrategy waitStrategy) {
        return new JCToolsBlockingQueueFactory<E>(waitStrategy);
    }

    private static interface Idle {
        public int idle(int var1);
    }

    public static enum WaitStrategy {
        SPIN(idleCounter -> idleCounter + 1),
        YIELD(idleCounter -> {
            Thread.yield();
            return idleCounter + 1;
        }),
        PARK(idleCounter -> {
            LockSupport.parkNanos(1L);
            return idleCounter + 1;
        }),
        PROGRESSIVE(idleCounter -> {
            if (idleCounter > 200) {
                LockSupport.parkNanos(1L);
            } else if (idleCounter > 100) {
                Thread.yield();
            }
            return idleCounter + 1;
        });

        private final Idle idle;

        private int idle(int idleCounter) {
            return this.idle.idle(idleCounter);
        }

        private WaitStrategy(Idle idle) {
            this.idle = idle;
        }
    }

    private static final class MpscBlockingQueue<E>
    extends MpscArrayQueue<E>
    implements BlockingQueue<E> {
        private final WaitStrategy waitStrategy;

        MpscBlockingQueue(int capacity, WaitStrategy waitStrategy) {
            super(capacity);
            this.waitStrategy = waitStrategy;
        }

        @Override
        public int drainTo(Collection<? super E> c) {
            return this.drainTo(c, this.capacity());
        }

        @Override
        public int drainTo(Collection<? super E> c, int maxElements) {
            return this.drain(e -> c.add(e), maxElements);
        }

        @Override
        public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
            int idleCounter = 0;
            long timeoutNanos = System.nanoTime() + unit.toNanos(timeout);
            do {
                if (this.offer(e)) {
                    return true;
                }
                if (System.nanoTime() - timeoutNanos > 0L) {
                    return false;
                }
                idleCounter = this.waitStrategy.idle(idleCounter);
            } while (!Thread.interrupted());
            throw new InterruptedException();
        }

        @Override
        public E poll(long timeout, TimeUnit unit) throws InterruptedException {
            int idleCounter = 0;
            long timeoutNanos = System.nanoTime() + unit.toNanos(timeout);
            do {
                Object result;
                if ((result = this.poll()) != null) {
                    return (E)result;
                }
                if (System.nanoTime() - timeoutNanos > 0L) {
                    return null;
                }
                idleCounter = this.waitStrategy.idle(idleCounter);
            } while (!Thread.interrupted());
            throw new InterruptedException();
        }

        @Override
        public void put(E e) throws InterruptedException {
            int idleCounter = 0;
            do {
                if (this.offer(e)) {
                    return;
                }
                idleCounter = this.waitStrategy.idle(idleCounter);
            } while (!Thread.interrupted());
            throw new InterruptedException();
        }

        @Override
        public boolean offer(E e) {
            return this.offerIfBelowThreshold(e, this.capacity() - 32);
        }

        @Override
        public int remainingCapacity() {
            return this.capacity() - this.size();
        }

        @Override
        public E take() throws InterruptedException {
            int idleCounter = 100;
            do {
                Object result;
                if ((result = this.relaxedPoll()) != null) {
                    return (E)result;
                }
                idleCounter = this.waitStrategy.idle(idleCounter);
            } while (!Thread.interrupted());
            throw new InterruptedException();
        }
    }
}

