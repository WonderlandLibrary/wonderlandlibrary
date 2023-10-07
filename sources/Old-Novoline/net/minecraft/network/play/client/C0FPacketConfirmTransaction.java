package net.minecraft.network.play.client;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C0FPacketConfirmTransaction implements Packet<INetHandlerPlayServer> {

    private int windowId;
    private short transactionID;
    private boolean accepted;

    public C0FPacketConfirmTransaction() {
    }

    public C0FPacketConfirmTransaction(int windowId, short transactionID, boolean accepted) {
        this.windowId = windowId;
        this.transactionID = transactionID;
        this.accepted = accepted;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler) {
        handler.processConfirmTransaction(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.windowId = buf.readByte();
        this.transactionID = buf.readShort();
        this.accepted = buf.readByte() != 0;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.transactionID);
        buf.writeByte(this.accepted ? 1 : 0);
    }

    public int getWindowId() {
        return this.windowId;
    }

    public short getID() {
        return this.transactionID;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }

    public void setID(short transactionID) {
        this.transactionID = transactionID;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
