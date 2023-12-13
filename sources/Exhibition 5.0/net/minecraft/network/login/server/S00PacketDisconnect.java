// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.login.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.login.INetHandlerLoginClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.Packet;

public class S00PacketDisconnect implements Packet
{
    private IChatComponent reason;
    private static final String __OBFID = "CL_00001377";
    
    public S00PacketDisconnect() {
    }
    
    public S00PacketDisconnect(final IChatComponent reasonIn) {
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
    
    public void func_180772_a(final INetHandlerLoginClient p_180772_1_) {
        p_180772_1_.handleDisconnect(this);
    }
    
    public IChatComponent func_149603_c() {
        return this.reason;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180772_a((INetHandlerLoginClient)handler);
    }
}
