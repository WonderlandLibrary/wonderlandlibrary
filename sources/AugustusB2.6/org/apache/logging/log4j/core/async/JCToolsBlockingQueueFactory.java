// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.TimeUnit;
import java.util.Collection;
import org.jctools.queues.MpscArrayQueue;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.concurrent.BlockingQueue;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "JCToolsBlockingQueue", category = "Core", elementType = "BlockingQueueFactory")
public class JCToolsBlockingQueueFactory<E> implements BlockingQueueFactory<E>
{
    private final WaitStrategy waitStrategy;
    
    private JCToolsBlockingQueueFactory(final WaitStrategy waitStrategy) {
        this.waitStrategy = waitStrategy;
    }
    
    @Override
    public BlockingQueue<E> create(final int capacity) {
        return new MpscBlockingQueue<E>(capacity, this.waitStrategy);
    }
    
    @PluginFactory
    public static <E> JCToolsBlockingQueueFactory<E> createFactory(@PluginAttribute(value = "WaitStrategy", defaultString = "PARK") final WaitStrategy waitStrategy) {
        return new JCToolsBlockingQueueFactory<E>(waitStrategy);
    }
    
    private static final class MpscBlockingQueue<E> extends MpscArrayQueue<E> implements BlockingQueue<E>
    {
        private final WaitStrategy waitStrategy;
        
        MpscBlockingQueue(final int capacity, final WaitStrategy waitStrategy) {
            super(capacity);
            this.waitStrategy = waitStrategy;
        }
        
        public int drainTo(final Collection<? super E> c) {
            return this.drainTo(c, this.capacity());
        }
        
        public int drainTo(final Collection<? super E> c, final int maxElements) {
            return this.drain(e -> c.add(e), maxElements);
        }
        
        public boolean offer(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
            int idleCounter = 0;
            final long timeoutNanos = System.nanoTime() + unit.toNanos(timeout);
            while (!this.offer(e)) {
                if (System.nanoTime() - timeoutNanos > 0L) {
                    return false;
                }
                idleCounter = this.waitStrategy.idle(idleCounter);
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
            return true;
        }
        
        public E poll(final long timeout, final TimeUnit unit) throws InterruptedException {
            int idleCounter = 0;
            final long timeoutNanos = System.nanoTime() + unit.toNanos(timeout);
            do {
                final E result = this.poll();
                if (result != null) {
                    return result;
                }
                if (System.nanoTime() - timeoutNanos > 0L) {
                    return null;
                }
                idleCounter = this.waitStrategy.idle(idleCounter);
            } while (!Thread.interrupted());
            throw new InterruptedException();
        }
        
        public void put(final E e) throws InterruptedException {
            int idleCounter = 0;
            while (!this.offer(e)) {
                idleCounter = this.waitStrategy.idle(idleCounter);
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
        }
        
        public boolean offer(final E e) {
            return this.offerIfBelowThreshold((Object)e, this.capacity() - 32);
        }
        
        public int remainingCapacity() {
            return this.capacity() - this.size();
        }
        
        public E take() throws InterruptedException {
            int idleCounter = 100;
            do {
                final E result = (E)this.relaxedPoll();
                if (result != null) {
                    return result;
                }
                idleCounter = this.waitStrategy.idle(idleCounter);
            } while (!Thread.interrupted());
            throw new InterruptedException();
        }
    }
    
    public enum WaitStrategy
    {
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
            }
            else if (idleCounter > 100) {
                Thread.yield();
            }
            return idleCounter + 1;
        });
        
        private final Idle idle;
        
        private int idle(final int idleCounter) {
            return this.idle.idle(idleCounter);
        }
        
        private WaitStrategy(final Idle idle) {
            this.idle = idle;
        }
    }
    
    private interface Idle
    {
        int idle(final int idleCounter);
    }
}
