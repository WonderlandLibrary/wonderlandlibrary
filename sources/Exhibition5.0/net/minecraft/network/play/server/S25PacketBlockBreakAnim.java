// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;

public class S25PacketBlockBreakAnim implements Packet
{
    private int breakerId;
    private BlockPos position;
    private int progress;
    private static final String __OBFID = "CL_00001284";
    
    public S25PacketBlockBreakAnim() {
    }
    
    public S25PacketBlockBreakAnim(final int breakerId, final BlockPos pos, final int progress) {
        this.breakerId = breakerId;
        this.position = pos;
        this.progress = progress;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.breakerId = data.readVarIntFromBuffer();
        this.position = data.readBlockPos();
        this.progress = data.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.breakerId);
        data.writeBlockPos(this.position);
        data.writeByte(this.progress);
    }
    
    public void handle(final INetHandlerPlayClient handler) {
        handler.handleBlockBreakAnim(this);
    }
    
    public int func_148845_c() {
        return this.breakerId;
    }
    
    public BlockPos func_179821_b() {
        return this.position;
    }
    
    public int func_148846_g() {
        return this.progress;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.handle((INetHandlerPlayClient)handler);
    }
}
