/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;

public class SPacketUpdateScore
implements Packet<INetHandlerPlayClient> {
    private String name = "";
    private String objective = "";
    private int value;
    private Action action;

    public SPacketUpdateScore() {
    }

    public SPacketUpdateScore(Score scoreIn) {
        this.name = scoreIn.getPlayerName();
        this.objective = scoreIn.getObjective().getName();
        this.value = scoreIn.getScorePoints();
        this.action = Action.CHANGE;
    }

    public SPacketUpdateScore(String nameIn) {
        this.name = nameIn;
        this.objective = "";
        this.value = 0;
        this.action = Action.REMOVE;
    }

    public SPacketUpdateScore(String nameIn, ScoreObjective objectiveIn) {
        this.name = nameIn;
        this.objective = objectiveIn.getName();
        this.value = 0;
        this.action = Action.REMOVE;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException {
        this.name = buf.readStringFromBuffer(40);
        this.action = buf.readEnumValue(Action.class);
        this.objective = buf.readStringFromBuffer(16);
        if (this.action != Action.REMOVE) {
            this.value = buf.readVarIntFromBuffer();
        }
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeString(this.name);
        buf.writeEnumValue(this.action);
        buf.writeString(this.objective);
        if (this.action != Action.REMOVE) {
            buf.writeVarIntToBuffer(this.value);
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient handler) {
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

    public static enum Action {
        CHANGE,
        REMOVE;

    }
}

