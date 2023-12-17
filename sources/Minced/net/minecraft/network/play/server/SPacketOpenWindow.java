// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketOpenWindow implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private String inventoryType;
    private ITextComponent windowTitle;
    private int slotCount;
    private int entityId;
    
    public SPacketOpenWindow() {
    }
    
    public SPacketOpenWindow(final int windowIdIn, final String inventoryTypeIn, final ITextComponent windowTitleIn) {
        this(windowIdIn, inventoryTypeIn, windowTitleIn, 0);
    }
    
    public SPacketOpenWindow(final int windowIdIn, final String inventoryTypeIn, final ITextComponent windowTitleIn, final int slotCountIn) {
        this.windowId = windowIdIn;
        this.inventoryType = inventoryTypeIn;
        this.windowTitle = windowTitleIn;
        this.slotCount = slotCountIn;
    }
    
    public SPacketOpenWindow(final int windowIdIn, final String inventoryTypeIn, final ITextComponent windowTitleIn, final int slotCountIn, final int entityIdIn) {
        this(windowIdIn, inventoryTypeIn, windowTitleIn, slotCountIn);
        this.entityId = entityIdIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleOpenWindow(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
        this.inventoryType = buf.readString(32);
        this.windowTitle = buf.readTextComponent();
        this.slotCount = buf.readUnsignedByte();
        if (this.inventoryType.equals("EntityHorse")) {
            this.entityId = buf.readInt();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeString(this.inventoryType);
        buf.writeTextComponent(this.windowTitle);
        buf.writeByte(this.slotCount);
        if (this.inventoryType.equals("EntityHorse")) {
            buf.writeInt(this.entityId);
        }
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public String getGuiId() {
        return this.inventoryType;
    }
    
    public ITextComponent getWindowTitle() {
        return this.windowTitle;
    }
    
    public int getSlotCount() {
        return this.slotCount;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public boolean hasSlots() {
        return this.slotCount > 0;
    }
}
