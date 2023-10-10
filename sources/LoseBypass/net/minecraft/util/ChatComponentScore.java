/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.util.Iterator;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;

public class ChatComponentScore
extends ChatComponentStyle {
    private final String name;
    private final String objective;
    private String value = "";

    public ChatComponentScore(String nameIn, String objectiveIn) {
        this.name = nameIn;
        this.objective = objectiveIn;
    }

    public String getName() {
        return this.name;
    }

    public String getObjective() {
        return this.objective;
    }

    public void setValue(String valueIn) {
        this.value = valueIn;
    }

    @Override
    public String getUnformattedTextForChat() {
        ScoreObjective scoreobjective;
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        if (minecraftserver == null) return this.value;
        if (!minecraftserver.isAnvilFileSet()) return this.value;
        if (!StringUtils.isNullOrEmpty(this.value)) return this.value;
        Scoreboard scoreboard = minecraftserver.worldServerForDimension(0).getScoreboard();
        if (scoreboard.entityHasObjective(this.name, scoreobjective = scoreboard.getObjective(this.objective))) {
            Score score = scoreboard.getValueFromObjective(this.name, scoreobjective);
            this.setValue(String.format("%d", score.getScorePoints()));
            return this.value;
        }
        this.value = "";
        return this.value;
    }

    @Override
    public ChatComponentScore createCopy() {
        ChatComponentScore chatcomponentscore = new ChatComponentScore(this.name, this.objective);
        chatcomponentscore.setValue(this.value);
        chatcomponentscore.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator<IChatComponent> iterator = this.getSiblings().iterator();
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = iterator.next();
            chatcomponentscore.appendSibling(ichatcomponent.createCopy());
        }
        return chatcomponentscore;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ChatComponentScore)) {
            return false;
        }
        ChatComponentScore chatcomponentscore = (ChatComponentScore)p_equals_1_;
        if (!this.name.equals(chatcomponentscore.name)) return false;
        if (!this.objective.equals(chatcomponentscore.objective)) return false;
        if (!super.equals(p_equals_1_)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScoreComponent{name='" + this.name + '\'' + "objective='" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}

