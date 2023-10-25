/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;

public class ServerEntitySetPassengersPacket
extends MinecraftPacket {
    private int entityId;
    private int[] passengerIds;

    private ServerEntitySetPassengersPacket() {
    }

    public ServerEntitySetPassengersPacket(int entityId, int ... passengerIds) {
        this.entityId = entityId;
        this.passengerIds = passengerIds;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int[] getPassengerIds() {
        return this.passengerIds;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.passengerIds = new int[in.readVarInt()];
        int index = 0;
        while (index < this.passengerIds.length) {
            this.passengerIds[index] = in.readVarInt();
            ++index;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeVarInt(this.passengerIds.length);
        int[] nArray = this.passengerIds;
        int n = this.passengerIds.length;
        int n2 = 0;
        while (n2 < n) {
            int entityId = nArray[n2];
            out.writeVarInt(entityId);
            ++n2;
        }
    }
}

