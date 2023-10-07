// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.world.World;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;

public class S0APacketUseBed implements Packet
{
    private int playerID;
    private BlockPos field_179799_b;
    private static final String __OBFID = "CL_00001319";
    
    public S0APacketUseBed() {
    }
    
    public S0APacketUseBed(final EntityPlayer p_i45964_1_, final BlockPos p_i45964_2_) {
        this.playerID = p_i45964_1_.getEntityId();
        this.field_179799_b = p_i45964_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.playerID = data.readVarIntFromBuffer();
        this.field_179799_b = data.readBlockPos();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.playerID);
        data.writeBlockPos(this.field_179799_b);
    }
    
    public void func_180744_a(final INetHandlerPlayClient p_180744_1_) {
        p_180744_1_.handleUseBed(this);
    }
    
    public EntityPlayer getPlayer(final World worldIn) {
        return (EntityPlayer)worldIn.getEntityByID(this.playerID);
    }
    
    public BlockPos func_179798_a() {
        return this.field_179799_b;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180744_a((INetHandlerPlayClient)handler);
    }
}
