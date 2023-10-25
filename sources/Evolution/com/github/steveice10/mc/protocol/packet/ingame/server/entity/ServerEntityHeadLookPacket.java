/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;

public class ServerEntityHeadLookPacket
extends MinecraftPacket {
    private int entityId;
    private float headYaw;

    private ServerEntityHeadLookPacket() {
    }

    public ServerEntityHeadLookPacket(int entityId, float headYaw) {
        this.entityId = entityId;
        this.headYaw = headYaw;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public float getHeadYaw() {
        return this.headYaw;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.headYaw = (float)(in.readByte() * 360) / 256.0f;
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte((byte)(this.headYaw * 256.0f / 360.0f));
    }
}

