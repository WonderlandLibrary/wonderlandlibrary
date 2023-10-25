/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

import java.io.IOException;
import net.java.games.input.OSXHIDDevice;

final class OSXHIDDeviceIterator {
    private final long iterator_address = OSXHIDDeviceIterator.nCreateIterator();

    private static final native long nCreateIterator();

    public final void close() {
        OSXHIDDeviceIterator.nReleaseIterator(this.iterator_address);
    }

    private static final native void nReleaseIterator(long var0);

    public final OSXHIDDevice next() throws IOException {
        return OSXHIDDeviceIterator.nNext(this.iterator_address);
    }

    private static final native OSXHIDDevice nNext(long var0) throws IOException;
}

