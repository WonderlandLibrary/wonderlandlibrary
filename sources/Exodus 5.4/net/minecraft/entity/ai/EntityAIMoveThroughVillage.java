/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.entity.ai;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveThroughVillage
extends EntityAIBase {
    private boolean isNocturnal;
    private double movementSpeed;
    private VillageDoorInfo doorInfo;
    private List<VillageDoorInfo> doorList = Lists.newArrayList();
    private PathEntity entityPathNavigate;
    private EntityCreature theEntity;

    private VillageDoorInfo findNearestDoor(Village village) {
        VillageDoorInfo villageDoorInfo = null;
        int n = Integer.MAX_VALUE;
        for (VillageDoorInfo villageDoorInfo2 : village.getVillageDoorInfoList()) {
            int n2 = villageDoorInfo2.getDistanceSquared(MathHelper.floor_double(this.theEntity.posX), MathHelper.floor_double(this.theEntity.posY), MathHelper.floor_double(this.theEntity.posZ));
            if (n2 >= n || this.doesDoorListContain(villageDoorInfo2)) continue;
            villageDoorInfo = villageDoorInfo2;
            n = n2;
        }
        return villageDoorInfo;
    }

    @Override
    public boolean continueExecuting() {
        if (this.theEntity.getNavigator().noPath()) {
            return false;
        }
        float f = this.theEntity.width + 4.0f;
        return this.theEntity.getDistanceSq(this.doorInfo.getDoorBlockPos()) > (double)(f * f);
    }

    private boolean doesDoorListContain(VillageDoorInfo villageDoorInfo) {
        for (VillageDoorInfo villageDoorInfo2 : this.doorList) {
            if (!villageDoorInfo.getDoorBlockPos().equals(villageDoorInfo2.getDoorBlockPos())) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldExecute() {
        this.resizeDoorList();
        if (this.isNocturnal && this.theEntity.worldObj.isDaytime()) {
            return false;
        }
        Village village = this.theEntity.worldObj.getVillageCollection().getNearestVillage(new BlockPos(this.theEntity), 0);
        if (village == null) {
            return false;
        }
        this.doorInfo = this.findNearestDoor(village);
        if (this.doorInfo == null) {
            return false;
        }
        PathNavigateGround pathNavigateGround = (PathNavigateGround)this.theEntity.getNavigator();
        boolean bl = pathNavigateGround.getEnterDoors();
        pathNavigateGround.setBreakDoors(false);
        this.entityPathNavigate = pathNavigateGround.getPathToPos(this.doorInfo.getDoorBlockPos());
        pathNavigateGround.setBreakDoors(bl);
        if (this.entityPathNavigate != null) {
            return true;
        }
        Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 10, 7, new Vec3(this.doorInfo.getDoorBlockPos().getX(), this.doorInfo.getDoorBlockPos().getY(), this.doorInfo.getDoorBlockPos().getZ()));
        if (vec3 == null) {
            return false;
        }
        pathNavigateGround.setBreakDoors(false);
        this.entityPathNavigate = this.theEntity.getNavigator().getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
        pathNavigateGround.setBreakDoors(bl);
        return this.entityPathNavigate != null;
    }

    private void resizeDoorList() {
        if (this.doorList.size() > 15) {
            this.doorList.remove(0);
        }
    }

    public EntityAIMoveThroughVillage(EntityCreature entityCreature, double d, boolean bl) {
        this.theEntity = entityCreature;
        this.movementSpeed = d;
        this.isNocturnal = bl;
        this.setMutexBits(1);
        if (!(entityCreature.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
        }
    }

    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().setPath(this.entityPathNavigate, this.movementSpeed);
    }

    @Override
    public void resetTask() {
        if (this.theEntity.getNavigator().noPath() || this.theEntity.getDistanceSq(this.doorInfo.getDoorBlockPos()) < 16.0) {
            this.doorList.add(this.doorInfo);
        }
    }
}

