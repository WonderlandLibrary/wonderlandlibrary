/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.pathfinder;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public abstract class NodeProcessor {
    protected int entitySizeZ;
    protected int entitySizeY;
    protected IntHashMap<PathPoint> pointMap = new IntHashMap();
    protected IBlockAccess blockaccess;
    protected int entitySizeX;

    public void postProcess() {
    }

    protected PathPoint openPoint(int n, int n2, int n3) {
        int n4 = PathPoint.makeHash(n, n2, n3);
        PathPoint pathPoint = this.pointMap.lookup(n4);
        if (pathPoint == null) {
            pathPoint = new PathPoint(n, n2, n3);
            this.pointMap.addKey(n4, pathPoint);
        }
        return pathPoint;
    }

    public abstract PathPoint getPathPointTo(Entity var1);

    public abstract PathPoint getPathPointToCoords(Entity var1, double var2, double var4, double var6);

    public void initProcessor(IBlockAccess iBlockAccess, Entity entity) {
        this.blockaccess = iBlockAccess;
        this.pointMap.clearMap();
        this.entitySizeX = MathHelper.floor_float(entity.width + 1.0f);
        this.entitySizeY = MathHelper.floor_float(entity.height + 1.0f);
        this.entitySizeZ = MathHelper.floor_float(entity.width + 1.0f);
    }

    public abstract int findPathOptions(PathPoint[] var1, Entity var2, PathPoint var3, PathPoint var4, float var5);
}

