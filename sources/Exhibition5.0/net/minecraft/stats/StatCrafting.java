// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.stats;

import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.util.IChatComponent;
import net.minecraft.item.Item;

public class StatCrafting extends StatBase
{
    private final Item field_150960_a;
    private static final String __OBFID = "CL_00001470";
    
    public StatCrafting(final String p_i45910_1_, final String p_i45910_2_, final IChatComponent p_i45910_3_, final Item p_i45910_4_) {
        super(p_i45910_1_ + p_i45910_2_, p_i45910_3_);
        this.field_150960_a = p_i45910_4_;
        final int var5 = Item.getIdFromItem(p_i45910_4_);
        if (var5 != 0) {
            IScoreObjectiveCriteria.INSTANCES.put(p_i45910_1_ + var5, this.func_150952_k());
        }
    }
    
    public Item func_150959_a() {
        return this.field_150960_a;
    }
}
