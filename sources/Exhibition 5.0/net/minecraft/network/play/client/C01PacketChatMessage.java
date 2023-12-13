// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayServer;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class C01PacketChatMessage implements Packet
{
    private String message;
    private static final String __OBFID = "CL_00001347";
    
    public C01PacketChatMessage() {
    }
    
    public C01PacketChatMessage(String messageIn) {
        if (messageIn.length() > 100) {
            messageIn = messageIn.substring(0, 100);
        }
        this.message = messageIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.message = data.readStringFromBuffer(100);
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeString(this.message);
    }
    
    public void func_180757_a(final INetHandlerPlayServer p_180757_1_) {
        p_180757_1_.processChatMessage(this);
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180757_a((INetHandlerPlayServer)handler);
    }
}
