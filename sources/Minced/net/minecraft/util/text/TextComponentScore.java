// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text;

import java.util.Iterator;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import net.minecraft.command.ICommandSender;

public class TextComponentScore extends TextComponentBase
{
    private final String name;
    private final String objective;
    private String value;
    
    public TextComponentScore(final String nameIn, final String objectiveIn) {
        this.value = "";
        this.name = nameIn;
        this.objective = objectiveIn;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getObjective() {
        return this.objective;
    }
    
    public void setValue(final String valueIn) {
        this.value = valueIn;
    }
    
    @Override
    public String getUnformattedComponentText() {
        return this.value;
    }
    
    public void resolve(final ICommandSender sender) {
        final MinecraftServer minecraftserver = sender.getServer();
        if (minecraftserver != null && minecraftserver.isAnvilFileSet() && StringUtils.isNullOrEmpty(this.value)) {
            final Scoreboard scoreboard = minecraftserver.getWorld(0).getScoreboard();
            final ScoreObjective scoreobjective = scoreboard.getObjective(this.objective);
            if (scoreboard.entityHasObjective(this.name, scoreobjective)) {
                final Score score = scoreboard.getOrCreateScore(this.name, scoreobjective);
                this.setValue(String.format("%d", score.getScorePoints()));
            }
            else {
                this.value = "";
            }
        }
    }
    
    @Override
    public TextComponentScore createCopy() {
        final TextComponentScore textcomponentscore = new TextComponentScore(this.name, this.objective);
        textcomponentscore.setValue(this.value);
        textcomponentscore.setStyle(this.getStyle().createShallowCopy());
        for (final ITextComponent itextcomponent : this.getSiblings()) {
            textcomponentscore.appendSibling(itextcomponent.createCopy());
        }
        return textcomponentscore;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof TextComponentScore)) {
            return false;
        }
        final TextComponentScore textcomponentscore = (TextComponentScore)p_equals_1_;
        return this.name.equals(textcomponentscore.name) && this.objective.equals(textcomponentscore.objective) && super.equals(p_equals_1_);
    }
    
    @Override
    public String toString() {
        return "ScoreComponent{name='" + this.name + '\'' + "objective='" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
}
