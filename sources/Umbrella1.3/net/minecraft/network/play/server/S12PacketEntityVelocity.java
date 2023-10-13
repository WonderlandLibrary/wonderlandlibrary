/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S12PacketEntityVelocity
implements Packet {
    private int field_149417_a;
    public int motionX;
    public int motionY;
    public int motionZ;
    private static final String __OBFID = "CL_00001328";

    public S12PacketEntityVelocity() {
    }

    public S12PacketEntityVelocity(Entity p_i45219_1_) {
        this(p_i45219_1_.getEntityId(), p_i45219_1_.motionX, p_i45219_1_.motionY, p_i45219_1_.motionZ);
    }

    public S12PacketEntityVelocity(int p_i45220_1_, double p_i45220_2_, double p_i45220_4_, double p_i45220_6_) {
        this.field_149417_a = p_i45220_1_;
        double var8 = 3.9;
        if (p_i45220_2_ < -var8) {
            p_i45220_2_ = -var8;
        }
        if (p_i45220_4_ < -var8) {
            p_i45220_4_ = -var8;
        }
        if (p_i45220_6_ < -var8) {
            p_i45220_6_ = -var8;
        }
        if (p_i45220_2_ > var8) {
            p_i45220_2_ = var8;
        }
        if (p_i45220_4_ > var8) {
            p_i45220_4_ = var8;
        }
        if (p_i45220_6_ > var8) {
            p_i45220_6_ = var8;
        }
        this.motionX = (int)(p_i45220_2_ * 8000.0);
        this.motionY = (int)(p_i45220_4_ * 8000.0);
        this.motionZ = (int)(p_i45220_6_ * 8000.0);
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_149417_a = data.readVarIntFromBuffer();
        this.motionX = data.readShort();
        this.motionY = data.readShort();
        this.motionZ = data.readShort();
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149417_a);
        data.writeShort(this.motionX);
        data.writeShort(this.motionY);
        data.writeShort(this.motionZ);
    }

    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleEntityVelocity(this);
    }

    public int func_149412_c() {
        return this.field_149417_a;
    }

    public int getMotionX() {
        return this.motionX;
    }

    public int getMotionY() {
        return this.motionY;
    }

    public int getMotionZ() {
        return this.motionZ;
    }

    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}

