/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server.entity.player;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.player.PositionElement;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerPlayerPositionRotationPacket
extends MinecraftPacket {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private List<PositionElement> relative;
    private int teleportId;

    private ServerPlayerPositionRotationPacket() {
    }

    public ServerPlayerPositionRotationPacket(double x, double y, double z, float yaw, float pitch, int teleportId, PositionElement ... relative) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.teleportId = teleportId;
        this.relative = Arrays.asList(relative != null ? relative : new PositionElement[]{});
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public List<PositionElement> getRelativeElements() {
        return this.relative;
    }

    public int getTeleportId() {
        return this.teleportId;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readFloat();
        this.pitch = in.readFloat();
        this.relative = new ArrayList<PositionElement>();
        int flags = in.readUnsignedByte();
        PositionElement[] positionElementArray = PositionElement.values();
        int n = positionElementArray.length;
        int n2 = 0;
        while (n2 < n) {
            PositionElement element = positionElementArray[n2];
            int bit = 1 << MagicValues.value(Integer.class, (Object)element);
            if ((flags & bit) == bit) {
                this.relative.add(element);
            }
            ++n2;
        }
        this.teleportId = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeDouble(this.x);
        out.writeDouble(this.y);
        out.writeDouble(this.z);
        out.writeFloat(this.yaw);
        out.writeFloat(this.pitch);
        int flags = 0;
        for (PositionElement element : this.relative) {
            flags |= 1 << MagicValues.value(Integer.class, (Object)element);
        }
        out.writeByte(flags);
        out.writeVarInt(this.teleportId);
    }
}

