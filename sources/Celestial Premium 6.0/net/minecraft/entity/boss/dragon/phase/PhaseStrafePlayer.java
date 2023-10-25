/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseBase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhaseStrafePlayer
extends PhaseBase {
    private static final Logger LOGGER = LogManager.getLogger();
    private int fireballCharge;
    private Path currentPath;
    private Vec3d targetLocation;
    private EntityLivingBase attackTarget;
    private boolean holdingPatternClockwise;

    public PhaseStrafePlayer(EntityDragon dragonIn) {
        super(dragonIn);
    }

    @Override
    public void doLocalUpdate() {
        if (this.attackTarget == null) {
            LOGGER.warn("Skipping player strafe phase because no player was found");
            this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        } else {
            double d12;
            if (this.currentPath != null && this.currentPath.isFinished()) {
                double d0 = this.attackTarget.posX;
                double d1 = this.attackTarget.posZ;
                double d2 = d0 - this.dragon.posX;
                double d3 = d1 - this.dragon.posZ;
                double d4 = MathHelper.sqrt(d2 * d2 + d3 * d3);
                double d5 = Math.min((double)0.4f + d4 / 80.0 - 1.0, 10.0);
                this.targetLocation = new Vec3d(d0, this.attackTarget.posY + d5, d1);
            }
            double d = d12 = this.targetLocation == null ? 0.0 : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
            if (d12 < 100.0 || d12 > 22500.0) {
                this.findNewTarget();
            }
            double d13 = 64.0;
            if (this.attackTarget.getDistanceSqToEntity(this.dragon) < 4096.0) {
                if (this.dragon.canEntityBeSeen(this.attackTarget)) {
                    ++this.fireballCharge;
                    Vec3d vec3d1 = new Vec3d(this.attackTarget.posX - this.dragon.posX, 0.0, this.attackTarget.posZ - this.dragon.posZ).normalize();
                    Vec3d vec3d = new Vec3d(MathHelper.sin(this.dragon.rotationYaw * ((float)Math.PI / 180)), 0.0, -MathHelper.cos(this.dragon.rotationYaw * ((float)Math.PI / 180))).normalize();
                    float f1 = (float)vec3d.dotProduct(vec3d1);
                    float f = (float)(Math.acos(f1) * 57.29577951308232);
                    f += 0.5f;
                    if (this.fireballCharge >= 5 && f >= 0.0f && f < 10.0f) {
                        double d14 = 1.0;
                        Vec3d vec3d2 = this.dragon.getLook(1.0f);
                        double d6 = this.dragon.dragonPartHead.posX - vec3d2.x * 1.0;
                        double d7 = this.dragon.dragonPartHead.posY + (double)(this.dragon.dragonPartHead.height / 2.0f) + 0.5;
                        double d8 = this.dragon.dragonPartHead.posZ - vec3d2.z * 1.0;
                        double d9 = this.attackTarget.posX - d6;
                        double d10 = this.attackTarget.posY + (double)(this.attackTarget.height / 2.0f) - (d7 + (double)(this.dragon.dragonPartHead.height / 2.0f));
                        double d11 = this.attackTarget.posZ - d8;
                        this.dragon.world.playEvent(null, 1017, new BlockPos(this.dragon), 0);
                        EntityDragonFireball entitydragonfireball = new EntityDragonFireball(this.dragon.world, this.dragon, d9, d10, d11);
                        entitydragonfireball.setLocationAndAngles(d6, d7, d8, 0.0f, 0.0f);
                        this.dragon.world.spawnEntityInWorld(entitydragonfireball);
                        this.fireballCharge = 0;
                        if (this.currentPath != null) {
                            while (!this.currentPath.isFinished()) {
                                this.currentPath.incrementPathIndex();
                            }
                        }
                        this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
                    }
                } else if (this.fireballCharge > 0) {
                    --this.fireballCharge;
                }
            } else if (this.fireballCharge > 0) {
                --this.fireballCharge;
            }
        }
    }

    private void findNewTarget() {
        if (this.currentPath == null || this.currentPath.isFinished()) {
            int i;
            int j = i = this.dragon.initPathPoints();
            if (this.dragon.getRNG().nextInt(8) == 0) {
                this.holdingPatternClockwise = !this.holdingPatternClockwise;
                j = i + 6;
            }
            j = this.holdingPatternClockwise ? ++j : --j;
            if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() > 0) {
                if ((j %= 12) < 0) {
                    j += 12;
                }
            } else {
                j -= 12;
                j &= 7;
                j += 12;
            }
            this.currentPath = this.dragon.findPath(i, j, null);
            if (this.currentPath != null) {
                this.currentPath.incrementPathIndex();
            }
        }
        this.navigateToNextPathNode();
    }

    private void navigateToNextPathNode() {
        if (this.currentPath != null && !this.currentPath.isFinished()) {
            double d1;
            Vec3d vec3d = this.currentPath.getCurrentPos();
            this.currentPath.incrementPathIndex();
            double d0 = vec3d.x;
            double d2 = vec3d.z;
            while (!((d1 = vec3d.y + (double)(this.dragon.getRNG().nextFloat() * 20.0f)) >= vec3d.y)) {
            }
            this.targetLocation = new Vec3d(d0, d1, d2);
        }
    }

    @Override
    public void initPhase() {
        this.fireballCharge = 0;
        this.targetLocation = null;
        this.currentPath = null;
        this.attackTarget = null;
    }

    public void setTarget(EntityLivingBase p_188686_1_) {
        this.attackTarget = p_188686_1_;
        int i = this.dragon.initPathPoints();
        int j = this.dragon.getNearestPpIdx(this.attackTarget.posX, this.attackTarget.posY, this.attackTarget.posZ);
        int k = MathHelper.floor(this.attackTarget.posX);
        int l = MathHelper.floor(this.attackTarget.posZ);
        double d0 = (double)k - this.dragon.posX;
        double d1 = (double)l - this.dragon.posZ;
        double d2 = MathHelper.sqrt(d0 * d0 + d1 * d1);
        double d3 = Math.min((double)0.4f + d2 / 80.0 - 1.0, 10.0);
        int i1 = MathHelper.floor(this.attackTarget.posY + d3);
        PathPoint pathpoint = new PathPoint(k, i1, l);
        this.currentPath = this.dragon.findPath(i, j, pathpoint);
        if (this.currentPath != null) {
            this.currentPath.incrementPathIndex();
            this.navigateToNextPathNode();
        }
    }

    @Override
    @Nullable
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }

    public PhaseList<PhaseStrafePlayer> getPhaseList() {
        return PhaseList.STRAFE_PLAYER;
    }
}

