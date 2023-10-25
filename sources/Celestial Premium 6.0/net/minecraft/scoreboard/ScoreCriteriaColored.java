/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.scoreboard;

import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.util.text.TextFormatting;

public class ScoreCriteriaColored
implements IScoreCriteria {
    private final String goalName;

    public ScoreCriteriaColored(String name, TextFormatting format) {
        this.goalName = name + format.getFriendlyName();
        IScoreCriteria.INSTANCES.put(this.goalName, this);
    }

    @Override
    public String getName() {
        return this.goalName;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public IScoreCriteria.EnumRenderType getRenderType() {
        return IScoreCriteria.EnumRenderType.INTEGER;
    }
}

