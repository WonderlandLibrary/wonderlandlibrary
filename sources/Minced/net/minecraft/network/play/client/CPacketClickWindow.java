// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketClickWindow implements Packet<INetHandlerPlayServer>
{
    private int windowId;
    private int slotId;
    private int packedClickData;
    private short actionNumber;
    private ItemStack clickedItem;
    private ClickType mode;
    
    public CPacketClickWindow() {
        this.clickedItem = ItemStack.EMPTY;
    }
    
    public CPacketClickWindow(final int windowIdIn, final int slotIdIn, final int usedButtonIn, final ClickType modeIn, final ItemStack clickedItemIn, final short actionNumberIn) {
        this.clickedItem = ItemStack.EMPTY;
        this.windowId = windowIdIn;
        this.slotId = slotIdIn;
        this.packedClickData = usedButtonIn;
        this.clickedItem = clickedItemIn.copy();
        this.actionNumber = actionNumberIn;
        this.mode = modeIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processClickWindow(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readByte();
        this.slotId = buf.readShort();
        this.packedClickData = buf.readByte();
        this.actionNumber = buf.readShort();
        this.mode = buf.readEnumValue(ClickType.class);
        this.clickedItem = buf.readItemStack();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.slotId);
        buf.writeByte(this.packedClickData);
        buf.writeShort(this.actionNumber);
        buf.writeEnumValue(this.mode);
        buf.writeItemStack(this.clickedItem);
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public int getSlotId() {
        return this.slotId;
    }
    
    public int getUsedButton() {
        return this.packedClickData;
    }
    
    public short getActionNumber() {
        return this.actionNumber;
    }
    
    public ItemStack getClickedItem() {
        return this.clickedItem;
    }
    
    public ClickType getClickType() {
        return this.mode;
    }
}
