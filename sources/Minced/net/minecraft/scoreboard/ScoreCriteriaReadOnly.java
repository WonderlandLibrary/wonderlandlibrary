// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.scoreboard;

public class ScoreCriteriaReadOnly extends ScoreCriteria
{
    public ScoreCriteriaReadOnly(final String name) {
        super(name);
    }
    
    @Override
    public boolean isReadOnly() {
        return true;
    }
}
