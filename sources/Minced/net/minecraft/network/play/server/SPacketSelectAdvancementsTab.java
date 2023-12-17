// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketSelectAdvancementsTab implements Packet<INetHandlerPlayClient>
{
    @Nullable
    private ResourceLocation tab;
    
    public SPacketSelectAdvancementsTab() {
    }
    
    public SPacketSelectAdvancementsTab(@Nullable final ResourceLocation p_i47596_1_) {
        this.tab = p_i47596_1_;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSelectAdvancementsTab(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        if (buf.readBoolean()) {
            this.tab = buf.readResourceLocation();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBoolean(this.tab != null);
        if (this.tab != null) {
            buf.writeResourceLocation(this.tab);
        }
    }
    
    @Nullable
    public ResourceLocation getTab() {
        return this.tab;
    }
}
