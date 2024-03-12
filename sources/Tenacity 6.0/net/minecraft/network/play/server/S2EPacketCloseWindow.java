// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S2EPacketCloseWindow implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    
    public S2EPacketCloseWindow() {
    }
    
    public S2EPacketCloseWindow(final int windowIdIn) {
        this.windowId = windowIdIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
            handler.handleCloseWindow(this);
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
    }
    
    @Override
    public int getID() {
        return 28;
    }
}
