/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.iio.common;

import java.nio.ByteBuffer;

public interface PushbroomScaler {
    public ByteBuffer getDestination();

    public boolean putSourceScanline(byte[] var1, int var2);
}

