// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.MultiPartEntityPart;
import javax.annotation.Nullable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.item.EntityEnderCrystal;

public interface IPhase
{
    boolean getIsStationary();
    
    void doClientRenderEffects();
    
    void doLocalUpdate();
    
    void onCrystalDestroyed(final EntityEnderCrystal p0, final BlockPos p1, final DamageSource p2, final EntityPlayer p3);
    
    void initPhase();
    
    void removeAreaEffect();
    
    float getMaxRiseOrFall();
    
    float getYawFactor();
    
    PhaseList<? extends IPhase> getType();
    
    @Nullable
    Vec3d getTargetLocation();
    
    float getAdjustedDamage(final MultiPartEntityPart p0, final DamageSource p1, final float p2);
}
