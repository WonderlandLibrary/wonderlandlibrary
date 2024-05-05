package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S14PacketEntity;

public static class S14PacketEntity.S17PacketEntityLookMove
extends S14PacketEntity {
    public S14PacketEntity.S17PacketEntityLookMove() {
        this.field_149069_g = true;
    }

    public S14PacketEntity.S17PacketEntityLookMove(int p_i45973_1_, byte p_i45973_2_, byte p_i45973_3_, byte p_i45973_4_, byte p_i45973_5_, byte p_i45973_6_, boolean p_i45973_7_) {
        super(p_i45973_1_);
        this.posX = p_i45973_2_;
        this.posY = p_i45973_3_;
        this.posZ = p_i45973_4_;
        this.yaw = p_i45973_5_;
        this.pitch = p_i45973_6_;
        this.onGround = p_i45973_7_;
        this.field_149069_g = true;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        super.readPacketData(buf);
        this.posX = buf.readByte();
        this.posY = buf.readByte();
        this.posZ = buf.readByte();
        this.yaw = buf.readByte();
        this.pitch = buf.readByte();
        this.onGround = buf.readBoolean();
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        super.writePacketData(buf);
        buf.writeByte((int)this.posX);
        buf.writeByte((int)this.posY);
        buf.writeByte((int)this.posZ);
        buf.writeByte((int)this.yaw);
        buf.writeByte((int)this.pitch);
        buf.writeBoolean(this.onGround);
    }
}
