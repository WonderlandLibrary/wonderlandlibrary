/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class S27PacketExplosion
implements Packet<INetHandlerPlayClient> {
    private double posX;
    private double posY;
    private double posZ;
    private float strength;
    private List<BlockPos> affectedBlockPositions;
    private float field_149152_f;
    private float field_149153_g;
    private float field_149159_h;

    public S27PacketExplosion() {
    }

    public S27PacketExplosion(double p_i45193_1_, double y2, double z2, float strengthIn, List<BlockPos> affectedBlocksIn, Vec3 p_i45193_9_) {
        this.posX = p_i45193_1_;
        this.posY = y2;
        this.posZ = z2;
        this.strength = strengthIn;
        this.affectedBlockPositions = Lists.newArrayList(affectedBlocksIn);
        if (p_i45193_9_ != null) {
            this.field_149152_f = (float)p_i45193_9_.xCoord;
            this.field_149153_g = (float)p_i45193_9_.yCoord;
            this.field_149159_h = (float)p_i45193_9_.zCoord;
        }
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.posX = buf.readFloat();
        this.posY = buf.readFloat();
        this.posZ = buf.readFloat();
        this.strength = buf.readFloat();
        int i2 = buf.readInt();
        this.affectedBlockPositions = Lists.newArrayListWithCapacity(i2);
        int j2 = (int)this.posX;
        int k2 = (int)this.posY;
        int l2 = (int)this.posZ;
        for (int i1 = 0; i1 < i2; ++i1) {
            int j1 = buf.readByte() + j2;
            int k1 = buf.readByte() + k2;
            int l1 = buf.readByte() + l2;
            this.affectedBlockPositions.add(new BlockPos(j1, k1, l1));
        }
        this.field_149152_f = buf.readFloat();
        this.field_149153_g = buf.readFloat();
        this.field_149159_h = buf.readFloat();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeFloat((float)this.posX);
        buf.writeFloat((float)this.posY);
        buf.writeFloat((float)this.posZ);
        buf.writeFloat(this.strength);
        buf.writeInt(this.affectedBlockPositions.size());
        int i2 = (int)this.posX;
        int j2 = (int)this.posY;
        int k2 = (int)this.posZ;
        for (BlockPos blockpos : this.affectedBlockPositions) {
            int l2 = blockpos.getX() - i2;
            int i1 = blockpos.getY() - j2;
            int j1 = blockpos.getZ() - k2;
            buf.writeByte(l2);
            buf.writeByte(i1);
            buf.writeByte(j1);
        }
        buf.writeFloat(this.field_149152_f);
        buf.writeFloat(this.field_149153_g);
        buf.writeFloat(this.field_149159_h);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleExplosion(this);
    }

    public float func_149149_c() {
        return this.field_149152_f;
    }

    public float func_149144_d() {
        return this.field_149153_g;
    }

    public float func_149147_e() {
        return this.field_149159_h;
    }

    public double getX() {
        return this.posX;
    }

    public double getY() {
        return this.posY;
    }

    public double getZ() {
        return this.posZ;
    }

    public float getStrength() {
        return this.strength;
    }

    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }
}

