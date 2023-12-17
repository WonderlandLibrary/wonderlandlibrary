// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.Vec3d;

public class PhaseLanding extends PhaseBase
{
    private Vec3d targetLocation;
    
    public PhaseLanding(final EntityDragon dragonIn) {
        super(dragonIn);
    }
    
    @Override
    public void doClientRenderEffects() {
        final Vec3d vec3d = this.dragon.getHeadLookVec(1.0f).normalize();
        vec3d.rotateYaw(-0.7853982f);
        final double d0 = this.dragon.dragonPartHead.posX;
        final double d2 = this.dragon.dragonPartHead.posY + this.dragon.dragonPartHead.height / 2.0f;
        final double d3 = this.dragon.dragonPartHead.posZ;
        for (int i = 0; i < 8; ++i) {
            final double d4 = d0 + this.dragon.getRNG().nextGaussian() / 2.0;
            final double d5 = d2 + this.dragon.getRNG().nextGaussian() / 2.0;
            final double d6 = d3 + this.dragon.getRNG().nextGaussian() / 2.0;
            this.dragon.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, d4, d5, d6, -vec3d.x * 0.07999999821186066 + this.dragon.motionX, -vec3d.y * 0.30000001192092896 + this.dragon.motionY, -vec3d.z * 0.07999999821186066 + this.dragon.motionZ, new int[0]);
            vec3d.rotateYaw(0.19634955f);
        }
    }
    
    @Override
    public void doLocalUpdate() {
        if (this.targetLocation == null) {
            this.targetLocation = new Vec3d(this.dragon.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION));
        }
        if (this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ) < 1.0) {
            this.dragon.getPhaseManager().getPhase(PhaseList.SITTING_FLAMING).resetFlameCount();
            this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
        }
    }
    
    @Override
    public float getMaxRiseOrFall() {
        return 1.5f;
    }
    
    @Override
    public float getYawFactor() {
        final float f = MathHelper.sqrt(this.dragon.motionX * this.dragon.motionX + this.dragon.motionZ * this.dragon.motionZ) + 1.0f;
        final float f2 = Math.min(f, 40.0f);
        return f2 / f;
    }
    
    @Override
    public void initPhase() {
        this.targetLocation = null;
    }
    
    @Nullable
    @Override
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }
    
    @Override
    public PhaseList<PhaseLanding> getType() {
        return PhaseList.LANDING;
    }
}
