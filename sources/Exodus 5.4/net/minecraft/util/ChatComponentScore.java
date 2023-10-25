/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;

public class ChatComponentScore
extends ChatComponentStyle {
    private String value = "";
    private final String name;
    private final String objective;

    public String getObjective() {
        return this.objective;
    }

    @Override
    public ChatComponentScore createCopy() {
        ChatComponentScore chatComponentScore = new ChatComponentScore(this.name, this.objective);
        chatComponentScore.setValue(this.value);
        chatComponentScore.setChatStyle(this.getChatStyle().createShallowCopy());
        for (IChatComponent iChatComponent : this.getSiblings()) {
            chatComponentScore.appendSibling(iChatComponent.createCopy());
        }
        return chatComponentScore;
    }

    @Override
    public String getUnformattedTextForChat() {
        MinecraftServer minecraftServer = MinecraftServer.getServer();
        if (minecraftServer != null && minecraftServer.isAnvilFileSet() && StringUtils.isNullOrEmpty(this.value)) {
            ScoreObjective scoreObjective;
            Scoreboard scoreboard = minecraftServer.worldServerForDimension(0).getScoreboard();
            if (scoreboard.entityHasObjective(this.name, scoreObjective = scoreboard.getObjective(this.objective))) {
                Score score = scoreboard.getValueFromObjective(this.name, scoreObjective);
                this.setValue(String.format("%d", score.getScorePoints()));
            } else {
                this.value = "";
            }
        }
        return this.value;
    }

    @Override
    public String toString() {
        return "ScoreComponent{name='" + this.name + '\'' + "objective='" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ChatComponentScore)) {
            return false;
        }
        ChatComponentScore chatComponentScore = (ChatComponentScore)object;
        return this.name.equals(chatComponentScore.name) && this.objective.equals(chatComponentScore.objective) && super.equals(object);
    }

    public void setValue(String string) {
        this.value = string;
    }

    public ChatComponentScore(String string, String string2) {
        this.name = string;
        this.objective = string2;
    }

    public String getName() {
        return this.name;
    }
}

