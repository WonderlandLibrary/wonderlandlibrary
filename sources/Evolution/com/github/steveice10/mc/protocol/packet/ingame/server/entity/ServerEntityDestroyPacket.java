/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;

public class ServerEntityDestroyPacket
extends MinecraftPacket {
    private int[] entityIds;

    private ServerEntityDestroyPacket() {
    }

    public ServerEntityDestroyPacket(int ... entityIds) {
        this.entityIds = entityIds;
    }

    public int[] getEntityIds() {
        return this.entityIds;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityIds = new int[in.readVarInt()];
        int index = 0;
        while (index < this.entityIds.length) {
            this.entityIds[index] = in.readVarInt();
            ++index;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityIds.length);
        int[] nArray = this.entityIds;
        int n = this.entityIds.length;
        int n2 = 0;
        while (n2 < n) {
            int entityId = nArray[n2];
            out.writeVarInt(entityId);
            ++n2;
        }
    }
}

