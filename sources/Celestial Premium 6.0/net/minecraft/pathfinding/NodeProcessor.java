/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public abstract class NodeProcessor {
    protected IBlockAccess blockaccess;
    protected EntityLiving entity;
    protected final IntHashMap<PathPoint> pointMap = new IntHashMap();
    protected int entitySizeX;
    protected int entitySizeY;
    protected int entitySizeZ;
    protected boolean canEnterDoors;
    protected boolean canBreakDoors;
    protected boolean canSwim;

    public void initProcessor(IBlockAccess sourceIn, EntityLiving mob) {
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

    protected PathPoint openPoint(int x, int y, int z) {
        int i = PathPoint.makeHash(x, y, z);
        PathPoint pathpoint = this.pointMap.lookup(i);
        if (pathpoint == null) {
            pathpoint = new PathPoint(x, y, z);
            this.pointMap.addKey(i, pathpoint);
        }
        return pathpoint;
    }

    public abstract PathPoint getStart();

    public abstract PathPoint getPathPointToCoords(double var1, double var3, double var5);

    public abstract int findPathOptions(PathPoint[] var1, PathPoint var2, PathPoint var3, float var4);

    public abstract PathNodeType getPathNodeType(IBlockAccess var1, int var2, int var3, int var4, EntityLiving var5, int var6, int var7, int var8, boolean var9, boolean var10);

    public abstract PathNodeType getPathNodeType(IBlockAccess var1, int var2, int var3, int var4);

    public void setCanEnterDoors(boolean canEnterDoorsIn) {
        this.canEnterDoors = canEnterDoorsIn;
    }

    public void setCanBreakDoors(boolean canBreakDoorsIn) {
        this.canBreakDoors = canBreakDoorsIn;
    }

    public void setCanSwim(boolean canSwimIn) {
        this.canSwim = canSwimIn;
    }

    public boolean getCanEnterDoors() {
        return this.canEnterDoors;
    }

    public boolean getCanBreakDoors() {
        return this.canBreakDoors;
    }

    public boolean getCanSwim() {
        return this.canSwim;
    }
}

