// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import java.nio.ByteBuffer;

public interface ByteBufferDestination
{
    ByteBuffer getByteBuffer();
    
    ByteBuffer drain(final ByteBuffer buf);
    
    void writeBytes(final ByteBuffer data);
    
    void writeBytes(final byte[] data, final int offset, final int length);
}
