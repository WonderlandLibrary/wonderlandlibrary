package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S14PacketEntity;

public static class S14PacketEntity.S16PacketEntityLook
extends S14PacketEntity {
    public S14PacketEntity.S16PacketEntityLook() {
        this.field_149069_g = true;
    }

    public S14PacketEntity.S16PacketEntityLook(int entityIdIn, byte yawIn, byte pitchIn, boolean onGroundIn) {
        super(entityIdIn);
        this.yaw = yawIn;
        this.pitch = pitchIn;
        this.field_149069_g = true;
        this.onGround = onGroundIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        super.readPacketData(buf);
        this.yaw = buf.readByte();
        this.pitch = buf.readByte();
        this.onGround = buf.readBoolean();
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        super.writePacketData(buf);
        buf.writeByte((int)this.yaw);
        buf.writeByte((int)this.pitch);
        buf.writeBoolean(this.onGround);
    }
}
