/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;

public interface Packet {
    public void read(NetInput var1) throws IOException;

    public void write(NetOutput var1) throws IOException;

    public boolean isPriority();
}

