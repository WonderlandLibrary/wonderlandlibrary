/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMovementPacket;

public class ServerEntityPositionRotationPacket
extends ServerEntityMovementPacket {
    protected ServerEntityPositionRotationPacket() {
        this.pos = true;
        this.rot = true;
    }

    public ServerEntityPositionRotationPacket(int entityId, double moveX, double moveY, double moveZ, float yaw, float pitch, boolean onGround) {
        super(entityId, onGround);
        this.pos = true;
        this.rot = true;
        this.moveX = moveX;
        this.moveY = moveY;
        this.moveZ = moveZ;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}

