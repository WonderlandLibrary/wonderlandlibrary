/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseSittingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PhaseSittingScanning
extends PhaseSittingBase {
    private int scanningTime;

    public PhaseSittingScanning(EntityDragon dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doLocalUpdate() {
        ++this.scanningTime;
        EntityPlayer entitylivingbase = this.dragon.world.getNearestAttackablePlayer(this.dragon, 20.0, 10.0);
        if (entitylivingbase != null) {
            if (this.scanningTime > 25) {
                this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_ATTACKING);
            } else {
                Vec3d vec3d = new Vec3d(entitylivingbase.posX - this.dragon.posX, 0.0, entitylivingbase.posZ - this.dragon.posZ).normalize();
                Vec3d vec3d1 = new Vec3d(MathHelper.sin(this.dragon.rotationYaw * ((float)Math.PI / 180)), 0.0, -MathHelper.cos(this.dragon.rotationYaw * ((float)Math.PI / 180))).normalize();
                float f = (float)vec3d1.dotProduct(vec3d);
                float f1 = (float)(Math.acos(f) * 57.29577951308232) + 0.5f;
                if (f1 < 0.0f || f1 > 10.0f) {
                    float f2;
                    double d0 = entitylivingbase.posX - this.dragon.dragonPartHead.posX;
                    double d1 = entitylivingbase.posZ - this.dragon.dragonPartHead.posZ;
                    double d2 = MathHelper.clamp(MathHelper.wrapDegrees(180.0 - MathHelper.atan2(d0, d1) * 57.29577951308232 - (double)this.dragon.rotationYaw), -100.0, 100.0);
                    this.dragon.randomYawVelocity *= 0.8f;
                    float f3 = f2 = MathHelper.sqrt(d0 * d0 + d1 * d1) + 1.0f;
                    if (f2 > 40.0f) {
                        f2 = 40.0f;
                    }
                    this.dragon.randomYawVelocity = (float)((double)this.dragon.randomYawVelocity + d2 * (double)(0.7f / f2 / f3));
                    this.dragon.rotationYaw += this.dragon.randomYawVelocity;
                }
            }
        } else if (this.scanningTime >= 100) {
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

    public PhaseList<PhaseSittingScanning> getPhaseList() {
        return PhaseList.SITTING_SCANNING;
    }
}

