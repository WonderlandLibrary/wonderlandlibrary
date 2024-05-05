/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.scoreboard;

import java.util.Iterator;
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
        float f = 0.0f;
        Iterator<EntityPlayer> iterator = p_96635_1_.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                if (p_96635_1_.size() <= 0) return MathHelper.ceiling_float_int(f);
                f /= (float)p_96635_1_.size();
                return MathHelper.ceiling_float_int(f);
            }
            EntityPlayer entityplayer = iterator.next();
            f += entityplayer.getHealth() + entityplayer.getAbsorptionAmount();
        }
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

