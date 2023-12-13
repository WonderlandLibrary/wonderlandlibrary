/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.scoreboard;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreDummyCriteria;
import net.minecraft.util.MathHelper;

public class ScoreHealthCriteria
extends ScoreDummyCriteria {
    private static final String __OBFID = "CL_00000623";

    public ScoreHealthCriteria(String p_i2312_1_) {
        super(p_i2312_1_);
    }

    @Override
    public int func_96635_a(List p_96635_1_) {
        float var2 = 0.0f;
        for (EntityPlayer var4 : p_96635_1_) {
            var2 += var4.getHealth() + var4.getAbsorptionAmount();
        }
        if (p_96635_1_.size() > 0) {
            var2 /= (float)p_96635_1_.size();
        }
        return MathHelper.ceiling_float_int(var2);
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public IScoreObjectiveCriteria.EnumRenderType func_178790_c() {
        return IScoreObjectiveCriteria.EnumRenderType.HEARTS;
    }
}

