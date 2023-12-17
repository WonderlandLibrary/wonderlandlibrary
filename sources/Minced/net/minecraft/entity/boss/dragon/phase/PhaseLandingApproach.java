// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.Vec3d;
import net.minecraft.pathfinding.Path;

public class PhaseLandingApproach extends PhaseBase
{
    private Path currentPath;
    private Vec3d targetLocation;
    
    public PhaseLandingApproach(final EntityDragon dragonIn) {
        super(dragonIn);
    }
    
    @Override
    public PhaseList<PhaseLandingApproach> getType() {
        return PhaseList.LANDING_APPROACH;
    }
    
    @Override
    public void initPhase() {
        this.currentPath = null;
        this.targetLocation = null;
    }
    
    @Override
    public void doLocalUpdate() {
        final double d0 = (this.targetLocation == null) ? 0.0 : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
        if (d0 < 100.0 || d0 > 22500.0 || this.dragon.collidedHorizontally || this.dragon.collidedVertically) {
            this.findNewTarget();
        }
    }
    
    @Nullable
    @Override
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }
    
    private void findNewTarget() {
        if (this.currentPath == null || this.currentPath.isFinished()) {
            final int i = this.dragon.initPathPoints();
            final BlockPos blockpos = this.dragon.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
            final EntityPlayer entityplayer = this.dragon.world.getNearestAttackablePlayer(blockpos, 128.0, 128.0);
            int j;
            if (entityplayer != null) {
                final Vec3d vec3d = new Vec3d(entityplayer.posX, 0.0, entityplayer.posZ).normalize();
                j = this.dragon.getNearestPpIdx(-vec3d.x * 40.0, 105.0, -vec3d.z * 40.0);
            }
            else {
                j = this.dragon.getNearestPpIdx(40.0, blockpos.getY(), 0.0);
            }
            final PathPoint pathpoint = new PathPoint(blockpos.getX(), blockpos.getY(), blockpos.getZ());
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
}
