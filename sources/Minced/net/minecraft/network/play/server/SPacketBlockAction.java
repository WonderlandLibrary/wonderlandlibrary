// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketBlockAction implements Packet<INetHandlerPlayClient>
{
    private BlockPos blockPosition;
    private int instrument;
    private int pitch;
    private Block block;
    
    public SPacketBlockAction() {
    }
    
    public SPacketBlockAction(final BlockPos pos, final Block blockIn, final int instrumentIn, final int pitchIn) {
        this.blockPosition = pos;
        this.instrument = instrumentIn;
        this.pitch = pitchIn;
        this.block = blockIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.blockPosition = buf.readBlockPos();
        this.instrument = buf.readUnsignedByte();
        this.pitch = buf.readUnsignedByte();
        this.block = Block.getBlockById(buf.readVarInt() & 0xFFF);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.blockPosition);
        buf.writeByte(this.instrument);
        buf.writeByte(this.pitch);
        buf.writeVarInt(Block.getIdFromBlock(this.block) & 0xFFF);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleBlockAction(this);
    }
    
    public BlockPos getBlockPosition() {
        return this.blockPosition;
    }
    
    public int getData1() {
        return this.instrument;
    }
    
    public int getData2() {
        return this.pitch;
    }
    
    public Block getBlockType() {
        return this.block;
    }
}
