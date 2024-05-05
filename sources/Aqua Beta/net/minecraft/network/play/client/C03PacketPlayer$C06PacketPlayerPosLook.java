package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;

public static class C03PacketPlayer.C06PacketPlayerPosLook
extends C03PacketPlayer {
    public C03PacketPlayer.C06PacketPlayerPosLook() {
        this.moving = true;
        this.rotating = true;
    }

    public C03PacketPlayer.C06PacketPlayerPosLook(double playerX, double playerY, double playerZ, float playerYaw, float playerPitch, boolean playerIsOnGround) {
        this.x = playerX;
        this.y = playerY;
        this.z = playerZ;
        this.yaw = playerYaw;
        this.pitch = playerPitch;
        this.onGround = playerIsOnGround;
        this.rotating = true;
        this.moving = true;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        super.readPacketData(buf);
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        super.writePacketData(buf);
    }
}
