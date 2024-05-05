package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S14PacketEntity
implements Packet<INetHandlerPlayClient> {
    protected int entityId;
    protected byte posX;
    protected byte posY;
    protected byte posZ;
    protected byte yaw;
    protected byte pitch;
    protected boolean onGround;
    protected boolean field_149069_g;

    public S14PacketEntity() {
    }

    public S14PacketEntity(int entityIdIn) {
        this.entityId = entityIdIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarIntFromBuffer();
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityId);
    }

    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleEntityMovement(this);
    }

    public String toString() {
        return "Entity_" + super.toString();
    }

    public Entity getEntity(World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }

    public byte func_149062_c() {
        return this.posX;
    }

    public byte func_149061_d() {
        return this.posY;
    }

    public byte func_149064_e() {
        return this.posZ;
    }

    public byte func_149066_f() {
        return this.yaw;
    }

    public byte func_149063_g() {
        return this.pitch;
    }

    public boolean func_149060_h() {
        return this.field_149069_g;
    }

    public boolean getOnGround() {
        return this.onGround;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void setPosX(int posX) {
        this.posX = (byte)posX;
    }

    public void setPosY(int posY) {
        this.posY = (byte)posY;
    }

    public void setPosZ(int posZ) {
        this.posZ = (byte)posZ;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public byte getYaw() {
        return this.yaw;
    }

    public byte getPitch() {
        return this.pitch;
    }
}
