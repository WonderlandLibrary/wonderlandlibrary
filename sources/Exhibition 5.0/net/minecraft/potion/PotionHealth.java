// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.potion;

import net.minecraft.util.ResourceLocation;

public class PotionHealth extends Potion
{
    private static final String __OBFID = "CL_00001527";
    
    public PotionHealth(final int p_i45898_1_, final ResourceLocation p_i45898_2_, final boolean p_i45898_3_, final int p_i45898_4_) {
        super(p_i45898_1_, p_i45898_2_, p_i45898_3_, p_i45898_4_);
    }
    
    @Override
    public boolean isInstant() {
        return true;
    }
    
    @Override
    public boolean isReady(final int p_76397_1_, final int p_76397_2_) {
        return p_76397_1_ >= 1;
    }
}
