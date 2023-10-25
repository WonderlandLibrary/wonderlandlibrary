/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.village;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSavedData;

public class VillageCollection
extends WorldSavedData {
    private final List<BlockPos> villagerPositionsList = Lists.newArrayList();
    private final List<Village> villageList;
    private int tickCounter;
    private World worldObj;
    private final List<VillageDoorInfo> newDoors = Lists.newArrayList();

    public static String fileNameForProvider(WorldProvider worldProvider) {
        return "villages" + worldProvider.getInternalNameSuffix();
    }

    private void removeAnnihilatedVillages() {
        Iterator<Village> iterator = this.villageList.iterator();
        while (iterator.hasNext()) {
            Village village = iterator.next();
            if (!village.isAnnihilated()) continue;
            iterator.remove();
            this.markDirty();
        }
    }

    public VillageCollection(World world) {
        super(VillageCollection.fileNameForProvider(world.provider));
        this.villageList = Lists.newArrayList();
        this.worldObj = world;
        this.markDirty();
    }

    public void addToVillagerPositionList(BlockPos blockPos) {
        if (this.villagerPositionsList.size() <= 64 && !this.positionInList(blockPos)) {
            this.villagerPositionsList.add(blockPos);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        this.tickCounter = nBTTagCompound.getInteger("Tick");
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Villages", 10);
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n);
            Village village = new Village();
            village.readVillageDataFromNBT(nBTTagCompound2);
            this.villageList.add(village);
            ++n;
        }
    }

    private void addToNewDoorsList(BlockPos blockPos) {
        int n;
        EnumFacing enumFacing = BlockDoor.getFacing(this.worldObj, blockPos);
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        int n2 = this.countBlocksCanSeeSky(blockPos, enumFacing, 5);
        if (n2 != (n = this.countBlocksCanSeeSky(blockPos, enumFacing2, n2 + 1))) {
            this.newDoors.add(new VillageDoorInfo(blockPos, n2 < n ? enumFacing : enumFacing2, this.tickCounter));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setInteger("Tick", this.tickCounter);
        NBTTagList nBTTagList = new NBTTagList();
        for (Village village : this.villageList) {
            NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
            village.writeVillageDataToNBT(nBTTagCompound2);
            nBTTagList.appendTag(nBTTagCompound2);
        }
        nBTTagCompound.setTag("Villages", nBTTagList);
    }

    public void tick() {
        ++this.tickCounter;
        for (Village village : this.villageList) {
            village.tick(this.tickCounter);
        }
        this.removeAnnihilatedVillages();
        this.dropOldestVillagerPosition();
        this.addNewDoorsToVillageOrCreateVillage();
        if (this.tickCounter % 400 == 0) {
            this.markDirty();
        }
    }

    private boolean isWoodDoor(BlockPos blockPos) {
        Block block = this.worldObj.getBlockState(blockPos).getBlock();
        return block instanceof BlockDoor ? block.getMaterial() == Material.wood : false;
    }

    public Village getNearestVillage(BlockPos blockPos, int n) {
        Village village = null;
        double d = 3.4028234663852886E38;
        for (Village village2 : this.villageList) {
            float f;
            double d2 = village2.getCenter().distanceSq(blockPos);
            if (!(d2 < d) || !(d2 <= (double)((f = (float)(n + village2.getVillageRadius())) * f))) continue;
            village = village2;
            d = d2;
        }
        return village;
    }

    private void addDoorsAround(BlockPos blockPos) {
        int n = 16;
        int n2 = 4;
        int n3 = 16;
        int n4 = -n;
        while (n4 < n) {
            int n5 = -n2;
            while (n5 < n2) {
                int n6 = -n3;
                while (n6 < n3) {
                    BlockPos blockPos2 = blockPos.add(n4, n5, n6);
                    if (this.isWoodDoor(blockPos2)) {
                        VillageDoorInfo villageDoorInfo = this.checkDoorExistence(blockPos2);
                        if (villageDoorInfo == null) {
                            this.addToNewDoorsList(blockPos2);
                        } else {
                            villageDoorInfo.func_179849_a(this.tickCounter);
                        }
                    }
                    ++n6;
                }
                ++n5;
            }
            ++n4;
        }
    }

    private int countBlocksCanSeeSky(BlockPos blockPos, EnumFacing enumFacing, int n) {
        int n2 = 0;
        int n3 = 1;
        while (n3 <= 5) {
            if (this.worldObj.canSeeSky(blockPos.offset(enumFacing, n3)) && ++n2 >= n) {
                return n2;
            }
            ++n3;
        }
        return n2;
    }

    public void setWorldsForAll(World world) {
        this.worldObj = world;
        for (Village village : this.villageList) {
            village.setWorld(world);
        }
    }

    private boolean positionInList(BlockPos blockPos) {
        for (BlockPos blockPos2 : this.villagerPositionsList) {
            if (!blockPos2.equals(blockPos)) continue;
            return true;
        }
        return false;
    }

    private void dropOldestVillagerPosition() {
        if (!this.villagerPositionsList.isEmpty()) {
            this.addDoorsAround(this.villagerPositionsList.remove(0));
        }
    }

    private VillageDoorInfo checkDoorExistence(BlockPos blockPos) {
        for (VillageDoorInfo object : this.newDoors) {
            if (object.getDoorBlockPos().getX() != blockPos.getX() || object.getDoorBlockPos().getZ() != blockPos.getZ() || Math.abs(object.getDoorBlockPos().getY() - blockPos.getY()) > 1) continue;
            return object;
        }
        for (Village village : this.villageList) {
            VillageDoorInfo villageDoorInfo = village.getExistedDoor(blockPos);
            if (villageDoorInfo == null) continue;
            return villageDoorInfo;
        }
        return null;
    }

    public List<Village> getVillageList() {
        return this.villageList;
    }

    public VillageCollection(String string) {
        super(string);
        this.villageList = Lists.newArrayList();
    }

    private void addNewDoorsToVillageOrCreateVillage() {
        int n = 0;
        while (n < this.newDoors.size()) {
            VillageDoorInfo villageDoorInfo = this.newDoors.get(n);
            Village village = this.getNearestVillage(villageDoorInfo.getDoorBlockPos(), 32);
            if (village == null) {
                village = new Village(this.worldObj);
                this.villageList.add(village);
                this.markDirty();
            }
            village.addVillageDoorInfo(villageDoorInfo);
            ++n;
        }
        this.newDoors.clear();
    }
}

