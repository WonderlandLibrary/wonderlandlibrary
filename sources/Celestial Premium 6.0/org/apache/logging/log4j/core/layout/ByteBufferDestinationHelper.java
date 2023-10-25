/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.layout;

import java.nio.ByteBuffer;
import org.apache.logging.log4j.core.layout.ByteBufferDestination;

public final class ByteBufferDestinationHelper {
    private ByteBufferDestinationHelper() {
    }

    public static void writeToUnsynchronized(ByteBuffer source, ByteBufferDestination destination) {
        ByteBuffer destBuff = destination.getByteBuffer();
        while (source.remaining() > destBuff.remaining()) {
            int originalLimit = source.limit();
            source.limit(Math.min(source.limit(), source.position() + destBuff.remaining()));
            destBuff.put(source);
            source.limit(originalLimit);
            destBuff = destination.drain(destBuff);
        }
        destBuff.put(source);
    }

    public static void writeToUnsynchronized(byte[] data, int offset, int length, ByteBufferDestination destination) {
        ByteBuffer buffer = destination.getByteBuffer();
        while (length > buffer.remaining()) {
            int chunk = buffer.remaining();
            buffer.put(data, offset, chunk);
            offset += chunk;
            length -= chunk;
            buffer = destination.drain(buffer);
        }
        buffer.put(data, offset, length);
    }
}

