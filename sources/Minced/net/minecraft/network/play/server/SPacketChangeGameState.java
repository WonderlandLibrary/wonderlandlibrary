// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketChangeGameState implements Packet<INetHandlerPlayClient>
{
    public static final String[] MESSAGE_NAMES;
    private int state;
    private float value;
    
    public SPacketChangeGameState() {
    }
    
    public SPacketChangeGameState(final int stateIn, final float valueIn) {
        this.state = stateIn;
        this.value = valueIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.state = buf.readUnsignedByte();
        this.value = buf.readFloat();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.state);
        buf.writeFloat(this.value);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleChangeGameState(this);
    }
    
    public int getGameState() {
        return this.state;
    }
    
    public float getValue() {
        return this.value;
    }
    
    static {
        MESSAGE_NAMES = new String[] { "tile.bed.notValid" };
    }
}
