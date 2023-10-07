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

public class S02PacketChat implements Packet
{
    private IChatComponent chatComponent;
    private byte field_179842_b;
    private static final String __OBFID = "CL_00001289";
    
    public S02PacketChat() {
    }
    
    public S02PacketChat(final IChatComponent component) {
        this(component, (byte)1);
    }
    
    public S02PacketChat(final IChatComponent p_i45986_1_, final byte p_i45986_2_) {
        this.chatComponent = p_i45986_1_;
        this.field_179842_b = p_i45986_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.chatComponent = data.readChatComponent();
        this.field_179842_b = data.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeChatComponent(this.chatComponent);
        data.writeByte(this.field_179842_b);
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleChat(this);
    }
    
    public IChatComponent func_148915_c() {
        return this.chatComponent;
    }
    
    public boolean isChat() {
        return this.field_179842_b == 1 || this.field_179842_b == 2;
    }
    
    public byte func_179841_c() {
        return this.field_179842_b;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
