// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

public interface Encoder<T>
{
    void encode(final T source, final ByteBufferDestination destination);
}
