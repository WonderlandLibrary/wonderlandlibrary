// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.scoreboard;

import java.util.Iterator;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;

public class ScoreHealthCriteria extends ScoreDummyCriteria
{
    private static final String __OBFID = "CL_00000623";
    
    public ScoreHealthCriteria(final String p_i2312_1_) {
        super(p_i2312_1_);
    }
    
    @Override
    public int func_96635_a(final List p_96635_1_) {
        float var2 = 0.0f;
        for (final EntityPlayer var4 : p_96635_1_) {
            var2 += var4.getHealth() + var4.getAbsorptionAmount();
        }
        if (p_96635_1_.size() > 0) {
            var2 /= p_96635_1_.size();
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
