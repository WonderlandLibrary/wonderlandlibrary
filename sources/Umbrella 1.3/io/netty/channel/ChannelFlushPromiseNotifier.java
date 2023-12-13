/*
 * Decompiled with CFR 0.150.
 */
package io.netty.channel;

import io.netty.channel.ChannelPromise;
import java.util.ArrayDeque;
import java.util.Queue;

public final class ChannelFlushPromiseNotifier {
    private long writeCounter;
    private final Queue<FlushCheckpoint> flushCheckpoints = new ArrayDeque<FlushCheckpoint>();
    private final boolean tryNotify;

    public ChannelFlushPromiseNotifier(boolean tryNotify) {
        this.tryNotify = tryNotify;
    }

    public ChannelFlushPromiseNotifier() {
        this(false);
    }

    public ChannelFlushPromiseNotifier add(ChannelPromise promise, int pendingDataSize) {
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        if (pendingDataSize < 0) {
            throw new IllegalArgumentException("pendingDataSize must be >= 0 but was" + pendingDataSize);
        }
        long checkpoint = this.writeCounter + (long)pendingDataSize;
        if (promise instanceof FlushCheckpoint) {
            FlushCheckpoint cp = (FlushCheckpoint)((Object)promise);
            cp.flushCheckpoint(checkpoint);
            this.flushCheckpoints.add(cp);
        } else {
            this.flushCheckpoints.add(new DefaultFlushCheckpoint(checkpoint, promise));
        }
        return this;
    }

    public ChannelFlushPromiseNotifier increaseWriteCounter(long delta) {
        if (delta < 0L) {
            throw new IllegalArgumentException("delta must be >= 0 but was" + delta);
        }
        this.writeCounter += delta;
        return this;
    }

    public long writeCounter() {
        return this.writeCounter;
    }

    public ChannelFlushPromiseNotifier notifyFlushFutures() {
        this.notifyFlushFutures0(null);
        return this;
    }

    public ChannelFlushPromiseNotifier notifyFlushFutures(Throwable cause) {
        FlushCheckpoint cp;
        this.notifyFlushFutures();
        while ((cp = this.flushCheckpoints.poll()) != null) {
            if (this.tryNotify) {
                cp.promise().tryFailure(cause);
                continue;
            }
            cp.promise().setFailure(cause);
        }
        return this;
    }

    public ChannelFlushPromiseNotifier notifyFlushFutures(Throwable cause1, Throwable cause2) {
        FlushCheckpoint cp;
        this.notifyFlushFutures0(cause1);
        while ((cp = this.flushCheckpoints.poll()) != null) {
            if (this.tryNotify) {
                cp.promise().tryFailure(cause2);
                continue;
            }
            cp.promise().setFailure(cause2);
        }
        return this;
    }

    private void notifyFlushFutures0(Throwable cause) {
        if (this.flushCheckpoints.isEmpty()) {
            this.writeCounter = 0L;
            return;
        }
        long writeCounter = this.writeCounter;
        while (true) {
            FlushCheckpoint cp;
            if ((cp = this.flushCheckpoints.peek()) == null) {
                this.writeCounter = 0L;
                break;
            }
            if (cp.flushCheckpoint() > writeCounter) {
                if (writeCounter <= 0L || this.flushCheckpoints.size() != 1) break;
                this.writeCounter = 0L;
                cp.flushCheckpoint(cp.flushCheckpoint() - writeCounter);
                break;
            }
            this.flushCheckpoints.remove();
            if (cause == null) {
                if (this.tryNotify) {
                    cp.promise().trySuccess();
                    continue;
                }
                cp.promise().setSuccess();
                continue;
            }
            if (this.tryNotify) {
                cp.promise().tryFailure(cause);
                continue;
            }
            cp.promise().setFailure(cause);
        }
        long newWriteCounter = this.writeCounter;
        if (newWriteCounter >= 0x8000000000L) {
            this.writeCounter = 0L;
            for (FlushCheckpoint cp : this.flushCheckpoints) {
                cp.flushCheckpoint(cp.flushCheckpoint() - newWriteCounter);
            }
        }
    }

    private static class DefaultFlushCheckpoint
    implements FlushCheckpoint {
        private long checkpoint;
        private final ChannelPromise future;

        DefaultFlushCheckpoint(long checkpoint, ChannelPromise future) {
            this.checkpoint = checkpoint;
            this.future = future;
        }

        @Override
        public long flushCheckpoint() {
            return this.checkpoint;
        }

        @Override
        public void flushCheckpoint(long checkpoint) {
            this.checkpoint = checkpoint;
        }

        @Override
        public ChannelPromise promise() {
            return this.future;
        }
    }

    static interface FlushCheckpoint {
        public long flushCheckpoint();

        public void flushCheckpoint(long var1);

        public ChannelPromise promise();
    }
}

