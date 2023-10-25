/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;

public interface PacketHeader {
    public boolean isLengthVariable();

    public int getLengthSize();

    public int getLengthSize(int var1);

    public int readLength(NetInput var1, int var2) throws IOException;

    public void writeLength(NetOutput var1, int var2) throws IOException;

    public int readPacketId(NetInput var1) throws IOException;

    public void writePacketId(NetOutput var1, int var2) throws IOException;
}

