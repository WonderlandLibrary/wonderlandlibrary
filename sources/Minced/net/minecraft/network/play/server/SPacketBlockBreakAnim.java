// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketBlockBreakAnim implements Packet<INetHandlerPlayClient>
{
    private int breakerId;
    private BlockPos position;
    private int progress;
    
    public SPacketBlockBreakAnim() {
    }
    
    public SPacketBlockBreakAnim(final int breakerIdIn, final BlockPos positionIn, final int progressIn) {
        this.breakerId = breakerIdIn;
        this.position = positionIn;
        this.progress = progressIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.breakerId = buf.readVarInt();
        this.position = buf.readBlockPos();
        this.progress = buf.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.breakerId);
        buf.writeBlockPos(this.position);
        buf.writeByte(this.progress);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleBlockBreakAnim(this);
    }
    
    public int getBreakerId() {
        return this.breakerId;
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public int getProgress() {
        return this.progress;
    }
}
