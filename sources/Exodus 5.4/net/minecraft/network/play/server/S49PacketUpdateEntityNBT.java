/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S49PacketUpdateEntityNBT
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private NBTTagCompound tagCompound;

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readVarIntFromBuffer();
        this.tagCompound = packetBuffer.readNBTTagCompoundFromBuffer();
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityId);
        packetBuffer.writeNBTTagCompoundToBuffer(this.tagCompound);
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleEntityNBT(this);
    }

    public NBTTagCompound getTagCompound() {
        return this.tagCompound;
    }

    public Entity getEntity(World world) {
        return world.getEntityByID(this.entityId);
    }

    public S49PacketUpdateEntityNBT() {
    }

    public S49PacketUpdateEntityNBT(int n, NBTTagCompound nBTTagCompound) {
        this.entityId = n;
        this.tagCompound = nBTTagCompound;
    }
}

