/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.scoreboard;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreDummyCriteria;
import net.minecraft.util.MathHelper;

public class ScoreHealthCriteria
extends ScoreDummyCriteria {
    @Override
    public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
        return IScoreObjectiveCriteria.EnumRenderType.HEARTS;
    }

    @Override
    public int func_96635_a(List<EntityPlayer> list) {
        float f = 0.0f;
        for (EntityPlayer entityPlayer : list) {
            f += entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount();
        }
        if (list.size() > 0) {
            f /= (float)list.size();
        }
        return MathHelper.ceiling_float_int(f);
    }

    public ScoreHealthCriteria(String string) {
        super(string);
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}

