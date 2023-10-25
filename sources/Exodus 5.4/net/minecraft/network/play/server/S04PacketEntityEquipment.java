/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S04PacketEntityEquipment
implements Packet<INetHandlerPlayClient> {
    private int entityID;
    private ItemStack itemStack;
    private int equipmentSlot;

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public int getEquipmentSlot() {
        return this.equipmentSlot;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleEntityEquipment(this);
    }

    public S04PacketEntityEquipment() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityID);
        packetBuffer.writeShort(this.equipmentSlot);
        packetBuffer.writeItemStackToBuffer(this.itemStack);
    }

    public int getEntityID() {
        return this.entityID;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityID = packetBuffer.readVarIntFromBuffer();
        this.equipmentSlot = packetBuffer.readShort();
        this.itemStack = packetBuffer.readItemStackFromBuffer();
    }

    public S04PacketEntityEquipment(int n, int n2, ItemStack itemStack) {
        this.entityID = n;
        this.equipmentSlot = n2;
        this.itemStack = itemStack == null ? null : itemStack.copy();
    }
}

