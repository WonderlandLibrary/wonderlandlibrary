// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C11PacketEnchantItem implements Packet
{
    private int id;
    private int button;
    private static final String __OBFID = "CL_00001352";
    
    public C11PacketEnchantItem() {
    }
    
    public C11PacketEnchantItem(final int p_i45245_1_, final int p_i45245_2_) {
        this.id = p_i45245_1_;
        this.button = p_i45245_2_;
    }
    
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processEnchantItem(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.id = data.readByte();
        this.button = data.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.id);
        data.writeByte(this.button);
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getButton() {
        return this.button;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
