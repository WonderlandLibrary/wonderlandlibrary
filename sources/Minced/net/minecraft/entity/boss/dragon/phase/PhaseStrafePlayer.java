// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.pathfinding.Path;
import org.apache.logging.log4j.Logger;

public class PhaseStrafePlayer extends PhaseBase
{
    private static final Logger LOGGER;
    private int fireballCharge;
    private Path currentPath;
    private Vec3d targetLocation;
    private EntityLivingBase attackTarget;
    private boolean holdingPatternClockwise;
    
    public PhaseStrafePlayer(final EntityDragon dragonIn) {
        super(dragonIn);
    }
    
    @Override
    public void doLocalUpdate() {
        if (this.attackTarget == null) {
            PhaseStrafePlayer.LOGGER.warn("Skipping player strafe phase because no player was found");
            this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        }
        else {
            if (this.currentPath != null && this.currentPath.isFinished()) {
                final double d0 = this.attackTarget.posX;
                final double d2 = this.attackTarget.posZ;
                final double d3 = d0 - this.dragon.posX;
                final double d4 = d2 - this.dragon.posZ;
                final double d5 = MathHelper.sqrt(d3 * d3 + d4 * d4);
                final double d6 = Math.min(0.4000000059604645 + d5 / 80.0 - 1.0, 10.0);
                this.targetLocation = new Vec3d(d0, this.attackTarget.posY + d6, d2);
            }
            final double d7 = (this.targetLocation == null) ? 0.0 : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
            if (d7 < 100.0 || d7 > 22500.0) {
                this.findNewTarget();
            }
            final double d8 = 64.0;
            if (this.attackTarget.getDistanceSq(this.dragon) < 4096.0) {
                if (this.dragon.canEntityBeSeen(this.attackTarget)) {
                    ++this.fireballCharge;
                    final Vec3d vec3d1 = new Vec3d(this.attackTarget.posX - this.dragon.posX, 0.0, this.attackTarget.posZ - this.dragon.posZ).normalize();
                    final Vec3d vec3d2 = new Vec3d(MathHelper.sin(this.dragon.rotationYaw * 0.017453292f), 0.0, -MathHelper.cos(this.dragon.rotationYaw * 0.017453292f)).normalize();
                    final float f1 = (float)vec3d2.dotProduct(vec3d1);
                    float f2 = (float)(Math.acos(f1) * 57.29577951308232);
                    f2 += 0.5f;
                    if (this.fireballCharge >= 5 && f2 >= 0.0f && f2 < 10.0f) {
                        final double d9 = 1.0;
                        final Vec3d vec3d3 = this.dragon.getLook(1.0f);
                        final double d10 = this.dragon.dragonPartHead.posX - vec3d3.x * 1.0;
                        final double d11 = this.dragon.dragonPartHead.posY + this.dragon.dragonPartHead.height / 2.0f + 0.5;
                        final double d12 = this.dragon.dragonPartHead.posZ - vec3d3.z * 1.0;
                        final double d13 = this.attackTarget.posX - d10;
                        final double d14 = this.attackTarget.posY + this.attackTarget.height / 2.0f - (d11 + this.dragon.dragonPartHead.height / 2.0f);
                        final double d15 = this.attackTarget.posZ - d12;
                        this.dragon.world.playEvent(null, 1017, new BlockPos(this.dragon), 0);
                        final EntityDragonFireball entitydragonfireball = new EntityDragonFireball(this.dragon.world, this.dragon, d13, d14, d15);
                        entitydragonfireball.setLocationAndAngles(d10, d11, d12, 0.0f, 0.0f);
                        this.dragon.world.spawnEntity(entitydragonfireball);
                        this.fireballCharge = 0;
                        if (this.currentPath != null) {
                            while (!this.currentPath.isFinished()) {
                                this.currentPath.incrementPathIndex();
                            }
                        }
                        this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
                    }
                }
                else if (this.fireballCharge > 0) {
                    --this.fireballCharge;
                }
            }
            else if (this.fireballCharge > 0) {
                --this.fireballCharge;
            }
        }
    }
    
    private void findNewTarget() {
        if (this.currentPath == null || this.currentPath.isFinished()) {
            int j;
            final int i = j = this.dragon.initPathPoints();
            if (this.dragon.getRNG().nextInt(8) == 0) {
                this.holdingPatternClockwise = !this.holdingPatternClockwise;
                j = i + 6;
            }
            if (this.holdingPatternClockwise) {
                ++j;
            }
            else {
                --j;
            }
            if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() > 0) {
                j %= 12;
                if (j < 0) {
                    j += 12;
                }
            }
            else {
                j -= 12;
                j &= 0x7;
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
            final Vec3d vec3d = this.currentPath.getCurrentPos();
            this.currentPath.incrementPathIndex();
            final double d0 = vec3d.x;
            final double d2 = vec3d.z;
            double d3;
            do {
                d3 = vec3d.y + this.dragon.getRNG().nextFloat() * 20.0f;
            } while (d3 < vec3d.y);
            this.targetLocation = new Vec3d(d0, d3, d2);
        }
    }
    
    @Override
    public void initPhase() {
        this.fireballCharge = 0;
        this.targetLocation = null;
        this.currentPath = null;
        this.attackTarget = null;
    }
    
    public void setTarget(final EntityLivingBase p_188686_1_) {
        this.attackTarget = p_188686_1_;
        final int i = this.dragon.initPathPoints();
        final int j = this.dragon.getNearestPpIdx(this.attackTarget.posX, this.attackTarget.posY, this.attackTarget.posZ);
        final int k = MathHelper.floor(this.attackTarget.posX);
        final int l = MathHelper.floor(this.attackTarget.posZ);
        final double d0 = k - this.dragon.posX;
        final double d2 = l - this.dragon.posZ;
        final double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        final double d4 = Math.min(0.4000000059604645 + d3 / 80.0 - 1.0, 10.0);
        final int i2 = MathHelper.floor(this.attackTarget.posY + d4);
        final PathPoint pathpoint = new PathPoint(k, i2, l);
        this.currentPath = this.dragon.findPath(i, j, pathpoint);
        if (this.currentPath != null) {
            this.currentPath.incrementPathIndex();
            this.navigateToNextPathNode();
        }
    }
    
    @Nullable
    @Override
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }
    
    @Override
    public PhaseList<PhaseStrafePlayer> getType() {
        return PhaseList.STRAFE_PLAYER;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
