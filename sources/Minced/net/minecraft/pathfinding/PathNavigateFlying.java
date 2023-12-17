// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public class PathNavigateFlying extends PathNavigate
{
    public PathNavigateFlying(final EntityLiving entityIn, final World worldIn) {
        super(entityIn, worldIn);
    }
    
    @Override
    protected PathFinder getPathFinder() {
        (this.nodeProcessor = new FlyingNodeProcessor()).setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }
    
    @Override
    protected boolean canNavigate() {
        return (this.canFloat() && this.isInLiquid()) || !this.entity.isRiding();
    }
    
    @Override
    protected Vec3d getEntityPosition() {
        return new Vec3d(this.entity.posX, this.entity.posY, this.entity.posZ);
    }
    
    @Override
    public Path getPathToEntityLiving(final Entity entityIn) {
        return this.getPathToPos(new BlockPos(entityIn));
    }
    
    @Override
    public void onUpdateNavigation() {
        ++this.totalTicks;
        if (this.tryUpdatePath) {
            this.updatePath();
        }
        if (!this.noPath()) {
            if (this.canNavigate()) {
                this.pathFollow();
            }
            else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
                final Vec3d vec3d = this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex());
                if (MathHelper.floor(this.entity.posX) == MathHelper.floor(vec3d.x) && MathHelper.floor(this.entity.posY) == MathHelper.floor(vec3d.y) && MathHelper.floor(this.entity.posZ) == MathHelper.floor(vec3d.z)) {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }
            this.debugPathFinding();
            if (!this.noPath()) {
                final Vec3d vec3d2 = this.currentPath.getPosition(this.entity);
                this.entity.getMoveHelper().setMoveTo(vec3d2.x, vec3d2.y, vec3d2.z, this.speed);
            }
        }
    }
    
    @Override
    protected boolean isDirectPathBetweenPoints(final Vec3d posVec31, final Vec3d posVec32, final int sizeX, final int sizeY, final int sizeZ) {
        int i = MathHelper.floor(posVec31.x);
        int j = MathHelper.floor(posVec31.y);
        int k = MathHelper.floor(posVec31.z);
        double d0 = posVec32.x - posVec31.x;
        double d2 = posVec32.y - posVec31.y;
        double d3 = posVec32.z - posVec31.z;
        final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
        if (d4 < 1.0E-8) {
            return false;
        }
        final double d5 = 1.0 / Math.sqrt(d4);
        d0 *= d5;
        d2 *= d5;
        d3 *= d5;
        final double d6 = 1.0 / Math.abs(d0);
        final double d7 = 1.0 / Math.abs(d2);
        final double d8 = 1.0 / Math.abs(d3);
        double d9 = i - posVec31.x;
        double d10 = j - posVec31.y;
        double d11 = k - posVec31.z;
        if (d0 >= 0.0) {
            ++d9;
        }
        if (d2 >= 0.0) {
            ++d10;
        }
        if (d3 >= 0.0) {
            ++d11;
        }
        d9 /= d0;
        d10 /= d2;
        d11 /= d3;
        final int l = (d0 < 0.0) ? -1 : 1;
        final int i2 = (d2 < 0.0) ? -1 : 1;
        final int j2 = (d3 < 0.0) ? -1 : 1;
        final int k2 = MathHelper.floor(posVec32.x);
        final int l2 = MathHelper.floor(posVec32.y);
        final int i3 = MathHelper.floor(posVec32.z);
        int j3 = k2 - i;
        int k3 = l2 - j;
        int l3 = i3 - k;
        while (j3 * l > 0 || k3 * i2 > 0 || l3 * j2 > 0) {
            if (d9 < d11 && d9 <= d10) {
                d9 += d6;
                i += l;
                j3 = k2 - i;
            }
            else if (d10 < d9 && d10 <= d11) {
                d10 += d7;
                j += i2;
                k3 = l2 - j;
            }
            else {
                d11 += d8;
                k += j2;
                l3 = i3 - k;
            }
        }
        return true;
    }
    
    public void setCanOpenDoors(final boolean canOpenDoorsIn) {
        this.nodeProcessor.setCanOpenDoors(canOpenDoorsIn);
    }
    
    public void setCanEnterDoors(final boolean canEnterDoorsIn) {
        this.nodeProcessor.setCanEnterDoors(canEnterDoorsIn);
    }
    
    public void setCanFloat(final boolean canFloatIn) {
        this.nodeProcessor.setCanSwim(canFloatIn);
    }
    
    public boolean canFloat() {
        return this.nodeProcessor.getCanSwim();
    }
    
    @Override
    public boolean canEntityStandOnPos(final BlockPos pos) {
        return this.world.getBlockState(pos).isTopSolid();
    }
}
