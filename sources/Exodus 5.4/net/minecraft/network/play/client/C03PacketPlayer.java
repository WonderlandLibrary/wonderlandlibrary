/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C03PacketPlayer
implements Packet<INetHandlerPlayServer> {
    public float pitch;
    public boolean rotating;
    public double z;
    public boolean moving;
    public float yaw;
    public double x;
    public boolean onGround;
    public double y;

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processPlayer(this);
    }

    public float getYaw() {
        return this.yaw;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.onGround = packetBuffer.readUnsignedByte() != 0;
    }

    public C03PacketPlayer(boolean bl) {
        this.onGround = bl;
    }

    public void setMoving(boolean bl) {
        this.moving = bl;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeByte(this.onGround ? 1 : 0);
    }

    public double getPositionZ() {
        return this.z;
    }

    public boolean isMoving() {
        return this.moving;
    }

    public float getPitch() {
        return this.pitch;
    }

    public C03PacketPlayer() {
    }

    public boolean getRotating() {
        return this.rotating;
    }

    public double getPositionX() {
        return this.x;
    }

    public double getPositionY() {
        return this.y;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public static class C05PacketPlayerLook
    extends C03PacketPlayer {
        public C05PacketPlayerLook(float f, float f2, boolean bl) {
            this.yaw = f;
            this.pitch = f2;
            this.onGround = bl;
            this.rotating = true;
        }

        @Override
        public void writePacketData(PacketBuffer packetBuffer) throws IOException {
            packetBuffer.writeFloat(this.yaw);
            packetBuffer.writeFloat(this.pitch);
            super.writePacketData(packetBuffer);
        }

        @Override
        public void readPacketData(PacketBuffer packetBuffer) throws IOException {
            this.yaw = packetBuffer.readFloat();
            this.pitch = packetBuffer.readFloat();
            super.readPacketData(packetBuffer);
        }

        public C05PacketPlayerLook() {
            this.rotating = true;
        }
    }

    public static class C06PacketPlayerPosLook
    extends C03PacketPlayer {
        @Override
        public void writePacketData(PacketBuffer packetBuffer) throws IOException {
            packetBuffer.writeDouble(this.x);
            packetBuffer.writeDouble(this.y);
            packetBuffer.writeDouble(this.z);
            packetBuffer.writeFloat(this.yaw);
            packetBuffer.writeFloat(this.pitch);
            super.writePacketData(packetBuffer);
        }

        public C06PacketPlayerPosLook(double d, double d2, double d3, float f, float f2, boolean bl) {
            this.x = d;
            this.y = d2;
            this.z = d3;
            this.yaw = f;
            this.pitch = f2;
            this.onGround = bl;
            this.rotating = true;
            this.moving = true;
        }

        @Override
        public void readPacketData(PacketBuffer packetBuffer) throws IOException {
            this.x = packetBuffer.readDouble();
            this.y = packetBuffer.readDouble();
            this.z = packetBuffer.readDouble();
            this.yaw = packetBuffer.readFloat();
            this.pitch = packetBuffer.readFloat();
            super.readPacketData(packetBuffer);
        }

        public C06PacketPlayerPosLook() {
            this.moving = true;
            this.rotating = true;
        }
    }

    public static class C04PacketPlayerPosition
    extends C03PacketPlayer {
        public C04PacketPlayerPosition(double d, double d2, double d3, boolean bl) {
            this.x = d;
            this.y = d2;
            this.z = d3;
            this.onGround = bl;
            this.moving = true;
        }

        public C04PacketPlayerPosition() {
            this.moving = true;
        }

        @Override
        public void readPacketData(PacketBuffer packetBuffer) throws IOException {
            this.x = packetBuffer.readDouble();
            this.y = packetBuffer.readDouble();
            this.z = packetBuffer.readDouble();
            super.readPacketData(packetBuffer);
        }

        @Override
        public void writePacketData(PacketBuffer packetBuffer) throws IOException {
            packetBuffer.writeDouble(this.x);
            packetBuffer.writeDouble(this.y);
            packetBuffer.writeDouble(this.z);
            super.writePacketData(packetBuffer);
        }
    }
}

