/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.ChannelPromise;
import java.util.ArrayDeque;
import java.util.Queue;

public final class ChannelFlushPromiseNotifier {
    private long writeCounter;
    private final boolean tryNotify;
    private final Queue<FlushCheckpoint> flushCheckpoints = new ArrayDeque<FlushCheckpoint>();

    public ChannelFlushPromiseNotifier notifyPromises(Throwable throwable) {
        FlushCheckpoint flushCheckpoint;
        this.notifyPromises();
        while ((flushCheckpoint = this.flushCheckpoints.poll()) != null) {
            if (this.tryNotify) {
                flushCheckpoint.promise().tryFailure(throwable);
                continue;
            }
            flushCheckpoint.promise().setFailure(throwable);
        }
        return this;
    }

    public ChannelFlushPromiseNotifier(boolean bl) {
        this.tryNotify = bl;
    }

    public ChannelFlushPromiseNotifier notifyPromises(Throwable throwable, Throwable throwable2) {
        FlushCheckpoint flushCheckpoint;
        this.notifyPromises0(throwable);
        while ((flushCheckpoint = this.flushCheckpoints.poll()) != null) {
            if (this.tryNotify) {
                flushCheckpoint.promise().tryFailure(throwable2);
                continue;
            }
            flushCheckpoint.promise().setFailure(throwable2);
        }
        return this;
    }

    @Deprecated
    public ChannelFlushPromiseNotifier notifyFlushFutures(Throwable throwable, Throwable throwable2) {
        return this.notifyPromises(throwable, throwable2);
    }

    public ChannelFlushPromiseNotifier increaseWriteCounter(long l) {
        if (l < 0L) {
            throw new IllegalArgumentException("delta must be >= 0 but was " + l);
        }
        this.writeCounter += l;
        return this;
    }

    public long writeCounter() {
        return this.writeCounter;
    }

    private void notifyPromises0(Throwable throwable) {
        if (this.flushCheckpoints.isEmpty()) {
            this.writeCounter = 0L;
            return;
        }
        long l = this.writeCounter;
        while (true) {
            FlushCheckpoint flushCheckpoint;
            if ((flushCheckpoint = this.flushCheckpoints.peek()) == null) {
                this.writeCounter = 0L;
                break;
            }
            if (flushCheckpoint.flushCheckpoint() > l) {
                if (l <= 0L || this.flushCheckpoints.size() != 1) break;
                this.writeCounter = 0L;
                flushCheckpoint.flushCheckpoint(flushCheckpoint.flushCheckpoint() - l);
                break;
            }
            this.flushCheckpoints.remove();
            ChannelPromise channelPromise = flushCheckpoint.promise();
            if (throwable == null) {
                if (this.tryNotify) {
                    channelPromise.trySuccess();
                    continue;
                }
                channelPromise.setSuccess();
                continue;
            }
            if (this.tryNotify) {
                channelPromise.tryFailure(throwable);
                continue;
            }
            channelPromise.setFailure(throwable);
        }
        long l2 = this.writeCounter;
        if (l2 >= 0x8000000000L) {
            this.writeCounter = 0L;
            for (FlushCheckpoint flushCheckpoint : this.flushCheckpoints) {
                flushCheckpoint.flushCheckpoint(flushCheckpoint.flushCheckpoint() - l2);
            }
        }
    }

    public ChannelFlushPromiseNotifier notifyPromises() {
        this.notifyPromises0(null);
        return this;
    }

    @Deprecated
    public ChannelFlushPromiseNotifier notifyFlushFutures(Throwable throwable) {
        return this.notifyPromises(throwable);
    }

    @Deprecated
    public ChannelFlushPromiseNotifier notifyFlushFutures() {
        return this.notifyPromises();
    }

    public ChannelFlushPromiseNotifier add(ChannelPromise channelPromise, long l) {
        if (channelPromise == null) {
            throw new NullPointerException("promise");
        }
        if (l < 0L) {
            throw new IllegalArgumentException("pendingDataSize must be >= 0 but was " + l);
        }
        long l2 = this.writeCounter + l;
        if (channelPromise instanceof FlushCheckpoint) {
            FlushCheckpoint flushCheckpoint = (FlushCheckpoint)((Object)channelPromise);
            flushCheckpoint.flushCheckpoint(l2);
            this.flushCheckpoints.add(flushCheckpoint);
        } else {
            this.flushCheckpoints.add(new DefaultFlushCheckpoint(l2, channelPromise));
        }
        return this;
    }

    public ChannelFlushPromiseNotifier() {
        this(false);
    }

    @Deprecated
    public ChannelFlushPromiseNotifier add(ChannelPromise channelPromise, int n) {
        return this.add(channelPromise, (long)n);
    }

    static interface FlushCheckpoint {
        public ChannelPromise promise();

        public void flushCheckpoint(long var1);

        public long flushCheckpoint();
    }

    private static class DefaultFlushCheckpoint
    implements FlushCheckpoint {
        private long checkpoint;
        private final ChannelPromise future;

        @Override
        public long flushCheckpoint() {
            return this.checkpoint;
        }

        @Override
        public void flushCheckpoint(long l) {
            this.checkpoint = l;
        }

        DefaultFlushCheckpoint(long l, ChannelPromise channelPromise) {
            this.checkpoint = l;
            this.future = channelPromise;
        }

        @Override
        public ChannelPromise promise() {
            return this.future;
        }
    }
}

