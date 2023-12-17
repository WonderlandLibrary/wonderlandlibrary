// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.EntityAreaEffectCloud;

public class PhaseSittingFlaming extends PhaseSittingBase
{
    private int flameTicks;
    private int flameCount;
    private EntityAreaEffectCloud areaEffectCloud;
    
    public PhaseSittingFlaming(final EntityDragon dragonIn) {
        super(dragonIn);
    }
    
    @Override
    public void doClientRenderEffects() {
        ++this.flameTicks;
        if (this.flameTicks % 2 == 0 && this.flameTicks < 10) {
            final Vec3d vec3d = this.dragon.getHeadLookVec(1.0f).normalize();
            vec3d.rotateYaw(-0.7853982f);
            final double d0 = this.dragon.dragonPartHead.posX;
            final double d2 = this.dragon.dragonPartHead.posY + this.dragon.dragonPartHead.height / 2.0f;
            final double d3 = this.dragon.dragonPartHead.posZ;
            for (int i = 0; i < 8; ++i) {
                final double d4 = d0 + this.dragon.getRNG().nextGaussian() / 2.0;
                final double d5 = d2 + this.dragon.getRNG().nextGaussian() / 2.0;
                final double d6 = d3 + this.dragon.getRNG().nextGaussian() / 2.0;
                for (int j = 0; j < 6; ++j) {
                    this.dragon.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, d4, d5, d6, -vec3d.x * 0.07999999821186066 * j, -vec3d.y * 0.6000000238418579, -vec3d.z * 0.07999999821186066 * j, new int[0]);
                }
                vec3d.rotateYaw(0.19634955f);
            }
        }
    }
    
    @Override
    public void doLocalUpdate() {
        ++this.flameTicks;
        if (this.flameTicks >= 200) {
            if (this.flameCount >= 4) {
                this.dragon.getPhaseManager().setPhase(PhaseList.TAKEOFF);
            }
            else {
                this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
            }
        }
        else if (this.flameTicks == 10) {
            final Vec3d vec3d = new Vec3d(this.dragon.dragonPartHead.posX - this.dragon.posX, 0.0, this.dragon.dragonPartHead.posZ - this.dragon.posZ).normalize();
            final float f = 5.0f;
            final double d0 = this.dragon.dragonPartHead.posX + vec3d.x * 5.0 / 2.0;
            final double d2 = this.dragon.dragonPartHead.posZ + vec3d.z * 5.0 / 2.0;
            double d3 = this.dragon.dragonPartHead.posY + this.dragon.dragonPartHead.height / 2.0f;
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(d0), MathHelper.floor(d3), MathHelper.floor(d2));
            while (this.dragon.world.isAirBlock(blockpos$mutableblockpos)) {
                --d3;
                blockpos$mutableblockpos.setPos(MathHelper.floor(d0), MathHelper.floor(d3), MathHelper.floor(d2));
            }
            d3 = MathHelper.floor(d3) + 1;
            (this.areaEffectCloud = new EntityAreaEffectCloud(this.dragon.world, d0, d3, d2)).setOwner(this.dragon);
            this.areaEffectCloud.setRadius(5.0f);
            this.areaEffectCloud.setDuration(200);
            this.areaEffectCloud.setParticle(EnumParticleTypes.DRAGON_BREATH);
            this.areaEffectCloud.addEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE));
            this.dragon.world.spawnEntity(this.areaEffectCloud);
        }
    }
    
    @Override
    public void initPhase() {
        this.flameTicks = 0;
        ++this.flameCount;
    }
    
    @Override
    public void removeAreaEffect() {
        if (this.areaEffectCloud != null) {
            this.areaEffectCloud.setDead();
            this.areaEffectCloud = null;
        }
    }
    
    @Override
    public PhaseList<PhaseSittingFlaming> getType() {
        return PhaseList.SITTING_FLAMING;
    }
    
    public void resetFlameCount() {
        this.flameCount = 0;
    }
}
