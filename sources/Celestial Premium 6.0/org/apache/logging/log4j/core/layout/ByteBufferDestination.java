/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.layout;

import java.nio.ByteBuffer;

public interface ByteBufferDestination {
    public ByteBuffer getByteBuffer();

    public ByteBuffer drain(ByteBuffer var1);

    public void writeBytes(ByteBuffer var1);

    public void writeBytes(byte[] var1, int var2, int var3);
}

