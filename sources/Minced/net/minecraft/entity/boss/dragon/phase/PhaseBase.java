// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.util.math.Vec3d;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.boss.EntityDragon;

public abstract class PhaseBase implements IPhase
{
    protected final EntityDragon dragon;
    
    public PhaseBase(final EntityDragon dragonIn) {
        this.dragon = dragonIn;
    }
    
    @Override
    public boolean getIsStationary() {
        return false;
    }
    
    @Override
    public void doClientRenderEffects() {
    }
    
    @Override
    public void doLocalUpdate() {
    }
    
    @Override
    public void onCrystalDestroyed(final EntityEnderCrystal crystal, final BlockPos pos, final DamageSource dmgSrc, @Nullable final EntityPlayer plyr) {
    }
    
    @Override
    public void initPhase() {
    }
    
    @Override
    public void removeAreaEffect() {
    }
    
    @Override
    public float getMaxRiseOrFall() {
        return 0.6f;
    }
    
    @Nullable
    @Override
    public Vec3d getTargetLocation() {
        return null;
    }
    
    @Override
    public float getAdjustedDamage(final MultiPartEntityPart pt, final DamageSource src, final float damage) {
        return damage;
    }
    
    @Override
    public float getYawFactor() {
        final float f = MathHelper.sqrt(this.dragon.motionX * this.dragon.motionX + this.dragon.motionZ * this.dragon.motionZ) + 1.0f;
        final float f2 = Math.min(f, 40.0f);
        return 0.7f / f2 / f;
    }
}
