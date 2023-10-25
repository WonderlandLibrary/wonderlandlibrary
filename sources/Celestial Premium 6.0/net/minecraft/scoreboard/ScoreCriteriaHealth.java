/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.scoreboard;

import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreCriteria;

public class ScoreCriteriaHealth
extends ScoreCriteria {
    public ScoreCriteriaHealth(String name) {
        super(name);
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public IScoreCriteria.EnumRenderType getRenderType() {
        return IScoreCriteria.EnumRenderType.HEARTS;
    }
}

