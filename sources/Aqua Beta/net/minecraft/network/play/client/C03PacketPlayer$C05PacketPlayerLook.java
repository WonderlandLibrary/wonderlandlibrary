package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C03PacketPlayer;

public static class C03PacketPlayer.C05PacketPlayerLook
extends C03PacketPlayer {
    public C03PacketPlayer.C05PacketPlayerLook() {
        this.rotating = true;
    }

    public C03PacketPlayer.C05PacketPlayerLook(float playerYaw, float playerPitch, boolean isOnGround) {
        this.yaw = playerYaw;
        this.pitch = playerPitch;
        this.onGround = isOnGround;
        this.rotating = true;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        super.readPacketData(buf);
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        super.writePacketData(buf);
    }
}
