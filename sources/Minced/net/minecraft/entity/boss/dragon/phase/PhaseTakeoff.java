// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.Vec3d;
import net.minecraft.pathfinding.Path;

public class PhaseTakeoff extends PhaseBase
{
    private boolean firstTick;
    private Path currentPath;
    private Vec3d targetLocation;
    
    public PhaseTakeoff(final EntityDragon dragonIn) {
        super(dragonIn);
    }
    
    @Override
    public void doLocalUpdate() {
        if (!this.firstTick && this.currentPath != null) {
            final BlockPos blockpos = this.dragon.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
            final double d0 = this.dragon.getDistanceSqToCenter(blockpos);
            if (d0 > 100.0) {
                this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
            }
        }
        else {
            this.firstTick = false;
            this.findNewTarget();
        }
    }
    
    @Override
    public void initPhase() {
        this.firstTick = true;
        this.currentPath = null;
        this.targetLocation = null;
    }
    
    private void findNewTarget() {
        final int i = this.dragon.initPathPoints();
        final Vec3d vec3d = this.dragon.getHeadLookVec(1.0f);
        int j = this.dragon.getNearestPpIdx(-vec3d.x * 40.0, 105.0, -vec3d.z * 40.0);
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
            this.navigateToNextPathNode();
        }
    }
    
    private void navigateToNextPathNode() {
        final Vec3d vec3d = this.currentPath.getCurrentPos();
        this.currentPath.incrementPathIndex();
        double d0;
        do {
            d0 = vec3d.y + this.dragon.getRNG().nextFloat() * 20.0f;
        } while (d0 < vec3d.y);
        this.targetLocation = new Vec3d(vec3d.x, d0, vec3d.z);
    }
    
    @Nullable
    @Override
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }
    
    @Override
    public PhaseList<PhaseTakeoff> getType() {
        return PhaseList.TAKEOFF;
    }
}
