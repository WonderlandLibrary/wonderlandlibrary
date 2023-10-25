/*
 * Decompiled with CFR 0.152.
 */
package io.netty.channel;

import io.netty.channel.FileRegion;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class DefaultFileRegion
extends AbstractReferenceCounted
implements FileRegion {
    private long transfered;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultFileRegion.class);
    private final long position;
    private final FileChannel file;
    private final long count;

    public DefaultFileRegion(FileChannel fileChannel, long l, long l2) {
        if (fileChannel == null) {
            throw new NullPointerException("file");
        }
        if (l < 0L) {
            throw new IllegalArgumentException("position must be >= 0 but was " + l);
        }
        if (l2 < 0L) {
            throw new IllegalArgumentException("count must be >= 0 but was " + l2);
        }
        this.file = fileChannel;
        this.position = l;
        this.count = l2;
    }

    @Override
    public long count() {
        return this.count;
    }

    @Override
    public long transfered() {
        return this.transfered;
    }

    @Override
    protected void deallocate() {
        block2: {
            try {
                this.file.close();
            }
            catch (IOException iOException) {
                if (!logger.isWarnEnabled()) break block2;
                logger.warn("Failed to close a file.", iOException);
            }
        }
    }

    @Override
    public long position() {
        return this.position;
    }

    @Override
    public long transferTo(WritableByteChannel writableByteChannel, long l) throws IOException {
        long l2 = this.count - l;
        if (l2 < 0L || l < 0L) {
            throw new IllegalArgumentException("position out of range: " + l + " (expected: 0 - " + (this.count - 1L) + ')');
        }
        if (l2 == 0L) {
            return 0L;
        }
        long l3 = this.file.transferTo(this.position + l, l2, writableByteChannel);
        if (l3 > 0L) {
            this.transfered += l3;
        }
        return l3;
    }
}

