// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketEntityEquipment implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private EntityEquipmentSlot equipmentSlot;
    private ItemStack itemStack;
    
    public SPacketEntityEquipment() {
        this.itemStack = ItemStack.EMPTY;
    }
    
    public SPacketEntityEquipment(final int entityIdIn, final EntityEquipmentSlot equipmentSlotIn, final ItemStack itemStackIn) {
        this.itemStack = ItemStack.EMPTY;
        this.entityID = entityIdIn;
        this.equipmentSlot = equipmentSlotIn;
        this.itemStack = itemStackIn.copy();
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityID = buf.readVarInt();
        this.equipmentSlot = buf.readEnumValue(EntityEquipmentSlot.class);
        this.itemStack = buf.readItemStack();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityID);
        buf.writeEnumValue(this.equipmentSlot);
        buf.writeItemStack(this.itemStack);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityEquipment(this);
    }
    
    public ItemStack getItemStack() {
        return this.itemStack;
    }
    
    public int getEntityID() {
        return this.entityID;
    }
    
    public EntityEquipmentSlot getEquipmentSlot() {
        return this.equipmentSlot;
    }
}
