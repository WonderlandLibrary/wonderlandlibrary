/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S2DPacketOpenWindow
implements Packet<INetHandlerPlayClient> {
    private int windowId;
    private String inventoryType;
    private IChatComponent windowTitle;
    private int entityId;
    private int slotCount;

    public S2DPacketOpenWindow() {
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleOpenWindow(this);
    }

    public String getGuiId() {
        return this.inventoryType;
    }

    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.windowId = packetBuffer.readUnsignedByte();
        this.inventoryType = packetBuffer.readStringFromBuffer(32);
        this.windowTitle = packetBuffer.readChatComponent();
        this.slotCount = packetBuffer.readUnsignedByte();
        if (this.inventoryType.equals("EntityHorse")) {
            this.entityId = packetBuffer.readInt();
        }
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeByte(this.windowId);
        packetBuffer.writeString(this.inventoryType);
        packetBuffer.writeChatComponent(this.windowTitle);
        packetBuffer.writeByte(this.slotCount);
        if (this.inventoryType.equals("EntityHorse")) {
            packetBuffer.writeInt(this.entityId);
        }
    }

    public S2DPacketOpenWindow(int n, String string, IChatComponent iChatComponent, int n2) {
        this.windowId = n;
        this.inventoryType = string;
        this.windowTitle = iChatComponent;
        this.slotCount = n2;
    }

    public boolean hasSlots() {
        return this.slotCount > 0;
    }

    public S2DPacketOpenWindow(int n, String string, IChatComponent iChatComponent, int n2, int n3) {
        this(n, string, iChatComponent, n2);
        this.entityId = n3;
    }

    public int getSlotCount() {
        return this.slotCount;
    }

    public IChatComponent getWindowTitle() {
        return this.windowTitle;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public S2DPacketOpenWindow(int n, String string, IChatComponent iChatComponent) {
        this(n, string, iChatComponent, 0);
    }
}

