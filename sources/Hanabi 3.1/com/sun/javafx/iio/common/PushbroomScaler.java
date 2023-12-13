package com.sun.javafx.iio.common;

import java.nio.ByteBuffer;

public interface PushbroomScaler {
   ByteBuffer getDestination();

   boolean putSourceScanline(byte[] var1, int var2);
}
