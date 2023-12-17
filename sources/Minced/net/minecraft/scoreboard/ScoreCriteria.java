// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

public class ScoreCriteria implements IScoreCriteria
{
    private final String dummyName;
    
    public ScoreCriteria(final String name) {
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
    public EnumRenderType getRenderType() {
        return EnumRenderType.INTEGER;
    }
}
