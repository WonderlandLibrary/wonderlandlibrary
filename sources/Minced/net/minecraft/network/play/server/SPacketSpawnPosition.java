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

public class SPacketSpawnPosition implements Packet<INetHandlerPlayClient>
{
    private BlockPos spawnBlockPos;
    
    public SPacketSpawnPosition() {
    }
    
    public SPacketSpawnPosition(final BlockPos posIn) {
        this.spawnBlockPos = posIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.spawnBlockPos = buf.readBlockPos();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.spawnBlockPos);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSpawnPosition(this);
    }
    
    public BlockPos getSpawnPos() {
        return this.spawnBlockPos;
    }
}
