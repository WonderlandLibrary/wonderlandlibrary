/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.packetlib.packet;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.PacketHeader;
import java.io.IOException;

public class DefaultPacketHeader
implements PacketHeader {
    @Override
    public boolean isLengthVariable() {
        return true;
    }

    @Override
    public int getLengthSize() {
        return 5;
    }

    @Override
    public int getLengthSize(int length) {
        if ((length & 0xFFFFFF80) == 0) {
            return 1;
        }
        if ((length & 0xFFFFC000) == 0) {
            return 2;
        }
        if ((length & 0xFFE00000) == 0) {
            return 3;
        }
        if ((length & 0xF0000000) == 0) {
            return 4;
        }
        return 5;
    }

    @Override
    public int readLength(NetInput in, int available) throws IOException {
        return in.readVarInt();
    }

    @Override
    public void writeLength(NetOutput out, int length) throws IOException {
        out.writeVarInt(length);
    }

    @Override
    public int readPacketId(NetInput in) throws IOException {
        return in.readVarInt();
    }

    @Override
    public void writePacketId(NetOutput out, int packetId) throws IOException {
        out.writeVarInt(packetId);
    }
}

