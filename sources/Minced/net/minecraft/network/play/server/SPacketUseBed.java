// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.world.World;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketUseBed implements Packet<INetHandlerPlayClient>
{
    private int playerID;
    private BlockPos bedPos;
    
    public SPacketUseBed() {
    }
    
    public SPacketUseBed(final EntityPlayer player, final BlockPos posIn) {
        this.playerID = player.getEntityId();
        this.bedPos = posIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.playerID = buf.readVarInt();
        this.bedPos = buf.readBlockPos();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.playerID);
        buf.writeBlockPos(this.bedPos);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleUseBed(this);
    }
    
    public EntityPlayer getPlayer(final World worldIn) {
        return (EntityPlayer)worldIn.getEntityByID(this.playerID);
    }
    
    public BlockPos getBedPosition() {
        return this.bedPos;
    }
}
