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
    public ScoreHealthCriteria(String name) {
        super(name);
    }

    @Override
    public int func_96635_a(List<EntityPlayer> p_96635_1_) {
        float f2 = 0.0f;
        for (EntityPlayer entityplayer : p_96635_1_) {
            f2 += entityplayer.getHealth() + entityplayer.getAbsorptionAmount();
        }
        if (p_96635_1_.size() > 0) {
            f2 /= (float)p_96635_1_.size();
        }
        return MathHelper.ceiling_float_int(f2);
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public IScoreObjectiveCriteria.EnumRenderType getRenderType() {
        return IScoreObjectiveCriteria.EnumRenderType.HEARTS;
    }
}

