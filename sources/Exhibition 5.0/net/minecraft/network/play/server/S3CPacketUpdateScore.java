// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Score;
import net.minecraft.network.Packet;

public class S3CPacketUpdateScore implements Packet
{
    private String name;
    private String objective;
    private int value;
    private Action action;
    private static final String __OBFID = "CL_00001335";
    
    public S3CPacketUpdateScore() {
        this.name = "";
        this.objective = "";
    }
    
    public S3CPacketUpdateScore(final Score scoreIn) {
        this.name = "";
        this.objective = "";
        this.name = scoreIn.getPlayerName();
        this.objective = scoreIn.getObjective().getName();
        this.value = scoreIn.getScorePoints();
        this.action = Action.CHANGE;
    }
    
    public S3CPacketUpdateScore(final String nameIn) {
        this.name = "";
        this.objective = "";
        this.name = nameIn;
        this.objective = "";
        this.value = 0;
        this.action = Action.REMOVE;
    }
    
    public S3CPacketUpdateScore(final String nameIn, final ScoreObjective objectiveIn) {
        this.name = "";
        this.objective = "";
        this.name = nameIn;
        this.objective = objectiveIn.getName();
        this.value = 0;
        this.action = Action.REMOVE;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.name = data.readStringFromBuffer(40);
        this.action = (Action)data.readEnumValue(Action.class);
        this.objective = data.readStringFromBuffer(16);
        if (this.action != Action.REMOVE) {
            this.value = data.readVarIntFromBuffer();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeString(this.name);
        data.writeEnumValue(this.action);
        data.writeString(this.objective);
        if (this.action != Action.REMOVE) {
            data.writeVarIntToBuffer(this.value);
        }
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleUpdateScore(this);
    }
    
    public String func_149324_c() {
        return this.name;
    }
    
    public String func_149321_d() {
        return this.objective;
    }
    
    public int func_149323_e() {
        return this.value;
    }
    
    public Action func_180751_d() {
        return this.action;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
    
    public enum Action
    {
        CHANGE("CHANGE", 0), 
        REMOVE("REMOVE", 1);
        
        private static final Action[] $VALUES;
        private static final String __OBFID = "CL_00002288";
        
        private Action(final String p_i45957_1_, final int p_i45957_2_) {
        }
        
        static {
            $VALUES = new Action[] { Action.CHANGE, Action.REMOVE };
        }
    }
}
