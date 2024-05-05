package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class C07PacketPlayerDigging
implements Packet<INetHandlerPlayServer> {
    private BlockPos position;
    private EnumFacing facing;
    private Action status;

    public C07PacketPlayerDigging() {
    }

    public C07PacketPlayerDigging(Action statusIn, BlockPos posIn, EnumFacing facingIn) {
        this.status = statusIn;
        this.position = posIn;
        this.facing = facingIn;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.status = (Action)buf.readEnumValue(Action.class);
        this.position = buf.readBlockPos();
        this.facing = EnumFacing.getFront((int)buf.readUnsignedByte());
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeEnumValue((Enum)this.status);
        buf.writeBlockPos(this.position);
        buf.writeByte(this.facing.getIndex());
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.processPlayerDigging(this);
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public EnumFacing getFacing() {
        return this.facing;
    }

    public Action getStatus() {
        return this.status;
    }
}
