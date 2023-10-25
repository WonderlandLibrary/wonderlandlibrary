/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public interface IPhase {
    public boolean getIsStationary();

    public void doClientRenderEffects();

    public void doLocalUpdate();

    public void onCrystalDestroyed(EntityEnderCrystal var1, BlockPos var2, DamageSource var3, EntityPlayer var4);

    public void initPhase();

    public void removeAreaEffect();

    public float getMaxRiseOrFall();

    public float getYawFactor();

    public PhaseList<? extends IPhase> getPhaseList();

    @Nullable
    public Vec3d getTargetLocation();

    public float getAdjustedDamage(MultiPartEntityPart var1, DamageSource var2, float var3);
}

