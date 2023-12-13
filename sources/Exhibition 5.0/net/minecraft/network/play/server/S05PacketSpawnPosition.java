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

public class S05PacketSpawnPosition implements Packet
{
    private BlockPos field_179801_a;
    private static final String __OBFID = "CL_00001336";
    
    public S05PacketSpawnPosition() {
    }
    
    public S05PacketSpawnPosition(final BlockPos p_i45956_1_) {
        this.field_179801_a = p_i45956_1_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_179801_a = data.readBlockPos();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeBlockPos(this.field_179801_a);
    }
    
    public void func_180752_a(final INetHandlerPlayClient p_180752_1_) {
        p_180752_1_.handleSpawnPosition(this);
    }
    
    public BlockPos func_179800_a() {
        return this.field_179801_a;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180752_a((INetHandlerPlayClient)handler);
    }
}
