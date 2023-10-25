package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;

public static class C03PacketPlayer.C04PacketPlayerPosition
extends C03PacketPlayer {
    public C03PacketPlayer.C04PacketPlayerPosition() {
        this.moving = true;
    }

    public C03PacketPlayer.C04PacketPlayerPosition(double posX, double posY, double posZ, boolean isOnGround) {
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.onGround = isOnGround;
        this.moving = true;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        super.readPacketData(buf);
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        super.writePacketData(buf);
    }
}
