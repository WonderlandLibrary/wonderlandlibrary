package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S14PacketEntity;

public static class S14PacketEntity.S15PacketEntityRelMove
extends S14PacketEntity {
    public S14PacketEntity.S15PacketEntityRelMove() {
    }

    public S14PacketEntity.S15PacketEntityRelMove(int entityIdIn, byte x, byte y, byte z, boolean onGroundIn) {
        super(entityIdIn);
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.onGround = onGroundIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        super.readPacketData(buf);
        this.posX = buf.readByte();
        this.posY = buf.readByte();
        this.posZ = buf.readByte();
        this.onGround = buf.readBoolean();
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        super.writePacketData(buf);
        buf.writeByte((int)this.posX);
        buf.writeByte((int)this.posY);
        buf.writeByte((int)this.posZ);
        buf.writeBoolean(this.onGround);
    }
}
