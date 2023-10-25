/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.text;

import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;

public class TextComponentScore
extends TextComponentBase {
    private final String name;
    private final String objective;
    private String value = "";

    public TextComponentScore(String nameIn, String objectiveIn) {
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
    public String getUnformattedComponentText() {
        return this.value;
    }

    public void resolve(ICommandSender sender) {
        MinecraftServer minecraftserver = sender.getServer();
        if (minecraftserver != null && minecraftserver.isAnvilFileSet() && StringUtils.isNullOrEmpty(this.value)) {
            ScoreObjective scoreobjective;
            Scoreboard scoreboard = minecraftserver.getWorld(0).getScoreboard();
            if (scoreboard.entityHasObjective(this.name, scoreobjective = scoreboard.getObjective(this.objective))) {
                Score score = scoreboard.getOrCreateScore(this.name, scoreobjective);
                this.setValue(String.format("%d", score.getScorePoints()));
            } else {
                this.value = "";
            }
        }
    }

    @Override
    public TextComponentScore createCopy() {
        TextComponentScore textcomponentscore = new TextComponentScore(this.name, this.objective);
        textcomponentscore.setValue(this.value);
        textcomponentscore.setStyle(this.getStyle().createShallowCopy());
        for (ITextComponent itextcomponent : this.getSiblings()) {
            textcomponentscore.appendSibling(itextcomponent.createCopy());
        }
        return textcomponentscore;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof TextComponentScore)) {
            return false;
        }
        TextComponentScore textcomponentscore = (TextComponentScore)p_equals_1_;
        return this.name.equals(textcomponentscore.name) && this.objective.equals(textcomponentscore.objective) && super.equals(p_equals_1_);
    }

    @Override
    public String toString() {
        return "ScoreComponent{name='" + this.name + '\'' + "objective='" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
}

