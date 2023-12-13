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

public class S47PacketPlayerListHeaderFooter implements Packet
{
    private IChatComponent field_179703_a;
    private IChatComponent field_179702_b;
    private static final String __OBFID = "CL_00002285";
    
    public S47PacketPlayerListHeaderFooter() {
    }
    
    public S47PacketPlayerListHeaderFooter(final IChatComponent p_i45950_1_) {
        this.field_179703_a = p_i45950_1_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_179703_a = data.readChatComponent();
        this.field_179702_b = data.readChatComponent();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeChatComponent(this.field_179703_a);
        data.writeChatComponent(this.field_179702_b);
    }
    
    public void func_179699_a(final INetHandlerPlayClient p_179699_1_) {
        p_179699_1_.func_175096_a(this);
    }
    
    public IChatComponent func_179700_a() {
        return this.field_179703_a;
    }
    
    public IChatComponent func_179701_b() {
        return this.field_179702_b;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_179699_a((INetHandlerPlayClient)handler);
    }
}
