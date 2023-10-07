// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;

public class C0EPacketClickWindow implements Packet
{
    private int windowId;
    private int slotId;
    private int usedButton;
    private short actionNumber;
    private ItemStack clickedItem;
    private int mode;
    private static final String __OBFID = "CL_00001353";
    
    public C0EPacketClickWindow() {
    }
    
    public C0EPacketClickWindow(final int p_i45246_1_, final int p_i45246_2_, final int p_i45246_3_, final int p_i45246_4_, final ItemStack p_i45246_5_, final short p_i45246_6_) {
        this.windowId = p_i45246_1_;
        this.slotId = p_i45246_2_;
        this.usedButton = p_i45246_3_;
        this.clickedItem = ((p_i45246_5_ != null) ? p_i45246_5_.copy() : null);
        this.actionNumber = p_i45246_6_;
        this.mode = p_i45246_4_;
    }
    
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processClickWindow(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.windowId = data.readByte();
        this.slotId = data.readShort();
        this.usedButton = data.readByte();
        this.actionNumber = data.readShort();
        this.mode = data.readByte();
        this.clickedItem = data.readItemStackFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.windowId);
        data.writeShort(this.slotId);
        data.writeByte(this.usedButton);
        data.writeShort(this.actionNumber);
        data.writeByte(this.mode);
        data.writeItemStackToBuffer(this.clickedItem);
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public int getSlotId() {
        return this.slotId;
    }
    
    public int getUsedButton() {
        return this.usedButton;
    }
    
    public short getActionNumber() {
        return this.actionNumber;
    }
    
    public ItemStack getClickedItem() {
        return this.clickedItem;
    }
    
    public int getMode() {
        return this.mode;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
