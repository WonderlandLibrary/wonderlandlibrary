/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseBase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseLandingApproach
extends PhaseBase {
    private Path currentPath;
    private Vec3d targetLocation;

    public PhaseLandingApproach(EntityDragon dragonIn) {
        super(dragonIn);
    }

    public PhaseList<PhaseLandingApproach> getPhaseList() {
        return PhaseList.LANDING_APPROACH;
    }

    @Override
    public void initPhase() {
        this.currentPath = null;
        this.targetLocation = null;
    }

    @Override
    public void doLocalUpdate() {
        double d0;
        double d = d0 = this.targetLocation == null ? 0.0 : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
        if (d0 < 100.0 || d0 > 22500.0 || this.dragon.isCollidedHorizontally || this.dragon.isCollidedVertically) {
            this.findNewTarget();
        }
    }

    @Override
    @Nullable
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }

    private void findNewTarget() {
        if (this.currentPath == null || this.currentPath.isFinished()) {
            int j;
            int i = this.dragon.initPathPoints();
            BlockPos blockpos = this.dragon.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
            EntityPlayer entityplayer = this.dragon.world.getNearestAttackablePlayer(blockpos, 128.0, 128.0);
            if (entityplayer != null) {
                Vec3d vec3d = new Vec3d(entityplayer.posX, 0.0, entityplayer.posZ).normalize();
                j = this.dragon.getNearestPpIdx(-vec3d.x * 40.0, 105.0, -vec3d.z * 40.0);
            } else {
                j = this.dragon.getNearestPpIdx(40.0, blockpos.getY(), 0.0);
            }
            PathPoint pathpoint = new PathPoint(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            this.currentPath = this.dragon.findPath(i, j, pathpoint);
            if (this.currentPath != null) {
                this.currentPath.incrementPathIndex();
            }
        }
        this.navigateToNextPathNode();
        if (this.currentPath != null && this.currentPath.isFinished()) {
            this.dragon.getPhaseManager().setPhase(PhaseList.LANDING);
        }
    }

    private void navigateToNextPathNode() {
        if (this.currentPath != null && !this.currentPath.isFinished()) {
            double d2;
            Vec3d vec3d = this.currentPath.getCurrentPos();
            this.currentPath.incrementPathIndex();
            double d0 = vec3d.x;
            double d1 = vec3d.z;
            while (!((d2 = vec3d.y + (double)(this.dragon.getRNG().nextFloat() * 20.0f)) >= vec3d.y)) {
            }
            this.targetLocation = new Vec3d(d0, d2, d1);
        }
    }
}

