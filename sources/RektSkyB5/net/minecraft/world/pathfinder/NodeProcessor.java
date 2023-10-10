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
    protected IBlockAccess blockaccess;
    protected IntHashMap<PathPoint> pointMap = new IntHashMap();
    protected int entitySizeX;
    protected int entitySizeY;
    protected int entitySizeZ;

    public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn) {
        this.blockaccess = iblockaccessIn;
        this.pointMap.clearMap();
        this.entitySizeX = MathHelper.floor_float(entityIn.width + 1.0f);
        this.entitySizeY = MathHelper.floor_float(entityIn.height + 1.0f);
        this.entitySizeZ = MathHelper.floor_float(entityIn.width + 1.0f);
    }

    public void postProcess() {
    }

    protected PathPoint openPoint(int x2, int y2, int z2) {
        int i2 = PathPoint.makeHash(x2, y2, z2);
        PathPoint pathpoint = this.pointMap.lookup(i2);
        if (pathpoint == null) {
            pathpoint = new PathPoint(x2, y2, z2);
            this.pointMap.addKey(i2, pathpoint);
        }
        return pathpoint;
    }

    public abstract PathPoint getPathPointTo(Entity var1);

    public abstract PathPoint getPathPointToCoords(Entity var1, double var2, double var4, double var6);

    public abstract int findPathOptions(PathPoint[] var1, Entity var2, PathPoint var3, PathPoint var4, float var5);
}

