package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C03PacketPlayer
implements Packet<INetHandlerPlayServer> {
    protected double x;
    public double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    public boolean onGround;
    protected boolean moving;
    protected boolean rotating;

    public C03PacketPlayer() {
    }

    public C03PacketPlayer(boolean isOnGround) {
        this.onGround = isOnGround;
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.processPlayer(this);
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.onGround = buf.readUnsignedByte() != 0;
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeByte(this.onGround ? 1 : 0);
    }

    public double getPositionX() {
        return this.x;
    }

    public double getPositionY() {
        return this.y;
    }

    public double getPositionZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public boolean isMoving() {
        return this.moving;
    }

    public boolean getRotating() {
        return this.rotating;
    }

    public void setMoving(boolean isMoving) {
        this.moving = isMoving;
    }
}
