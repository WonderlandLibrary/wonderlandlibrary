// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

public class ScoreCriteriaHealth extends ScoreCriteria
{
    public ScoreCriteriaHealth(final String name) {
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
