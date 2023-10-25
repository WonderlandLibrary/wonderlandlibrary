/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.api.protocol.remapper;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;

@FunctionalInterface
public interface ValueWriter<T> {
    public void write(PacketWrapper var1, T var2) throws Exception;
}

