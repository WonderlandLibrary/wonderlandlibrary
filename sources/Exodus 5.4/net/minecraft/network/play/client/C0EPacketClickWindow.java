/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0EPacketClickWindow
implements Packet<INetHandlerPlayServer> {
    private int usedButton;
    private int slotId;
    private int windowId;
    private short actionNumber;
    private ItemStack clickedItem;
    private int mode;

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.windowId = packetBuffer.readByte();
        this.slotId = packetBuffer.readShort();
        this.usedButton = packetBuffer.readByte();
        this.actionNumber = packetBuffer.readShort();
        this.mode = packetBuffer.readByte();
        this.clickedItem = packetBuffer.readItemStackFromBuffer();
    }

    public C0EPacketClickWindow() {
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeByte(this.windowId);
        packetBuffer.writeShort(this.slotId);
        packetBuffer.writeByte(this.usedButton);
        packetBuffer.writeShort(this.actionNumber);
        packetBuffer.writeByte(this.mode);
        packetBuffer.writeItemStackToBuffer(this.clickedItem);
    }

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processClickWindow(this);
    }

    public ItemStack getClickedItem() {
        return this.clickedItem;
    }

    public int getMode() {
        return this.mode;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public C0EPacketClickWindow(int n, int n2, int n3, int n4, ItemStack itemStack, short s) {
        this.windowId = n;
        this.slotId = n2;
        this.usedButton = n3;
        this.clickedItem = itemStack != null ? itemStack.copy() : null;
        this.actionNumber = s;
        this.mode = n4;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public short getActionNumber() {
        return this.actionNumber;
    }

    public int getUsedButton() {
        return this.usedButton;
    }
}

