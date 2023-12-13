// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer;

import net.minecraft.util.BlockPos;

public class DestroyBlockProgress
{
    private final int miningPlayerEntId;
    private final BlockPos field_180247_b;
    private int partialBlockProgress;
    private int createdAtCloudUpdateTick;
    private static final String __OBFID = "CL_00001427";
    
    public DestroyBlockProgress(final int p_i45925_1_, final BlockPos p_i45925_2_) {
        this.miningPlayerEntId = p_i45925_1_;
        this.field_180247_b = p_i45925_2_;
    }
    
    public BlockPos func_180246_b() {
        return this.field_180247_b;
    }
    
    public void setPartialBlockDamage(int damage) {
        if (damage > 10) {
            damage = 10;
        }
        this.partialBlockProgress = damage;
    }
    
    public int getPartialBlockDamage() {
        return this.partialBlockProgress;
    }
    
    public void setCloudUpdateTick(final int p_82744_1_) {
        this.createdAtCloudUpdateTick = p_82744_1_;
    }
    
    public int getCreationCloudUpdateTick() {
        return this.createdAtCloudUpdateTick;
    }
}
