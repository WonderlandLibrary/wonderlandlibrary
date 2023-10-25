/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.scoreboard;

import net.minecraft.scoreboard.IScoreCriteria;

public class ScoreCriteria
implements IScoreCriteria {
    private final String dummyName;

    public ScoreCriteria(String name) {
        this.dummyName = name;
        IScoreCriteria.INSTANCES.put(name, this);
    }

    @Override
    public String getName() {
        return this.dummyName;
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

