/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.FileRegion;
import io.netty.channel.MessageSizeEstimator;

public final class DefaultMessageSizeEstimator
implements MessageSizeEstimator {
    private final MessageSizeEstimator.Handle handle;
    public static final MessageSizeEstimator DEFAULT = new DefaultMessageSizeEstimator(0);

    @Override
    public MessageSizeEstimator.Handle newHandle() {
        return this.handle;
    }

    public DefaultMessageSizeEstimator(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("unknownSize: " + n + " (expected: >= 0)");
        }
        this.handle = new HandleImpl(n);
    }

    private static final class HandleImpl
    implements MessageSizeEstimator.Handle {
        private final int unknownSize;

        @Override
        public int size(Object object) {
            if (object instanceof ByteBuf) {
                return ((ByteBuf)object).readableBytes();
            }
            if (object instanceof ByteBufHolder) {
                return ((ByteBufHolder)object).content().readableBytes();
            }
            if (object instanceof FileRegion) {
                return 0;
            }
            return this.unknownSize;
        }

        private HandleImpl(int n) {
            this.unknownSize = n;
        }
    }
}

