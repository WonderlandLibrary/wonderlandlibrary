// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.BlockPos;

public class BlockEventData
{
    private BlockPos field_180329_a;
    private Block field_151344_d;
    private int eventID;
    private int eventParameter;
    private static final String __OBFID = "CL_00000131";
    
    public BlockEventData(final BlockPos p_i45756_1_, final Block p_i45756_2_, final int p_i45756_3_, final int p_i45756_4_) {
        this.field_180329_a = p_i45756_1_;
        this.eventID = p_i45756_3_;
        this.eventParameter = p_i45756_4_;
        this.field_151344_d = p_i45756_2_;
    }
    
    public BlockPos func_180328_a() {
        return this.field_180329_a;
    }
    
    public int getEventID() {
        return this.eventID;
    }
    
    public int getEventParameter() {
        return this.eventParameter;
    }
    
    public Block getBlock() {
        return this.field_151344_d;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (!(p_equals_1_ instanceof BlockEventData)) {
            return false;
        }
        final BlockEventData var2 = (BlockEventData)p_equals_1_;
        return this.field_180329_a.equals(var2.field_180329_a) && this.eventID == var2.eventID && this.eventParameter == var2.eventParameter && this.field_151344_d == var2.field_151344_d;
    }
    
    @Override
    public String toString() {
        return "TE(" + this.field_180329_a + ")," + this.eventID + "," + this.eventParameter + "," + this.field_151344_d;
    }
}
