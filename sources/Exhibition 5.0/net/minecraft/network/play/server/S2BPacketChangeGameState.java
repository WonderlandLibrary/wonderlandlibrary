// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class S2BPacketChangeGameState implements Packet
{
    public static final String[] MESSAGE_NAMES;
    private int state;
    private float field_149141_c;
    private static final String __OBFID = "CL_00001301";
    
    public S2BPacketChangeGameState() {
    }
    
    public S2BPacketChangeGameState(final int stateIn, final float p_i45194_2_) {
        this.state = stateIn;
        this.field_149141_c = p_i45194_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.state = data.readUnsignedByte();
        this.field_149141_c = data.readFloat();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.state);
        data.writeFloat(this.field_149141_c);
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleChangeGameState(this);
    }
    
    public int func_149138_c() {
        return this.state;
    }
    
    public float func_149137_d() {
        return this.field_149141_c;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
    
    static {
        MESSAGE_NAMES = new String[] { "tile.bed.notValid" };
    }
}
