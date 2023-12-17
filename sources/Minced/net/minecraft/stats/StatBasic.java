// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import net.minecraft.util.text.ITextComponent;

public class StatBasic extends StatBase
{
    public StatBasic(final String statIdIn, final ITextComponent statNameIn, final IStatType typeIn) {
        super(statIdIn, statNameIn, typeIn);
    }
    
    public StatBasic(final String statIdIn, final ITextComponent statNameIn) {
        super(statIdIn, statNameIn);
    }
    
    @Override
    public StatBase registerStat() {
        super.registerStat();
        StatList.BASIC_STATS.add(this);
        return this;
    }
}
