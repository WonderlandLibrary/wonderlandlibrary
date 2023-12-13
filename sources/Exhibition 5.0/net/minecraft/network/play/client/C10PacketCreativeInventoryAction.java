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

public class C10PacketCreativeInventoryAction implements Packet
{
    private int slotId;
    private ItemStack stack;
    private static final String __OBFID = "CL_00001369";
    
    public C10PacketCreativeInventoryAction() {
    }
    
    public C10PacketCreativeInventoryAction(final int p_i45263_1_, final ItemStack p_i45263_2_) {
        this.slotId = p_i45263_1_;
        this.stack = ((p_i45263_2_ != null) ? p_i45263_2_.copy() : null);
    }
    
    public void func_180767_a(final INetHandlerPlayServer p_180767_1_) {
        p_180767_1_.processCreativeInventoryAction(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.slotId = data.readShort();
        this.stack = data.readItemStackFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeShort(this.slotId);
        data.writeItemStackToBuffer(this.stack);
    }
    
    public int getSlotId() {
        return this.slotId;
    }
    
    public ItemStack getStack() {
        return this.stack;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180767_a((INetHandlerPlayServer)handler);
    }
}
