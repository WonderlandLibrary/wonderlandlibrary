/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S30PacketWindowItems
implements Packet<INetHandlerPlayClient> {
    private int windowId;
    private ItemStack[] itemStacks;

    public S30PacketWindowItems() {
    }

    public S30PacketWindowItems(int windowIdIn, List<ItemStack> p_i45186_2_) {
        this.windowId = windowIdIn;
        this.itemStacks = new ItemStack[p_i45186_2_.size()];
        int i = 0;
        while (i < this.itemStacks.length) {
            ItemStack itemstack = p_i45186_2_.get(i);
            this.itemStacks[i] = itemstack == null ? null : itemstack.copy();
            ++i;
        }
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
        int i = buf.readShort();
        this.itemStacks = new ItemStack[i];
        int j = 0;
        while (j < i) {
            this.itemStacks[j] = buf.readItemStackFromBuffer();
            ++j;
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.itemStacks.length);
        ItemStack[] itemStackArray = this.itemStacks;
        int n = itemStackArray.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack itemstack = itemStackArray[n2];
            buf.writeItemStackToBuffer(itemstack);
            ++n2;
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleWindowItems(this);
    }

    public int func_148911_c() {
        return this.windowId;
    }

    public ItemStack[] getItemStacks() {
        return this.itemStacks;
    }
}

