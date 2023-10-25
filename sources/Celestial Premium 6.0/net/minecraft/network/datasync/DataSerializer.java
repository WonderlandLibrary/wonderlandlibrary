/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.datasync;

import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;

public interface DataSerializer<T> {
    public void write(PacketBuffer var1, T var2);

    public T read(PacketBuffer var1) throws IOException;

    public DataParameter<T> createKey(int var1);

    public T func_192717_a(T var1);
}

