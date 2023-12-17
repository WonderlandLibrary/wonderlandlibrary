// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Score;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketUpdateScore implements Packet<INetHandlerPlayClient>
{
    private String name;
    private String objective;
    private int value;
    private Action action;
    
    public SPacketUpdateScore() {
        this.name = "";
        this.objective = "";
    }
    
    public SPacketUpdateScore(final Score scoreIn) {
        this.name = "";
        this.objective = "";
        this.name = scoreIn.getPlayerName();
        this.objective = scoreIn.getObjective().getName();
        this.value = scoreIn.getScorePoints();
        this.action = Action.CHANGE;
    }
    
    public SPacketUpdateScore(final String nameIn) {
        this.name = "";
        this.objective = "";
        this.name = nameIn;
        this.objective = "";
        this.value = 0;
        this.action = Action.REMOVE;
    }
    
    public SPacketUpdateScore(final String nameIn, final ScoreObjective objectiveIn) {
        this.name = "";
        this.objective = "";
        this.name = nameIn;
        this.objective = objectiveIn.getName();
        this.value = 0;
        this.action = Action.REMOVE;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.name = buf.readString(40);
        this.action = buf.readEnumValue(Action.class);
        this.objective = buf.readString(16);
        if (this.action != Action.REMOVE) {
            this.value = buf.readVarInt();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeString(this.name);
        buf.writeEnumValue(this.action);
        buf.writeString(this.objective);
        if (this.action != Action.REMOVE) {
            buf.writeVarInt(this.value);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleUpdateScore(this);
    }
    
    public String getPlayerName() {
        return this.name;
    }
    
    public String getObjectiveName() {
        return this.objective;
    }
    
    public int getScoreValue() {
        return this.value;
    }
    
    public Action getScoreAction() {
        return this.action;
    }
    
    public enum Action
    {
        CHANGE, 
        REMOVE;
    }
}
