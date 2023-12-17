// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.IntHashMap;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.IBlockAccess;

public abstract class NodeProcessor
{
    protected IBlockAccess blockaccess;
    protected EntityLiving entity;
    protected final IntHashMap<PathPoint> pointMap;
    protected int entitySizeX;
    protected int entitySizeY;
    protected int entitySizeZ;
    protected boolean canEnterDoors;
    protected boolean canOpenDoors;
    protected boolean canSwim;
    
    public NodeProcessor() {
        this.pointMap = new IntHashMap<PathPoint>();
    }
    
    public void init(final IBlockAccess sourceIn, final EntityLiving mob) {
        this.blockaccess = sourceIn;
        this.entity = mob;
        this.pointMap.clearMap();
        this.entitySizeX = MathHelper.floor(mob.width + 1.0f);
        this.entitySizeY = MathHelper.floor(mob.height + 1.0f);
        this.entitySizeZ = MathHelper.floor(mob.width + 1.0f);
    }
    
    public void postProcess() {
        this.blockaccess = null;
        this.entity = null;
    }
    
    protected PathPoint openPoint(final int x, final int y, final int z) {
        final int i = PathPoint.makeHash(x, y, z);
        PathPoint pathpoint = this.pointMap.lookup(i);
        if (pathpoint == null) {
            pathpoint = new PathPoint(x, y, z);
            this.pointMap.addKey(i, pathpoint);
        }
        return pathpoint;
    }
    
    public abstract PathPoint getStart();
    
    public abstract PathPoint getPathPointToCoords(final double p0, final double p1, final double p2);
    
    public abstract int findPathOptions(final PathPoint[] p0, final PathPoint p1, final PathPoint p2, final float p3);
    
    public abstract PathNodeType getPathNodeType(final IBlockAccess p0, final int p1, final int p2, final int p3, final EntityLiving p4, final int p5, final int p6, final int p7, final boolean p8, final boolean p9);
    
    public abstract PathNodeType getPathNodeType(final IBlockAccess p0, final int p1, final int p2, final int p3);
    
    public void setCanEnterDoors(final boolean canEnterDoorsIn) {
        this.canEnterDoors = canEnterDoorsIn;
    }
    
    public void setCanOpenDoors(final boolean canOpenDoorsIn) {
        this.canOpenDoors = canOpenDoorsIn;
    }
    
    public void setCanSwim(final boolean canSwimIn) {
        this.canSwim = canSwimIn;
    }
    
    public boolean getCanEnterDoors() {
        return this.canEnterDoors;
    }
    
    public boolean getCanOpenDoors() {
        return this.canOpenDoors;
    }
    
    public boolean getCanSwim() {
        return this.canSwim;
    }
}
