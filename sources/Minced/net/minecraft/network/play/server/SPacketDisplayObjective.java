// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketDisplayObjective implements Packet<INetHandlerPlayClient>
{
    private int position;
    private String scoreName;
    
    public SPacketDisplayObjective() {
    }
    
    public SPacketDisplayObjective(final int positionIn, final ScoreObjective objective) {
        this.position = positionIn;
        if (objective == null) {
            this.scoreName = "";
        }
        else {
            this.scoreName = objective.getName();
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.position = buf.readByte();
        this.scoreName = buf.readString(16);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.position);
        buf.writeString(this.scoreName);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleDisplayObjective(this);
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public String getName() {
        return this.scoreName;
    }
}
