/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.SwimNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateSwimmer
extends PathNavigate {
    public PathNavigateSwimmer(EntityLiving entitylivingIn, World worldIn) {
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
        return new Vec3d(this.theEntity.posX, this.theEntity.posY + (double)this.theEntity.height * 0.5, this.theEntity.posZ);
    }

    @Override
    protected void pathFollow() {
        Vec3d vec3d = this.getEntityPosition();
        float f = this.theEntity.width * this.theEntity.width;
        int i = 6;
        if (vec3d.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex())) < (double)f) {
            this.currentPath.incrementPathIndex();
        }
        for (int j = Math.min(this.currentPath.getCurrentPathIndex() + 6, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); --j) {
            Vec3d vec3d1 = this.currentPath.getVectorFromIndex(this.theEntity, j);
            if (!(vec3d1.squareDistanceTo(vec3d) <= 36.0) || !this.isDirectPathBetweenPoints(vec3d, vec3d1, 0, 0, 0)) continue;
            this.currentPath.setCurrentPathIndex(j);
            break;
        }
        this.checkForStuck(vec3d);
    }

    @Override
    protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ) {
        RayTraceResult raytraceresult = this.worldObj.rayTraceBlocks(posVec31, new Vec3d(posVec32.x, posVec32.y + (double)this.theEntity.height * 0.5, posVec32.z), false, true, false);
        return raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS;
    }

    @Override
    public boolean canEntityStandOnPos(BlockPos pos) {
        return !this.worldObj.getBlockState(pos).isFullBlock();
    }
}

