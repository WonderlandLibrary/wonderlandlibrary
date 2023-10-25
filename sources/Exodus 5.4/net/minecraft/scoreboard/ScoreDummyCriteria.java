/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.scoreboard;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;

public class ScoreDummyCriteria
implements IScoreObjectiveCriteria {
    private final String dummyName;

    public ScoreDummyCriteria(String string) {
        this.dummyName = string;
        IScoreObjectiveCriteria.INSTANCES.put(string, this);
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
    public int func_96635_a(List<EntityPlayer> list) {
        return 0;
    }

    @Override
    public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
        return IScoreObjectiveCriteria.EnumRenderType.INTEGER;
    }
}

