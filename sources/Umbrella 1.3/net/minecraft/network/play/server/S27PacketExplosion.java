/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class S27PacketExplosion
implements Packet {
    private double field_149158_a;
    private double field_149156_b;
    private double field_149157_c;
    private float field_149154_d;
    private List field_149155_e;
    public float motionX;
    public float motionY;
    public float motionZ;
    private static final String __OBFID = "CL_00001300";

    public S27PacketExplosion() {
    }

    public S27PacketExplosion(double p_i45193_1_, double p_i45193_3_, double p_i45193_5_, float p_i45193_7_, List p_i45193_8_, Vec3 p_i45193_9_) {
        this.field_149158_a = p_i45193_1_;
        this.field_149156_b = p_i45193_3_;
        this.field_149157_c = p_i45193_5_;
        this.field_149154_d = p_i45193_7_;
        this.field_149155_e = Lists.newArrayList((Iterable)p_i45193_8_);
        if (p_i45193_9_ != null) {
            this.motionX = (float)p_i45193_9_.xCoord;
            this.motionY = (float)p_i45193_9_.yCoord;
            this.motionZ = (float)p_i45193_9_.zCoord;
        }
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.field_149158_a = data.readFloat();
        this.field_149156_b = data.readFloat();
        this.field_149157_c = data.readFloat();
        this.field_149154_d = data.readFloat();
        int var2 = data.readInt();
        this.field_149155_e = Lists.newArrayListWithCapacity((int)var2);
        int var3 = (int)this.field_149158_a;
        int var4 = (int)this.field_149156_b;
        int var5 = (int)this.field_149157_c;
        for (int var6 = 0; var6 < var2; ++var6) {
            int var7 = data.readByte() + var3;
            int var8 = data.readByte() + var4;
            int var9 = data.readByte() + var5;
            this.field_149155_e.add(new BlockPos(var7, var8, var9));
        }
        this.motionX = data.readFloat();
        this.motionY = data.readFloat();
        this.motionZ = data.readFloat();
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeFloat((float)this.field_149158_a);
        data.writeFloat((float)this.field_149156_b);
        data.writeFloat((float)this.field_149157_c);
        data.writeFloat(this.field_149154_d);
        data.writeInt(this.field_149155_e.size());
        int var2 = (int)this.field_149158_a;
        int var3 = (int)this.field_149156_b;
        int var4 = (int)this.field_149157_c;
        for (BlockPos var6 : this.field_149155_e) {
            int var7 = var6.getX() - var2;
            int var8 = var6.getY() - var3;
            int var9 = var6.getZ() - var4;
            data.writeByte(var7);
            data.writeByte(var8);
            data.writeByte(var9);
        }
        data.writeFloat(this.motionX);
        data.writeFloat(this.motionY);
        data.writeFloat(this.motionZ);
    }

    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleExplosion(this);
    }

    public float getMotionX() {
        return this.motionX;
    }

    public float getMotionY() {
        return this.motionY;
    }

    public float getMotionZ() {
        return this.motionZ;
    }

    public double func_149148_f() {
        return this.field_149158_a;
    }

    public double func_149143_g() {
        return this.field_149156_b;
    }

    public double func_149145_h() {
        return this.field_149157_c;
    }

    public float func_149146_i() {
        return this.field_149154_d;
    }

    public List func_149150_j() {
        return this.field_149155_e;
    }

    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}

