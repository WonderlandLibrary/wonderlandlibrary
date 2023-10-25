/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;

public class S3CPacketUpdateScore
implements Packet<INetHandlerPlayClient> {
    private Action action;
    private String name = "";
    private int value;
    private String objective = "";

    public int getScoreValue() {
        return this.value;
    }

    public S3CPacketUpdateScore(String string) {
        this.name = string;
        this.objective = "";
        this.value = 0;
        this.action = Action.REMOVE;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleUpdateScore(this);
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.name = packetBuffer.readStringFromBuffer(40);
        this.action = packetBuffer.readEnumValue(Action.class);
        this.objective = packetBuffer.readStringFromBuffer(16);
        if (this.action != Action.REMOVE) {
            this.value = packetBuffer.readVarIntFromBuffer();
        }
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeString(this.name);
        packetBuffer.writeEnumValue(this.action);
        packetBuffer.writeString(this.objective);
        if (this.action != Action.REMOVE) {
            packetBuffer.writeVarIntToBuffer(this.value);
        }
    }

    public S3CPacketUpdateScore(Score score) {
        this.name = score.getPlayerName();
        this.objective = score.getObjective().getName();
        this.value = score.getScorePoints();
        this.action = Action.CHANGE;
    }

    public S3CPacketUpdateScore() {
    }

    public S3CPacketUpdateScore(String string, ScoreObjective scoreObjective) {
        this.name = string;
        this.objective = scoreObjective.getName();
        this.value = 0;
        this.action = Action.REMOVE;
    }

    public String getObjectiveName() {
        return this.objective;
    }

    public Action getScoreAction() {
        return this.action;
    }

    public String getPlayerName() {
        return this.name;
    }

    public static enum Action {
        CHANGE,
        REMOVE;

    }
}

