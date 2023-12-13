// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.Packet;

public class S2DPacketOpenWindow implements Packet
{
    private int windowId;
    private String inventoryType;
    private IChatComponent windowTitle;
    private int slotCount;
    private int entityId;
    private static final String __OBFID = "CL_00001293";
    
    public S2DPacketOpenWindow() {
    }
    
    public S2DPacketOpenWindow(final int p_i45981_1_, final String p_i45981_2_, final IChatComponent p_i45981_3_) {
        this(p_i45981_1_, p_i45981_2_, p_i45981_3_, 0);
    }
    
    public S2DPacketOpenWindow(final int p_i45982_1_, final String p_i45982_2_, final IChatComponent p_i45982_3_, final int p_i45982_4_) {
        this.windowId = p_i45982_1_;
        this.inventoryType = p_i45982_2_;
        this.windowTitle = p_i45982_3_;
        this.slotCount = p_i45982_4_;
    }
    
    public S2DPacketOpenWindow(final int p_i45983_1_, final String p_i45983_2_, final IChatComponent p_i45983_3_, final int p_i45983_4_, final int p_i45983_5_) {
        this(p_i45983_1_, p_i45983_2_, p_i45983_3_, p_i45983_4_);
        this.entityId = p_i45983_5_;
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleOpenWindow(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.windowId = data.readUnsignedByte();
        this.inventoryType = data.readStringFromBuffer(32);
        this.windowTitle = data.readChatComponent();
        this.slotCount = data.readUnsignedByte();
        if (this.inventoryType.equals("EntityHorse")) {
            this.entityId = data.readInt();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.windowId);
        data.writeString(this.inventoryType);
        data.writeChatComponent(this.windowTitle);
        data.writeByte(this.slotCount);
        if (this.inventoryType.equals("EntityHorse")) {
            data.writeInt(this.entityId);
        }
    }
    
    public int func_148901_c() {
        return this.windowId;
    }
    
    public String func_148902_e() {
        return this.inventoryType;
    }
    
    public IChatComponent func_179840_c() {
        return this.windowTitle;
    }
    
    public int func_148898_f() {
        return this.slotCount;
    }
    
    public int func_148897_h() {
        return this.entityId;
    }
    
    public boolean func_148900_g() {
        return this.slotCount > 0;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
