// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.Packet;

public class S40PacketDisconnect implements Packet
{
    private IChatComponent reason;
    private static final String __OBFID = "CL_00001298";
    
    public S40PacketDisconnect() {
    }
    
    public S40PacketDisconnect(final IChatComponent reasonIn) {
        this.reason = reasonIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.reason = data.readChatComponent();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeChatComponent(this.reason);
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleDisconnect(this);
    }
    
    public IChatComponent func_149165_c() {
        return this.reason;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
