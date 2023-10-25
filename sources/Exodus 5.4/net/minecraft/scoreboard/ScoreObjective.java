/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.scoreboard;

import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Scoreboard;

public class ScoreObjective {
    private final String name;
    private String displayName;
    private final Scoreboard theScoreboard;
    private IScoreObjectiveCriteria.EnumRenderType renderType;
    private final IScoreObjectiveCriteria objectiveCriteria;

    public String getName() {
        return this.name;
    }

    public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
        return this.renderType;
    }

    public void setRenderType(IScoreObjectiveCriteria.EnumRenderType enumRenderType) {
        this.renderType = enumRenderType;
        this.theScoreboard.func_96532_b(this);
    }

    public void setDisplayName(String string) {
        this.displayName = string;
        this.theScoreboard.func_96532_b(this);
    }

    public Scoreboard getScoreboard() {
        return this.theScoreboard;
    }

    public IScoreObjectiveCriteria getCriteria() {
        return this.objectiveCriteria;
    }

    public ScoreObjective(Scoreboard scoreboard, String string, IScoreObjectiveCriteria iScoreObjectiveCriteria) {
        this.theScoreboard = scoreboard;
        this.name = string;
        this.objectiveCriteria = iScoreObjectiveCriteria;
        this.displayName = string;
        this.renderType = iScoreObjectiveCriteria.getRenderType();
    }

    public String getDisplayName() {
        return this.displayName;
    }
}

