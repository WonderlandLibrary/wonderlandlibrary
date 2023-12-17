// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketWindowItems implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private List<ItemStack> itemStacks;
    
    public SPacketWindowItems() {
    }
    
    public SPacketWindowItems(final int p_i47317_1_, final NonNullList<ItemStack> p_i47317_2_) {
        this.windowId = p_i47317_1_;
        this.itemStacks = NonNullList.withSize(p_i47317_2_.size(), ItemStack.EMPTY);
        for (int i = 0; i < this.itemStacks.size(); ++i) {
            final ItemStack itemstack = p_i47317_2_.get(i);
            this.itemStacks.set(i, itemstack.copy());
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
        final int i = buf.readShort();
        this.itemStacks = NonNullList.withSize(i, ItemStack.EMPTY);
        for (int j = 0; j < i; ++j) {
            this.itemStacks.set(j, buf.readItemStack());
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.itemStacks.size());
        for (final ItemStack itemstack : this.itemStacks) {
            buf.writeItemStack(itemstack);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleWindowItems(this);
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public List<ItemStack> getItemStacks() {
        return this.itemStacks;
    }
}
