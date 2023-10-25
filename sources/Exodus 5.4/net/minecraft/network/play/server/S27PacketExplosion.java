/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
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
    private float strength;
    private double posZ;
    private float field_149153_g;
    private List<BlockPos> affectedBlockPositions;
    private float field_149159_h;
    private float field_149152_f;
    private double posY;

    public float getStrength() {
        return this.strength;
    }

    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleExplosion(this);
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

    public double getY() {
        return this.posY;
    }

    public double getZ() {
        return this.posZ;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeFloat((float)this.posX);
        packetBuffer.writeFloat((float)this.posY);
        packetBuffer.writeFloat((float)this.posZ);
        packetBuffer.writeFloat(this.strength);
        packetBuffer.writeInt(this.affectedBlockPositions.size());
        int n = (int)this.posX;
        int n2 = (int)this.posY;
        int n3 = (int)this.posZ;
        for (BlockPos blockPos : this.affectedBlockPositions) {
            int n4 = blockPos.getX() - n;
            int n5 = blockPos.getY() - n2;
            int n6 = blockPos.getZ() - n3;
            packetBuffer.writeByte(n4);
            packetBuffer.writeByte(n5);
            packetBuffer.writeByte(n6);
        }
        packetBuffer.writeFloat(this.field_149152_f);
        packetBuffer.writeFloat(this.field_149153_g);
        packetBuffer.writeFloat(this.field_149159_h);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.posX = packetBuffer.readFloat();
        this.posY = packetBuffer.readFloat();
        this.posZ = packetBuffer.readFloat();
        this.strength = packetBuffer.readFloat();
        int n = packetBuffer.readInt();
        this.affectedBlockPositions = Lists.newArrayListWithCapacity((int)n);
        int n2 = (int)this.posX;
        int n3 = (int)this.posY;
        int n4 = (int)this.posZ;
        int n5 = 0;
        while (n5 < n) {
            int n6 = packetBuffer.readByte() + n2;
            int n7 = packetBuffer.readByte() + n3;
            int n8 = packetBuffer.readByte() + n4;
            this.affectedBlockPositions.add(new BlockPos(n6, n7, n8));
            ++n5;
        }
        this.field_149152_f = packetBuffer.readFloat();
        this.field_149153_g = packetBuffer.readFloat();
        this.field_149159_h = packetBuffer.readFloat();
    }

    public double getX() {
        return this.posX;
    }

    public S27PacketExplosion() {
    }

    public S27PacketExplosion(double d, double d2, double d3, float f, List<BlockPos> list, Vec3 vec3) {
        this.posX = d;
        this.posY = d2;
        this.posZ = d3;
        this.strength = f;
        this.affectedBlockPositions = Lists.newArrayList(list);
        if (vec3 != null) {
            this.field_149152_f = (float)vec3.xCoord;
            this.field_149153_g = (float)vec3.yCoord;
            this.field_149159_h = (float)vec3.zCoord;
        }
    }
}

