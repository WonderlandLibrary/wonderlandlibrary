// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketUpdateSign implements Packet<INetHandlerPlayServer>
{
    private BlockPos pos;
    private String[] lines;
    
    public CPacketUpdateSign() {
    }
    
    public CPacketUpdateSign(final BlockPos posIn, final ITextComponent[] linesIn) {
        this.pos = posIn;
        this.lines = new String[] { linesIn[0].getUnformattedText(), linesIn[1].getUnformattedText(), linesIn[2].getUnformattedText(), linesIn[3].getUnformattedText() };
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.pos = buf.readBlockPos();
        this.lines = new String[4];
        for (int i = 0; i < 4; ++i) {
            this.lines[i] = buf.readString(384);
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.pos);
        for (int i = 0; i < 4; ++i) {
            buf.writeString(this.lines[i]);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processUpdateSign(this);
    }
    
    public BlockPos getPosition() {
        return this.pos;
    }
    
    public String[] getLines() {
        return this.lines;
    }
}
