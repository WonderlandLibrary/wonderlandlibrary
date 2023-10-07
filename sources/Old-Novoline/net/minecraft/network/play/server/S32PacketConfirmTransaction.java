package net.minecraft.network.play.server;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S32PacketConfirmTransaction implements Packet<INetHandlerPlayClient> {

    private int windowId;
    private short actionNumber;
    private boolean accepted;

    public S32PacketConfirmTransaction() {
    }

    public S32PacketConfirmTransaction(int windowIdIn, short actionNumberIn, boolean accepted) {
        this.windowId = windowIdIn;
        this.actionNumber = actionNumberIn;
        this.accepted = accepted;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleConfirmTransaction(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
        this.actionNumber = buf.readShort();
        this.accepted = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.actionNumber);
        buf.writeBoolean(this.accepted);
    }

    public int getWindowId() {
        return this.windowId;
    }

    public short getActionNumber() {
        return this.actionNumber;
    }

    public boolean isAccepted() {
        return this.accepted;
    }

    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }

    public void setActionNumber(short actionNumber) {
        this.actionNumber = actionNumber;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
