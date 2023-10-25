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
    private ItemStack[] itemStacks;
    private int windowId;

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeByte(this.windowId);
        packetBuffer.writeShort(this.itemStacks.length);
        ItemStack[] itemStackArray = this.itemStacks;
        int n = this.itemStacks.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack itemStack = itemStackArray[n2];
            packetBuffer.writeItemStackToBuffer(itemStack);
            ++n2;
        }
    }

    public S30PacketWindowItems(int n, List<ItemStack> list) {
        this.windowId = n;
        this.itemStacks = new ItemStack[list.size()];
        int n2 = 0;
        while (n2 < this.itemStacks.length) {
            ItemStack itemStack = list.get(n2);
            this.itemStacks[n2] = itemStack == null ? null : itemStack.copy();
            ++n2;
        }
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.windowId = packetBuffer.readUnsignedByte();
        int n = packetBuffer.readShort();
        this.itemStacks = new ItemStack[n];
        int n2 = 0;
        while (n2 < n) {
            this.itemStacks[n2] = packetBuffer.readItemStackFromBuffer();
            ++n2;
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleWindowItems(this);
    }

    public S30PacketWindowItems() {
    }

    public ItemStack[] getItemStacks() {
        return this.itemStacks;
    }

    public int func_148911_c() {
        return this.windowId;
    }
}

