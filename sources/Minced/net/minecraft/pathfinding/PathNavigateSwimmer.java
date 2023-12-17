// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public class PathNavigateSwimmer extends PathNavigate
{
    public PathNavigateSwimmer(final EntityLiving entitylivingIn, final World worldIn) {
        super(entitylivingIn, worldIn);
    }
    
    @Override
    protected PathFinder getPathFinder() {
        return new PathFinder(new SwimNodeProcessor());
    }
    
    @Override
    protected boolean canNavigate() {
        return this.isInLiquid();
    }
    
    @Override
    protected Vec3d getEntityPosition() {
        return new Vec3d(this.entity.posX, this.entity.posY + this.entity.height * 0.5, this.entity.posZ);
    }
    
    @Override
    protected void pathFollow() {
        final Vec3d vec3d = this.getEntityPosition();
        final float f = this.entity.width * this.entity.width;
        final int i = 6;
        if (vec3d.squareDistanceTo(this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex())) < f) {
            this.currentPath.incrementPathIndex();
        }
        for (int j = Math.min(this.currentPath.getCurrentPathIndex() + 6, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); --j) {
            final Vec3d vec3d2 = this.currentPath.getVectorFromIndex(this.entity, j);
            if (vec3d2.squareDistanceTo(vec3d) <= 36.0 && this.isDirectPathBetweenPoints(vec3d, vec3d2, 0, 0, 0)) {
                this.currentPath.setCurrentPathIndex(j);
                break;
            }
        }
        this.checkForStuck(vec3d);
    }
    
    @Override
    protected boolean isDirectPathBetweenPoints(final Vec3d posVec31, final Vec3d posVec32, final int sizeX, final int sizeY, final int sizeZ) {
        final RayTraceResult raytraceresult = this.world.rayTraceBlocks(posVec31, new Vec3d(posVec32.x, posVec32.y + this.entity.height * 0.5, posVec32.z), false, true, false);
        return raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS;
    }
    
    @Override
    public boolean canEntityStandOnPos(final BlockPos pos) {
        return !this.world.getBlockState(pos).isFullBlock();
    }
}
