/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.layout.ByteBufferDestination;

public interface Encoder<T> {
    public void encode(T var1, ByteBufferDestination var2);
}

