/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server.window;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;

public class ServerWindowItemsPacket
extends MinecraftPacket {
    private int windowId;
    private ItemStack[] items;

    private ServerWindowItemsPacket() {
    }

    public ServerWindowItemsPacket(int windowId, ItemStack[] items) {
        this.windowId = windowId;
        this.items = items;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public ItemStack[] getItems() {
        return this.items;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.items = new ItemStack[in.readShort()];
        int index = 0;
        while (index < this.items.length) {
            this.items[index] = NetUtil.readItem(in);
            ++index;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.items.length);
        ItemStack[] itemStackArray = this.items;
        int n = this.items.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack item = itemStackArray[n2];
            NetUtil.writeItem(out, item);
            ++n2;
        }
    }
}

