// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.nio.ByteBuffer;

public final class ByteBufferDestinationHelper
{
    private ByteBufferDestinationHelper() {
    }
    
    public static void writeToUnsynchronized(final ByteBuffer source, final ByteBufferDestination destination) {
        ByteBuffer destBuff;
        for (destBuff = destination.getByteBuffer(); source.remaining() > destBuff.remaining(); destBuff = destination.drain(destBuff)) {
            final int originalLimit = source.limit();
            source.limit(Math.min(source.limit(), source.position() + destBuff.remaining()));
            destBuff.put(source);
            source.limit(originalLimit);
        }
        destBuff.put(source);
    }
    
    public static void writeToUnsynchronized(final byte[] data, int offset, int length, final ByteBufferDestination destination) {
        ByteBuffer buffer;
        int chunk;
        for (buffer = destination.getByteBuffer(); length > buffer.remaining(); length -= chunk, buffer = destination.drain(buffer)) {
            chunk = buffer.remaining();
            buffer.put(data, offset, chunk);
            offset += chunk;
        }
        buffer.put(data, offset, length);
    }
}
