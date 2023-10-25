/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SPacketUseBed
implements Packet<INetHandlerPlayClient> {
    private int playerID;
    private BlockPos bedPos;

    public SPacketUseBed() {
    }

    public SPacketUseBed(EntityPlayer player, BlockPos posIn) {
        this.playerID = player.getEntityId();
        this.bedPos = posIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.playerID = buf.readVarIntFromBuffer();
        this.bedPos = buf.readBlockPos();
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.playerID);
        buf.writeBlockPos(this.bedPos);
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleUseBed(this);
    }

    public EntityPlayer getPlayer(World worldIn) {
        return (EntityPlayer)worldIn.getEntityByID(this.playerID);
    }

    public BlockPos getBedPosition() {
        return this.bedPos;
    }
}

