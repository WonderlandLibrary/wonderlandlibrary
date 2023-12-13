/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;

public class S3CPacketUpdateScore
implements Packet {
    private String name = "";
    private String objective = "";
    private int value;
    private Action action;
    private static final String __OBFID = "CL_00001335";

    public S3CPacketUpdateScore() {
    }

    public S3CPacketUpdateScore(Score scoreIn) {
        this.name = scoreIn.getPlayerName();
        this.objective = scoreIn.getObjective().getName();
        this.value = scoreIn.getScorePoints();
        this.action = Action.CHANGE;
    }

    public S3CPacketUpdateScore(String nameIn) {
        this.name = nameIn;
        this.objective = "";
        this.value = 0;
        this.action = Action.REMOVE;
    }

    public S3CPacketUpdateScore(String nameIn, ScoreObjective objectiveIn) {
        this.name = nameIn;
        this.objective = objectiveIn.getName();
        this.value = 0;
        this.action = Action.REMOVE;
    }

    @Override
    public void readPacketData(PacketBuffer data) throws IOException {
        this.name = data.readStringFromBuffer(40);
        this.action = (Action)data.readEnumValue(Action.class);
        this.objective = data.readStringFromBuffer(16);
        if (this.action != Action.REMOVE) {
            this.value = data.readVarIntFromBuffer();
        }
    }

    @Override
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeString(this.name);
        data.writeEnumValue(this.action);
        data.writeString(this.objective);
        if (this.action != Action.REMOVE) {
            data.writeVarIntToBuffer(this.value);
        }
    }

    public void processPacket(INetHandlerPlayClient handler) {
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

    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    public static enum Action {
        CHANGE("CHANGE", 0),
        REMOVE("REMOVE", 1);

        private static final Action[] $VALUES;
        private static final String __OBFID = "CL_00002288";

        static {
            $VALUES = new Action[]{CHANGE, REMOVE};
        }

        private Action(String p_i45957_1_, int p_i45957_2_) {
        }
    }
}

