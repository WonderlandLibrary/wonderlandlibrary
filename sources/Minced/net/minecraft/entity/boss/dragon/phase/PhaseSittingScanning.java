// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;

public class PhaseSittingScanning extends PhaseSittingBase
{
    private int scanningTime;
    
    public PhaseSittingScanning(final EntityDragon dragonIn) {
        super(dragonIn);
    }
    
    @Override
    public void doLocalUpdate() {
        ++this.scanningTime;
        EntityLivingBase entitylivingbase = this.dragon.world.getNearestAttackablePlayer(this.dragon, 20.0, 10.0);
        if (entitylivingbase != null) {
            if (this.scanningTime > 25) {
                this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_ATTACKING);
            }
            else {
                final Vec3d vec3d = new Vec3d(entitylivingbase.posX - this.dragon.posX, 0.0, entitylivingbase.posZ - this.dragon.posZ).normalize();
                final Vec3d vec3d2 = new Vec3d(MathHelper.sin(this.dragon.rotationYaw * 0.017453292f), 0.0, -MathHelper.cos(this.dragon.rotationYaw * 0.017453292f)).normalize();
                final float f = (float)vec3d2.dotProduct(vec3d);
                final float f2 = (float)(Math.acos(f) * 57.29577951308232) + 0.5f;
                if (f2 < 0.0f || f2 > 10.0f) {
                    final double d0 = entitylivingbase.posX - this.dragon.dragonPartHead.posX;
                    final double d2 = entitylivingbase.posZ - this.dragon.dragonPartHead.posZ;
                    final double d3 = MathHelper.clamp(MathHelper.wrapDegrees(180.0 - MathHelper.atan2(d0, d2) * 57.29577951308232 - this.dragon.rotationYaw), -100.0, 100.0);
                    final EntityDragon dragon = this.dragon;
                    dragon.randomYawVelocity *= 0.8f;
                    final float f4;
                    float f3 = f4 = MathHelper.sqrt(d0 * d0 + d2 * d2) + 1.0f;
                    if (f3 > 40.0f) {
                        f3 = 40.0f;
                    }
                    this.dragon.randomYawVelocity += (float)(d3 * (0.7f / f3 / f4));
                    final EntityDragon dragon2 = this.dragon;
                    dragon2.rotationYaw += this.dragon.randomYawVelocity;
                }
            }
        }
        else if (this.scanningTime >= 100) {
            entitylivingbase = this.dragon.world.getNearestAttackablePlayer(this.dragon, 150.0, 150.0);
            this.dragon.getPhaseManager().setPhase(PhaseList.TAKEOFF);
            if (entitylivingbase != null) {
                this.dragon.getPhaseManager().setPhase(PhaseList.CHARGING_PLAYER);
                this.dragon.getPhaseManager().getPhase(PhaseList.CHARGING_PLAYER).setTarget(new Vec3d(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ));
            }
        }
    }
    
    @Override
    public void initPhase() {
        this.scanningTime = 0;
    }
    
    @Override
    public PhaseList<PhaseSittingScanning> getType() {
        return PhaseList.SITTING_SCANNING;
    }
}
